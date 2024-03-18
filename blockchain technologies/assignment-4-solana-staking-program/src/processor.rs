use solana_program::{
    account_info::{next_account_info, AccountInfo},
    clock::Clock,
    entrypoint::ProgramResult,
    native_token::LAMPORTS_PER_SOL,
    program::{invoke, invoke_signed},
    program_error::ProgramError,
    pubkey::Pubkey,
    system_instruction,
    sysvar::{rent::Rent, Sysvar},
};

use crate::{
    error::StakeError,
    instruction::{InitializeStakingPoolData, StakeTokensData, UnstakeTokensData},
    state::{PoolState, Treasury},
    state::{TreasuryToken, UserState},
};

pub struct Processor;
impl Processor {
    pub fn initialize_staking_pool(
        program_id: &Pubkey,
        accounts: &[AccountInfo],
        instruction: InitializeStakingPoolData,
    ) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let token_mint_account = next_account_info(iter)?;
        let treasury_pda_account = next_account_info(iter)?;
        let treasury_token_pda_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let rent_account = next_account_info(iter)?;
        let token_program_account = next_account_info(iter)?;

        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }

        // ----------------- POOL STATE DERIVATION ------------------
        // Derive a pda
        let (pool_state_pda, bump) = Pubkey::find_program_address(
            &[
                PoolState::SEED_PREFIX.as_bytes(),
                initializer_account.key.as_ref(),
                pool_state_pda_account.key.as_ref(),
                instruction.seed.to_le_bytes().as_ref(),
            ],
            program_id,
        );

        // Check if pda is same as user given address
        if *pool_state_pda_account.key != pool_state_pda {
            return Err(StakeError::WrongUserState.into());
        }

        // Create PDA account
        invoke_signed(
            &system_instruction::create_account(
                initializer_account.key,
                pool_state_pda_account.key,
                Rent::get()?.minimum_balance(PoolState::LEN as usize),
                PoolState::LEN,
                program_id,
            ),
            &[initializer_account.clone(), pool_state_pda_account.clone()],
            &[&[
                UserState::SEED_PREFIX.as_bytes(),
                initializer_account.key.as_ref(),
                pool_state_pda_account.key.as_ref(),
                instruction.seed.to_le_bytes().as_ref(),
                &[bump],
            ]],
        )?;

        // ----------------- TREASURY PDA DERIVATION ------------------

        // Derive a pda
        let (treasury_pda, bump) = Pubkey::find_program_address(
            &[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
            ],
            program_id,
        );

        // Check if pda is same as user given address
        if *treasury_pda_account.key != treasury_pda {
            return Err(StakeError::WrongTreasury.into());
        }

        // Create PDA account
        invoke_signed(
            &system_instruction::create_account(
                initializer_account.key,
                treasury_pda_account.key,
                Rent::get()?.minimum_balance(0),
                0,
                program_id,
            ),
            &[initializer_account.clone(), treasury_pda_account.clone()],
            &[&[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
                &[bump],
            ]],
        )?;

        // ----------------- TREASURY TOKEN PDA DERIVATION ------------------

        // Derive a pda
        let (treasury_token_pda, bump) = Pubkey::find_program_address(
            &[
                TreasuryToken::SEED_PREFIX.as_bytes(),
                treasury_pda_account.key.as_ref(),
            ],
            program_id,
        );

        // Check if pda is same as user given address
        if *treasury_token_pda_account.key != treasury_token_pda {
            return Err(StakeError::WrongTreasuryToken.into());
        }

        // Create PDA account
        invoke_signed(
            &system_instruction::create_account(
                initializer_account.key,
                treasury_token_pda_account.key,
                Rent::get()?.minimum_balance(TreasuryToken::LEN as usize),
                TreasuryToken::LEN,
                token_program_account.key,
            ),
            &[initializer_account.clone(), treasury_pda_account.clone()],
            &[&[
                TreasuryToken::SEED_PREFIX.as_bytes(),
                treasury_pda_account.key.as_ref(),
                &[bump],
            ]],
        )?;

        // ----------------- TOKEN ACCOUNT CREATION ------------------
        invoke(
            &spl_token::instruction::initialize_account(
                &spl_token::ID,
                treasury_token_pda_account.key,
                token_mint_account.key,
                treasury_pda_account.key,
            )?,
            &[
                treasury_token_pda_account.clone(),
                token_mint_account.clone(),
                treasury_pda_account.clone(),
                rent_account.clone(),
            ],
        )?;

        // ------------------- SET POOL STATE DATA --------------------

        // Unpack from empty account
        let mut pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;
        pool_state.initializer = *initializer_account.key;
        pool_state.total_staked_tokens = 0;
        pool_state.reward_per_token_stored = 0;
        pool_state.token_mint = *token_mint_account.key;
        pool_state.treasury_pda = *treasury_pda_account.key;
        pool_state.treasury_token_account = *treasury_token_pda_account.key;
        pool_state.reward_rate = instruction.reward_rate;
        pool_state.last_update_time = Clock::get()?.unix_timestamp;
        pool_state.initialized = true;
        pool_state.launched = false;

        // Pack into account and save pool data
        pool_state.pack_to_account(pool_state_pda_account)?;

        // All done, FINALLY finished initializations for everything
        Ok(())
    }

    pub fn launch_staking(_program_id: &Pubkey, accounts: &[AccountInfo]) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let mut pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;

        // 3 Checks
        // Check if pool initializer has signed the transaction
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if initializer account address is the same we have saved in the pda
        if *initializer_account.key != pool_state.initializer {
            return Err(StakeError::WrongInitializer.into());
        }
        // Check if pool is already launched
        if pool_state.launched {
            return Err(StakeError::PoolAlreadyLaunched.into());
        }

        // Update pool state
        pool_state.launched = true;

        // Serialize and pack
        pool_state.pack_to_account(pool_state_pda_account)?;

        // All good, pool launched
        Ok(())
    }

    pub fn create_staking_account(program_id: &Pubkey, accounts: &[AccountInfo]) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let user_state_pda_account = next_account_info(iter)?; // User gives just 2 addresses right? we can derive pda so no need to pass it
        let pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;

        // Check if transaction has been signed
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if pool is initialized and launched
        if !pool_state.initialized {
            return Err(StakeError::PoolNotInitialized.into());
        }
        if !pool_state.launched {
            return Err(StakeError::PoolNotLaunched.into());
        }

        // Derive a pda
        let (user_state_pda, bump) = Pubkey::find_program_address(
            &[
                UserState::SEED_PREFIX.as_bytes(),
                initializer_account.key.as_ref(),
                pool_state_pda_account.key.as_ref(),
            ],
            program_id,
        );

        // Check if pda is same as user given address
        if *user_state_pda_account.key != user_state_pda {
            return Err(StakeError::WrongUserState.into());
        }

        // Create PDA account
        invoke_signed(
            &system_instruction::create_account(
                initializer_account.key,
                user_state_pda_account.key,
                Rent::get()?.minimum_balance(UserState::LEN as usize),
                UserState::LEN,
                program_id,
            ),
            &[initializer_account.clone(), user_state_pda_account.clone()],
            &[&[
                UserState::SEED_PREFIX.as_bytes(),
                initializer_account.key.as_ref(),
                pool_state_pda_account.key.as_ref(),
                &[bump],
            ]],
        )?;

        // Unpack to modify
        let mut user_state = UserState::unpack_from_account(user_state_pda_account)?;

        user_state.initializer = *initializer_account.key;
        user_state.pool_state_account = *pool_state_pda_account.key;
        user_state.staked_token_amount = 0;
        user_state.rewards_per_token_paid = 0;
        user_state.rewards = 0;
        user_state.last_interaction_time = Clock::get()?.unix_timestamp;
        user_state.initialized = true;

        // Pack and save account
        user_state.pack_to_account(user_state_pda_account)?;

        // All good, UserState pda account has been created and initialized
        Ok(())
    }

    pub fn stake_tokens(
        _program_id: &Pubkey,
        accounts: &[AccountInfo],
        data: StakeTokensData,
    ) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let initializer_token_account = next_account_info(iter)?;
        let treasury_token_pda_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let user_state_pda_account = next_account_info(iter)?;
        let token_program_account = next_account_info(iter)?;

        let mut pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;
        let mut user_state = UserState::unpack_from_account(user_state_pda_account)?;

        // Check if transaction has been signed
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if user_state belongs to initializer
        if *initializer_account.key != user_state.initializer {
            return Err(StakeError::WrongInitializer.into());
        }
        // Check if pool is initialized and launched
        if !pool_state.initialized {
            return Err(StakeError::PoolNotInitialized.into());
        }
        if !pool_state.launched {
            return Err(StakeError::PoolNotLaunched.into());
        }
        // Check if user state is initialized
        if !user_state.initialized {
            return Err(StakeError::UserNotInitialized.into());
        }
        // Check if user state has the correct pool
        if user_state.pool_state_account != *pool_state_pda_account.key {
            return Err(StakeError::WrongPoolState.into());
        }

        // UPDATE REWARDS HERE
        Processor::rewards(&mut pool_state, &mut user_state)?;

        invoke(
            &spl_token::instruction::transfer(
                token_program_account.key,
                initializer_token_account.key,
                &pool_state.treasury_token_account,
                initializer_account.key,
                &[],
                data.tokens as u64,
            )?,
            &[
                initializer_account.clone(),
                treasury_token_pda_account.clone(),
                initializer_account.clone(),
                token_program_account.clone(),
            ],
        )?;

        pool_state.total_staked_tokens += data.tokens;
        user_state.staked_token_amount += data.tokens;
        pool_state.pack_to_account(pool_state_pda_account)?;
        user_state.pack_to_account(user_state_pda_account)?;

        // All done
        Ok(())
    }

    pub fn unstake_tokens(
        program_id: &Pubkey,
        accounts: &[AccountInfo],
        data: UnstakeTokensData,
    ) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let initializer_token_account = next_account_info(iter)?;
        let treasury_pda_account = next_account_info(iter)?;
        let treasury_token_pda_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let user_state_pda_account = next_account_info(iter)?;
        let token_program_account = next_account_info(iter)?;
        let token_mint_account = next_account_info(iter)?;

        let mut pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;
        let mut user_state = UserState::unpack_from_account(user_state_pda_account)?;

        // Check if transaction has been signed
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if user_state belongs to initializer
        if *initializer_account.key != user_state.initializer {
            return Err(StakeError::WrongInitializer.into());
        }
        // Check if pool is initialized and launched
        if !pool_state.initialized {
            return Err(StakeError::PoolNotInitialized.into());
        }
        if !pool_state.launched {
            return Err(StakeError::PoolNotLaunched.into());
        }
        // Check if user state is initialized
        if !user_state.initialized {
            return Err(StakeError::UserNotInitialized.into());
        }
        // Check if user state has the correct pool
        if user_state.pool_state_account != *pool_state_pda_account.key {
            return Err(StakeError::WrongPoolState.into());
        }
        if data.tokens > user_state.staked_token_amount {
            return Err(StakeError::NotEnoughTokens.into());
        }

        // UPDATE REWARDS HERE
        Processor::rewards(&mut pool_state, &mut user_state)?;

        // Derive PDA
        let (_, bump) = Pubkey::find_program_address(
            &[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
            ],
            program_id,
        );

        invoke_signed(
            &spl_token::instruction::transfer(
                token_program_account.key,
                &pool_state.treasury_token_account,
                initializer_token_account.key,
                treasury_pda_account.key,
                &[],
                data.tokens as u64,
            )?,
            &[
                initializer_account.clone(),
                treasury_token_pda_account.clone(),
                token_program_account.clone(),
                initializer_token_account.clone(),
                treasury_pda_account.clone(),
            ],
            &[&[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
                &[bump],
            ]],
        )?;

        pool_state.total_staked_tokens -= data.tokens;
        user_state.staked_token_amount -= data.tokens;
        pool_state.pack_to_account(pool_state_pda_account)?;
        user_state.pack_to_account(user_state_pda_account)?;

        // All done
        Ok(())
    }

    pub fn get_rewards(program_id: &Pubkey, accounts: &[AccountInfo]) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let initializer_token_account = next_account_info(iter)?;
        let treasury_pda_account = next_account_info(iter)?;
        let treasury_token_pda_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let user_state_pda_account = next_account_info(iter)?;
        let token_program_account = next_account_info(iter)?;
        let token_mint_account = next_account_info(iter)?;

        let mut pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;
        let mut user_state = UserState::unpack_from_account(user_state_pda_account)?;

        // Check if transaction has been signed
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if user_state belongs to initializer
        if *initializer_account.key != user_state.initializer {
            return Err(StakeError::WrongInitializer.into());
        }
        // Check if pool is initialized and launched
        if !pool_state.initialized {
            return Err(StakeError::PoolNotInitialized.into());
        }
        if !pool_state.launched {
            return Err(StakeError::PoolNotLaunched.into());
        }
        // Check if user state is initialized
        if !user_state.initialized {
            return Err(StakeError::UserNotInitialized.into());
        }
        // Check if user state has the correct pool
        if user_state.pool_state_account != *pool_state_pda_account.key {
            return Err(StakeError::WrongPoolState.into());
        }

        // UPDATE REWARDS HERE
        Processor::rewards(&mut pool_state, &mut user_state)?;

        // Derive PDA
        let (_, bump) = Pubkey::find_program_address(
            &[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
            ],
            program_id,
        );

        invoke_signed(
            &spl_token::instruction::transfer(
                token_program_account.key,
                &pool_state.treasury_token_account,
                initializer_token_account.key,
                treasury_pda_account.key,
                &[],
                user_state.rewards as u64,
            )?,
            &[
                initializer_account.clone(),
                treasury_token_pda_account.clone(),
                token_program_account.clone(),
                initializer_token_account.clone(),
                treasury_pda_account.clone(),
            ],
            &[&[
                Treasury::SEED_PREFIX.as_bytes(),
                pool_state_pda_account.key.as_ref(),
                token_mint_account.key.as_ref(),
                &[bump],
            ]],
        )?;

        user_state.rewards = 0;
        user_state.pack_to_account(user_state_pda_account)?;

        // All done
        Ok(())
    }

    pub fn close_stake_account(_program_id: &Pubkey, accounts: &[AccountInfo]) -> ProgramResult {
        let iter = &mut accounts.iter();

        let initializer_account = next_account_info(iter)?;
        let pool_state_pda_account = next_account_info(iter)?;
        let user_state_pda_account = next_account_info(iter)?;
        let _pool_state = PoolState::unpack_from_account(pool_state_pda_account)?;
        let mut user_state = UserState::unpack_from_account(user_state_pda_account)?;

        // Check if transaction has been signed
        if !initializer_account.is_signer {
            return Err(ProgramError::MissingRequiredSignature);
        }
        // Check if user_state belongs to initializer
        if *initializer_account.key != user_state.initializer {
            return Err(StakeError::WrongInitializer.into());
        }
        // Check if user_state is for pool_state
        if *pool_state_pda_account.key != user_state.pool_state_account {
            return Err(StakeError::WrongPoolState.into());
        }
        // Check if rewards or staked tokens is empty
        if user_state.staked_token_amount != 0 || user_state.rewards != 0 {
            return Err(StakeError::UserStateNotEmpty.into());
        }
        // Transfer remaining lamports to initializer
        **initializer_account.try_borrow_mut_lamports()? =
            initializer_account.lamports() + user_state_pda_account.lamports();
        **user_state_pda_account.try_borrow_mut_lamports()? = 0;

        // Mark this pda as unused, if user decides to open this account again...
        user_state.initialized = false;

        // Save to pda
        user_state.pack_to_account(user_state_pda_account)?;

        // All good, staking account closed
        Ok(())
    }

    fn calculate_rewards(pool_state: &mut PoolState, user_state: &mut UserState) -> u64 {
        let staked = user_state.staked_token_amount;
        let rpts = pool_state.reward_per_token_stored;
        let rptp = user_state.rewards_per_token_paid;
        let div = LAMPORTS_PER_SOL as u128 * LAMPORTS_PER_SOL as u128;
        let rewards = user_state.rewards as u128;

        return (staked * (rpts - rptp) / div + rewards) as u64;
    }
    fn get_reward_per_token(pool_state: &mut PoolState, now: i64) -> u128 {
        if pool_state.total_staked_tokens == 0 {
            return 0;
        }
        let rpts = pool_state.reward_per_token_stored;
        let rr = pool_state.reward_rate as u128;
        let seconds = (now - pool_state.last_update_time) as u128;
        let div = (LAMPORTS_PER_SOL as u128 * LAMPORTS_PER_SOL as u128)
            as u128;
        let total = pool_state.total_staked_tokens;

        return rpts + (rr * (seconds) * div / total);
    }

    fn rewards(pool_state: &mut PoolState, user_state: &mut UserState) -> ProgramResult {
        let now = Clock::get()?.unix_timestamp;

        let reward_per_token: u128 = Processor::get_reward_per_token(pool_state, now);
        pool_state.reward_per_token_stored = reward_per_token;
        pool_state.last_update_time = now;

        let rewards = Processor::calculate_rewards(pool_state, user_state);
        user_state.rewards = rewards;
        user_state.rewards_per_token_paid = reward_per_token;

        Ok(())
    }

    // fn find_user_pda(program_id: &Pubkey, initializer: &AccountInfo, pool_pda: &AccountInfo) -> Pubkey {
    //     let (pda , _bump) = Pubkey::find_program_address(
    //         &[
    //             UserState::SEED_PREFIX.as_bytes(),
    //             initializer.key.as_ref(),
    //             pool_pda.key.as_ref(),
    //         ],
    //         program_id
    //     );
    //     return pda;
    // }
}

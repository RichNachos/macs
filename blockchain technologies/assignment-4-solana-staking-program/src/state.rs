use borsh::{BorshDeserialize, BorshSerialize};
use solana_program::{
    account_info::AccountInfo, entrypoint::ProgramResult, program_error::ProgramError, program_pack::Pack, pubkey::Pubkey
};

#[derive(BorshSerialize, BorshDeserialize, PartialEq, Debug)]
pub struct PoolState {
    pub initializer: Pubkey,            // 32
    pub total_staked_tokens: u128,      // 16
    pub reward_per_token_stored: u128,  // 16
    pub token_mint: Pubkey,             // 32
    pub treasury_pda: Pubkey,           // 32
    pub treasury_token_account: Pubkey, // 32
    pub reward_rate: u64,               // 8
    pub last_update_time: i64,          // 8
    pub initialized: bool,              // 1
    pub launched: bool,                 // 1
} // 178

#[derive(BorshSerialize, BorshDeserialize, PartialEq, Debug)]
pub struct UserState {
    pub initializer: Pubkey,          // 32
    pub pool_state_account: Pubkey,   // 32
    pub staked_token_amount: u128,    // 16
    pub rewards_per_token_paid: u128, // 16
    pub rewards: u64,                 // 8
    pub last_interaction_time: i64,   // 8
    pub initialized: bool,            // 1
} // 113

pub struct Treasury;
pub struct TreasuryToken;

impl PoolState {
    pub const LEN: u64 = 178;
    pub const SEED_PREFIX: &'static str = "pool_state";

    pub fn unpack_from_account(pool_account: &AccountInfo) -> Result<PoolState, ProgramError> {
        let data = pool_account.try_borrow_data()?;
        let bytes = data.as_ref();
        let result = PoolState::try_from_slice(bytes)?;

        return Ok(result);
    }

    pub fn pack_to_account(self, pool_account: &AccountInfo) -> ProgramResult {
        let bytes = self.try_to_vec()?;
        let mut data = pool_account.data.borrow_mut();
        data.copy_from_slice(&bytes[..]);

        Ok(())
    }
}

impl UserState {
    pub const LEN: u64 = 113;
    pub const SEED_PREFIX: &'static str = "user_state";

    pub fn unpack_from_account(user_account: &AccountInfo) -> Result<UserState, ProgramError> {
        let data = user_account.try_borrow_data()?;
        let bytes = data.as_ref();
        let result = UserState::try_from_slice(bytes)?;

        return Ok(result);
    }

    pub fn pack_to_account(self, account: &AccountInfo) -> ProgramResult {
        let bytes = self.try_to_vec()?;
        let mut data = account.data.borrow_mut();
        data.copy_from_slice(&bytes[..]);

        Ok(())
    }
}

impl Treasury {
    pub const LEN: u64 = 0;
    pub const SEED_PREFIX: &'static str = "treasury";
}

impl TreasuryToken {
    pub const LEN: u64 = spl_token::state::Account::LEN as u64;
    pub const SEED_PREFIX: &'static str = "treasury_token";
}
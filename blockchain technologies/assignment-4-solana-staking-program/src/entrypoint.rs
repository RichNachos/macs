use solana_program::{
    account_info::AccountInfo, entrypoint, entrypoint::ProgramResult, msg, pubkey::Pubkey,
};

use crate::{instruction::StakeInstruction, processor::Processor};

entrypoint!(process_instruction);
fn process_instruction(
    program_id: &Pubkey,
    accounts: &[AccountInfo],
    instruction_data: &[u8],
) -> ProgramResult {
    msg!("Starting Transaction...");
    msg!("Processing Instruction...");

    let instruction = StakeInstruction::unpack(instruction_data)?;
    match instruction {
        StakeInstruction::InitializeStakingPool(data) => {
            Processor::initialize_staking_pool(program_id, accounts, data)
        }
        StakeInstruction::LaunchStaking => Processor::launch_staking(program_id, accounts),
        StakeInstruction::CreateStakingAccount => {
            Processor::create_staking_account(program_id, accounts)
        }
        StakeInstruction::StakeTokens(data) => Processor::stake_tokens(program_id, accounts, data),
        StakeInstruction::UnstakeTokens(data) => {
            Processor::unstake_tokens(program_id, accounts, data)
        }
        StakeInstruction::GetRewards => Processor::get_rewards(program_id, accounts),
        StakeInstruction::CloseStakeAccount => Processor::close_stake_account(program_id, accounts),
    }
}

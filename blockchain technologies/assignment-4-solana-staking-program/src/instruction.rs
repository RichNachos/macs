use borsh::{BorshDeserialize, BorshSerialize};
use solana_program::program_error::ProgramError;

use crate::error::StakeError;

pub enum StakeInstruction {
    // []
    InitializeStakingPool(InitializeStakingPoolData),
    
    // [Signer] initializer
    // [Writable] pool_state_pda
    LaunchStaking,
    
    // [Signer] initializer
    // [] pool_state_pda
    // [Writable] user_state_pda
    CreateStakingAccount,
    
    // [Signer] initializer
    StakeTokens(StakeTokensData),
    
    // [Signer] initializer
    UnstakeTokens(UnstakeTokensData),
    
    // [Signer] initializer
    GetRewards,

    // [Signer] initializer
    // [] pool_state_pda
    // [Writable] user_state_pda
    CloseStakeAccount,
}

#[derive(BorshSerialize, BorshDeserialize, Debug, PartialEq)]
pub struct InitializeStakingPoolData {
    pub reward_rate: u64,
    pub seed: u64,
}

#[derive(BorshSerialize, BorshDeserialize, Debug, PartialEq)]
pub struct StakeTokensData {
    pub tokens: u128,
}

#[derive(BorshSerialize, BorshDeserialize, Debug, PartialEq)]
pub struct UnstakeTokensData {
    pub tokens: u128,
}

impl StakeInstruction {
    pub fn unpack(data: &[u8]) -> Result<StakeInstruction, ProgramError> {
        let (instruction_byte, instruction_data) = data
            .split_first()
            .ok_or(StakeError::InvalidInstruction)?;

        match instruction_byte {
            0 => {
                let data = InitializeStakingPoolData::try_from_slice(instruction_data)?;
                return Ok(StakeInstruction::InitializeStakingPool(data));
            }
            1 => Ok(StakeInstruction::LaunchStaking),
            2 => Ok(StakeInstruction::CreateStakingAccount),
            3 => {
                let data = StakeTokensData::try_from_slice(instruction_data)?;
                Ok(StakeInstruction::StakeTokens(data))
            }
            4 => {
                let data = UnstakeTokensData::try_from_slice(instruction_data)?;
                Ok(StakeInstruction::UnstakeTokens(data))
            }
            5 => Ok(StakeInstruction::GetRewards),
            6 => Ok(StakeInstruction::CloseStakeAccount),

            _ => Err(StakeError::InvalidInstruction.into()),
        }
    }
}

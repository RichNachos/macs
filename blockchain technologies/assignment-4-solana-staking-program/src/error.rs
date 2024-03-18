use solana_program::program_error::ProgramError;
use thiserror::Error;

#[derive(Error, Debug, Copy, Clone)]
pub enum StakeError {
    #[error("Invalid Instruction")]
    InvalidInstruction,
    #[error("Wrong Initializer")]
    WrongInitializer,
    #[error("Wrong Pool State")]
    WrongPoolState,
    #[error("Staking Pool Already Launched")]
    PoolAlreadyLaunched,
    #[error("Staking Pool Already Launched")]
    PoolAlreadyInitialized,
    #[error("Wrong User State")]
    WrongUserState,
    #[error("User Has Not Been Initialized")]
    UserNotInitialized,
    #[error("User State Has Rewards And Staked Tokens")]
    UserStateNotEmpty,
    #[error("Pool Has Not Been Initialized")]
    PoolNotInitialized,
    #[error("Pool Has Not Been Launched")]
    PoolNotLaunched,
    #[error("Wrong Treasury")]
    WrongTreasury,
    #[error("Wrong Treasury Token")]
    WrongTreasuryToken,
    #[error("User State Has Not Enough Tokens")]
    NotEnoughTokens,
}

impl From<StakeError> for ProgramError {
    fn from(e: StakeError) -> Self {
        ProgramError::Custom(e as u32)
    }
}
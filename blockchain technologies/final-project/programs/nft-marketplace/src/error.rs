use anchor_lang::prelude::*;

#[error_code]
pub enum NftError {
    #[msg("Custom Error")]
    CustomError
}
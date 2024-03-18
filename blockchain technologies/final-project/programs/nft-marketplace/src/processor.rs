
use anchor_lang::prelude::*;
use mpl_token_metadata::state::*;
use anchor_spl::token::{self, Mint, Token, TokenAccount};
use crate::context::*;


#[program]
pub mod nft_marketplace {

    use super::*;

    pub fn initialize_marketplace(ctx: Context<InitializeMarketplace>) -> Result<()> {
        todo!("initialize marketplace");
    }

    pub fn list_nft(ctx: Context<ListNft>) -> Result<()> {

        // example of token transfer
        // let cpi_ctx = CpiContext::new(
        //     ctx.accounts.token_program.to_account_info(),
        //     token::Transfer {
        //         from: ctx.accounts.nft_associated_account.to_account_info(),
        //         to: ctx.accounts.nft_holder_account.to_account_info(),
        //         authority: ctx.accounts.signer.to_account_info(),
        //     },
        // );
        // token::transfer(cpi_ctx, 1)?;
        todo!("initialize marketplace");
    }

    pub fn list_nft_in_spl(ctx: Context<ListNftInSpl>) -> Result<()> {
        todo!("initialize marketplace");
    }

    pub fn update_price(ctx: Context<UpdatePrice>) -> Result<()> {
        todo!("initialize marketplace");
    }

    pub fn cancel_listing(ctx: Context<CancelListing>) -> Result<()> {
        todo!("initialize marketplace");
    }

    pub fn buy_nft(ctx: Context<BuyNft>) -> Result<()> {
        // examples of how to get creators and remaining acounts
        let creator_accounts = ctx.remaining_accounts;

        let metadata: Metadata = Metadata::from_account_info(&ctx.accounts.nft_metadata_account.to_account_info())?;
        let creators_array = metadata.data.creators.unwrap();

        todo!("initialize marketplace");
    }

    pub fn buy_nft_with_spl(ctx: Context<BuyNftInSpl>) -> Result<()> {
        let creator_accounts = ctx.remaining_accounts;
        todo!("initialize marketplace");
    }
}

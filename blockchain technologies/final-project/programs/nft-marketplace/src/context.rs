use anchor_lang::prelude::*;
use anchor_spl::{associated_token::AssociatedToken, token::{Mint, Token, TokenAccount}};

use crate::state::*;

#[derive(Accounts)]
#[instruction(fee: u64, _extra: u64)]
pub struct InitializeMarketplace<'info> {
    // depends
    pub system_program: Program<'info, System>,
    
    #[account(mut)]
    pub initializer_account: Signer<'info>,

    #[account(
        init,
        payer = initializer_account,
        space = 8 + 32 + 4 + 4 + 16 + 8*3 + 1,
        seeds  = [b"global_state_", initializer_account.key().as_ref(), &_extra.to_le_bytes()],
        bump
    )]
    pub global_state_account: Account<'info, GlobalState>,

}

#[derive(Accounts)]
pub struct ListNft<'info> {
    // depends
    pub system_program: Program<'info, System>,
    pub token_program: Program<'info, Token>,
    pub associated_token_program: Program<'info, AssociatedToken>,

    #[account(mut)]
    pub global_state_account: Account<'info, GlobalState>,
    #[account(mut)]
    pub initializer_account: Signer<'info>,
    
    #[account(
        init,
        payer = initializer_account,
        space = 32 + 32 + 32 + 32 + 8 + 8 + 8 + 1 + 32 + 32 + 1 + 8,
        seeds = [
            b"nft_listing_",
            global_state_account.key().as_ref(),
            nft_mint_account.key().as_ref(),
            initializer_account.key().as_ref()
        ],
        bump
    )]
    pub nft_listing_account: Account<'info, Listing>,

    #[account(
        constraint = nft_mint_account.supply == 1,
        mint::decimals = 0
    )]
    pub nft_mint_account: Account<'info, Mint>,
    
    #[account(
        init_if_needed,
        payer = initializer_account,
        associated_token::mint = nft_mint_account,
        associated_token::authority = nft_listing_account,
    )]
    pub nft_holder_account: Account<'info, TokenAccount>,
    
    #[account(
        mut,
        token::mint = nft_mint_account,
        token::authority = initializer_account,
    )]
    pub initializer_nft_account: Account<'info, TokenAccount>,
}

#[derive(Accounts)]
pub struct ListNftInSpl<'info> {
    // depends
    pub system_program: Program<'info, System>,
    pub token_program: Program<'info, Token>,
    pub associated_token_program: Program<'info, AssociatedToken>,

    #[account(mut)]
    pub global_state_account: Account<'info, GlobalState>,
    #[account(mut)]
    pub initializer_account: Signer<'info>,

    #[account(
        init,
        payer = initializer_account,
        space = 32 + 32 + 32 + 32 + 8 + 8 + 8 + 1 + 32 + 32 + 1 + 8, // Same as sol nft listing
        seeds = [
            b"nft_listing_",
            global_state_account.key().as_ref(),
            nft_mint_account.key().as_ref(),
            initializer_account.key().as_ref()
        ],
        bump
    )]
    pub nft_listing_account: Account<'info, Listing>,

    #[account(
        constraint = nft_mint_account.supply == 1,
        mint::decimals = 0
    )]
    pub nft_mint_account: Account<'info, Mint>,
    
    #[account(
        init_if_needed,
        payer = initializer_account,
        associated_token::mint = nft_mint_account,
        associated_token::authority = nft_listing_account
    )]
    pub nft_holder_account: Account<'info, TokenAccount>,

    #[account(
        mut,
        token::mint = nft_mint_account,
        token::authority = initializer_account
    )]
    pub initializer_nft_account: Account<'info, TokenAccount>,

    #[account()]
    pub trade_spl_token_mint_account: Account<'info, Mint>,

    #[account(
        mut,
        token::mint = trade_spl_token_mint_account,
        token::authority = initializer_account
    )]
    pub trade_spl_token_seller_account: Account<'info, TokenAccount>,


    // Where we send fees
    #[account(
        init_if_needed,
        payer = initializer_account,
        associated_token::mint = trade_spl_token_mint_account,
        associated_token::authority = global_state_account
    )]
    pub marketplace_fee_account: Account<'info, TokenAccount>

}

#[derive(Accounts)]
pub struct UpdatePrice<'info> {
    #[account()]
    pub initializer_account: Signer<'info>,
    
    #[account(
        mut,
        constraint = nft_listing_account.initializer.key() == initializer_account.key()
    )]
    pub nft_listing_account: Account<'info, Listing>
}

#[derive(Accounts)]
pub struct CancelListing<'info> {
    // depends
    pub system_program: Program<'info, System>,
    pub token_program: Program<'info, Token>,
    pub associated_token_program: Program<'info, AssociatedToken>,
    
    #[account(mut)]
    pub initializer_account: Signer<'info>,
    #[account(mut)]
    pub global_state_account: Account<'info, GlobalState>,
    
    #[account(
        mut,
        close = initializer_account,
        seeds = [
            b"nft_listing_",
            global_state_account.key().as_ref(),
            nft_mint_account.key().as_ref(),
            initializer_account.key().as_ref()
        ],
        bump
    )]
    pub nft_listing_account: Account<'info, Listing>,
    
    #[account()]
    pub nft_mint_account: Account<'info, Mint>,
    
    #[account(
        mut,
        constraint = nft_listing_account.nft_holder_address == nft_holder_account.key(),
        associated_token::mint = nft_mint_account,
        associated_token::authority = nft_listing_account
    )]
    pub nft_holder_account: Account<'info, TokenAccount>,
    
    #[account(
        mut,
        token::mint = nft_mint_account,
        token::authority = initializer_account
    )]
    pub initializer_nft_account: Account<'info, TokenAccount>,
}


#[derive(Accounts)]
pub struct BuyNft<'info> {
    // depends
    pub system_program: Program<'info, System>,
    pub token_program: Program<'info, Token>,
    pub associated_token_program: Program<'info, AssociatedToken>,

    #[account(mut)]
    pub global_state_account: Account<'info, GlobalState>,
    #[account(mut)]
    pub initializer_account: Signer<'info>, // This is buyers account

    #[account(
        mut,
        constraint = nft_listing_account.is_spl_listing == false,
        constraint = global_state_account.key() == nft_listing_account.global_state_address,
        close = nft_initializer_account,
        seeds = [
            b"nft_listing_",
            global_state_account.key().as_ref(),
            nft_mint_account.key().as_ref(),
            global_state_account.initializer.as_ref(),
        ],
        bump
    )]
    pub nft_listing_account: Account<'info, Listing>,

    #[account(
        mut,
        constraint = nft_listing_account.initializer == nft_initializer_account.key()
    )]
    pub nft_initializer_account: SystemAccount<'info>, // this is the sellers account
    #[account(
        constraint = nft_listing_account.nft_mint_address == nft_mint_account.key()
    )]
    pub nft_mint_account: Account<'info, Mint>,
    #[account(
        mut,
        constraint = nft_listing_account.nft_holder_address == nft_holder_account.key(),
        associated_token::mint = nft_mint_account,
        associated_token::authority = nft_listing_account
    )]
    pub nft_holder_account: Account<'info, TokenAccount>,
    #[account(
        mut,
        token::mint = nft_mint_account,
        token::authority = initializer_account,
    )]
    pub initializer_nft_account: Account<'info, TokenAccount>,
    #[account(mut)]
    /// CHECK: checking in instruction // ? maybe needed for compilator to not throw warning?
    pub nft_metadata_account: AccountInfo<'info>,
}

#[derive(Accounts)]
pub struct BuyNftInSpl<'info> {
    pub system_program: Program<'info, System>,
    pub token_program: Program<'info, Token>,
    pub associated_token_program: Program<'info, AssociatedToken>,

    #[account(mut)]
    pub global_state_account: Account<'info, GlobalState>,
    #[account(mut)]
    pub initializer_account: Signer<'info>, // this is the buyers account

    #[account(
        mut,
        constraint = nft_listing_account.is_spl_listing == true,
        constraint = global_state_account.key() == nft_listing_account.global_state_address,
        close = nft_initializer_account,
        seeds = [
            b"nft_listing_",
            global_state_account.key().as_ref(),
            nft_mint_account.key().as_ref(),
            global_state_account.initializer.as_ref()
        ],
        bump
    )]
    pub nft_listing_account: Account<'info, Listing>,

    #[account(
        mut,
        constraint = nft_listing_account.initializer == nft_initializer_account.key()
    )]
    pub nft_initializer_account: SystemAccount<'info>,  // this is the sellers account

    #[account(
        constraint = nft_listing_account.nft_mint_address == nft_mint_account.key()
    )]
    pub nft_mint_account: Account<'info, Mint>,

    #[account(
        mut,
        constraint = nft_listing_account.nft_holder_address == nft_holder_account.key(),
        associated_token::mint = nft_mint_account,
        associated_token::authority = nft_listing_account
    )]
    pub nft_holder_account: Account<'info, TokenAccount>,

    #[account(
        mut,
        token::mint = nft_mint_account,
        token::authority = initializer_account
    )]
    pub initializer_nft_account: Account<'info, TokenAccount>,

    #[account(
        mut,
        token::mint = trade_spl_token_mint_account,
        token::authority = initializer_account
    )]
    pub initializer_trade_token_account: Account<'info, TokenAccount>,

    #[account(
        constraint = nft_listing_account.trade_spl_token_mint_address == trade_spl_token_mint_account.key()
    )]
    pub trade_spl_token_mint_account: Account<'info, Mint>,

    #[account(
        mut,
        constraint = nft_listing_account.trade_spl_token_seller_account_address == trade_spl_token_seller_account.key(),
        token::mint = trade_spl_token_mint_account
    )]
    pub trade_spl_token_seller_account: Account<'info, TokenAccount>,

    #[account(
        mut,
        associated_token::mint = trade_spl_token_mint_account,
        associated_token::authority = global_state_account
    )]
    pub marketplace_fee_account: Account<'info, TokenAccount>,

    #[account(mut)]
    /// CHECK: checking in instruction // ? maybe needed for compilator to not throw warning?
    pub nft_metadata_account: AccountInfo<'info>,
}


pub mod error;
pub mod state;
pub mod context;

use anchor_lang::prelude::*;
use mpl_token_metadata::state::*;
use anchor_spl::token::{self};
use self::context::*;

declare_id!("FxHLWgvCiF8PxSJ94E1kckuA6RkFYa3N1MhBTJaQEjn4");



#[program]
pub mod nft_marketplace {

    use anchor_lang::system_program;
    use anchor_spl::associated_token::{self, get_associated_token_address};

    use super::*;

    pub fn initialize_marketplace(ctx: Context<InitializeMarketplace>, fee: u64, _extra: u64) -> Result<()> {
        let global_state_account = &mut ctx.accounts.global_state_account;
        
        global_state_account.initializer = ctx.accounts.initializer_account.key();
        global_state_account.total_listed_count_sol = 0;
        global_state_account.total_listed_count_spl = 0;
        global_state_account.total_volume_all_time_sol = 0;
        global_state_account.all_time_sale_count_spl = 0;
        global_state_account.all_time_sale_count_sol = 0;
        global_state_account.marketplace_fee_percentage = fee;
        global_state_account.bump = ctx.bumps.global_state_account;

        Ok(())
    }

    pub fn list_nft(ctx: Context<ListNft>, price: u64) -> Result<()> {
        let time = Clock::get()?.unix_timestamp;
        
        // Get sol nft listing
        let nft_listing = &mut ctx.accounts.nft_listing_account;
        
        // Initialize and set nft listing values
        nft_listing.global_state_address = ctx.accounts.global_state_account.key();
        nft_listing.initializer = ctx.accounts.initializer_account.key();
        nft_listing.nft_mint_address = ctx.accounts.nft_mint_account.key();
        nft_listing.nft_holder_address = ctx.accounts.nft_holder_account.key();
        nft_listing.price = price;
        nft_listing.is_spl_listing = false;
        nft_listing.bump = ctx.bumps.nft_listing_account;
        nft_listing.creation_time = time;
        nft_listing.updated_at = time;

        // Get global state of this nft listing's marketplace and increase total listed
        let global_state = &mut ctx.accounts.global_state_account;
        global_state.total_listed_count_sol += 1;

        
        // example of token transfer
        let cpi_ctx = CpiContext::new(
            ctx.accounts.token_program.to_account_info(),
            token::Transfer {
                from: ctx.accounts.initializer_nft_account.to_account_info(),
                to: ctx.accounts.nft_holder_account.to_account_info(),
                authority: ctx.accounts.initializer_account.to_account_info(),
            },
        );
        token::transfer(cpi_ctx, 1)?;
        // todo!("initialize marketplace");
        Ok(())
    }
            
    pub fn list_nft_in_spl(ctx: Context<ListNftInSpl>, price: u64) -> Result<()> {
        let time = Clock::get()?.unix_timestamp;
        
        // Get spl nft listing
        let nft_listing = &mut ctx.accounts.nft_listing_account;
        
        // Initialize and set nft listing values
        nft_listing.global_state_address = ctx.accounts.global_state_account.key();
        nft_listing.initializer = ctx.accounts.initializer_account.key();
        nft_listing.nft_mint_address = ctx.accounts.nft_mint_account.key();
        nft_listing.nft_holder_address = ctx.accounts.nft_holder_account.key();
        nft_listing.price = price;
        nft_listing.is_spl_listing = true;
        nft_listing.bump = ctx.bumps.nft_listing_account;
        nft_listing.creation_time = time;
        nft_listing.updated_at = time;

        // Get global state of this nft listing's marketplace and increase total listed
        let global_state = &mut ctx.accounts.global_state_account;
        global_state.total_listed_count_spl += 1;

        // example of token transfer
        let cpi_ctx = CpiContext::new(
            ctx.accounts.token_program.to_account_info(),
            token::Transfer {
                from: ctx.accounts.initializer_nft_account.to_account_info(),
                to: ctx.accounts.nft_holder_account.to_account_info(),
                authority: ctx.accounts.initializer_account.to_account_info(),
            },
        );
        token::transfer(cpi_ctx, 1)?;
        // todo!("initialize marketplace");
        Ok(())
    }
    
    pub fn update_price(ctx: Context<UpdatePrice>, new_price: u64) -> Result<()> {
        let nft_listing = &mut ctx.accounts.nft_listing_account;
        nft_listing.price = new_price;
        nft_listing.updated_at = Clock::get()?.unix_timestamp;
        Ok(())
    }
    
    pub fn cancel_listing(ctx: Context<CancelListing>) -> Result<()> {

        // Had to do it this way because rust compiler did not like borrowing or something?
        let global_state_account_address = ctx.accounts.global_state_account.key();
        let nft_mint_account_address = ctx.accounts.nft_mint_account.key();
        let initializer_account_address = ctx.accounts.initializer_account.key();
        let seed = [
            b"nft_listing_",
            global_state_account_address.as_ref(),
            nft_mint_account_address.as_ref(),
            initializer_account_address.as_ref(),
            &[ctx.accounts.nft_listing_account.bump]
            ];
        let seed = [seed.as_slice()];
        
        let cpi_ctx = CpiContext::new_with_signer(
            ctx.accounts.token_program.to_account_info(),
            token::Transfer {
                from: ctx.accounts.nft_holder_account.to_account_info(),
                to: ctx.accounts.initializer_account.to_account_info(),
                authority: ctx.accounts.nft_listing_account.to_account_info(),
            },
            &seed
        );

        // Transfer nft to owner and update listing count
        token::transfer(cpi_ctx, 1)?;
        let global_state = &mut ctx.accounts.global_state_account;
        global_state.total_listed_count_sol -= 1;
        Ok(())
        // todo!("initialize marketplace");
    }

    pub fn buy_nft(ctx: Context<BuyNft>) -> Result<()> {
        let nft_listing = &mut ctx.accounts.nft_listing_account;
        // FEE TRANSFER
        let fee_percent = ctx.accounts.global_state_account.marketplace_fee_percentage / 100;
        let fee = nft_listing.price * fee_percent;
        system_program::transfer(
            CpiContext::new(
                ctx.accounts.system_program.to_account_info(),
                system_program::Transfer {
                    from: ctx.accounts.initializer_account.to_account_info(),
                    to: ctx.accounts.global_state_account.to_account_info()
                },
            ),
            fee
        )?;
        
        // LAMPORTS TRANSFER TO CREATORS
        let creator_accounts = ctx.remaining_accounts;
        let data = Metadata::from_account_info(&ctx.accounts.nft_metadata_account.to_account_info())?.data;
        let creator_fee = (nft_listing.price * data.seller_fee_basis_points as u64) / 10000; // normalize
        let creators = data.creators.unwrap();
        
        for creator in creators.iter() {
            let creator_account = creator_accounts.iter().find(|account| creator.address == account.key());
            if let Some(creator_account) = creator_account {
                let lamports = (creator_fee * creator.share as u64) / 100;
                let cpi_ctx = CpiContext::new(
                    ctx.accounts.system_program.to_account_info(),
                    system_program::Transfer {
                        from: ctx.accounts.initializer_account.to_account_info(),
                        to: creator_account.to_account_info()
                    }
                );
                system_program::transfer(
                    cpi_ctx,
                    lamports
                )?;
            }
        }
        
        // LAMPORTS TRANSFER TO SELLER
        let amount = nft_listing.price - fee - creator_fee;
        system_program::transfer(
            CpiContext::new(
                ctx.accounts.system_program.to_account_info(),
                system_program::Transfer {
                    from: ctx.accounts.initializer_account.to_account_info(),
                    to: ctx.accounts.nft_initializer_account.to_account_info()
                },
            ),
            amount
        )?;
        
        // TRANSFER NFT TO BUYER
        let global_state = &mut ctx.accounts.global_state_account;
        let global_state_account_address = global_state.key();
        let nft_mint_account_address = ctx.accounts.nft_mint_account.key();
        let initializer_account_address = ctx.accounts.initializer_account.key();
        let seed = [
            b"nft_listing_",
            global_state_account_address.as_ref(),
            nft_mint_account_address.as_ref(),
            initializer_account_address.as_ref(),
            &[ctx.accounts.nft_listing_account.bump]
            ];
        let seed = [seed.as_slice()];

        let cpi_ctx = CpiContext::new_with_signer(
            ctx.accounts.token_program.to_account_info(),
            token::Transfer {
                from: ctx.accounts.nft_holder_account.to_account_info(),
                to: ctx.accounts.initializer_nft_account.to_account_info(),
                authority: ctx.accounts.nft_listing_account.to_account_info(),
            },
            &seed
        );
        // Transfer nft to buyer and update listing count
        token::transfer(cpi_ctx, 1)?;
        global_state.total_listed_count_sol -= 1;
        global_state.all_time_sale_count_sol += 1;
        global_state.total_volume_all_time_sol += (fee + creator_fee) as u128;
        Ok(())
        // todo!("initialize marketplace");
    }

    pub fn buy_nft_with_spl(ctx: Context<BuyNftInSpl>) -> Result<()> {
        let nft_listing = &mut ctx.accounts.nft_listing_account;
        
        // SPL FEE TRANSFER
        let fee_percent = ctx.accounts.global_state_account.marketplace_fee_percentage / 100;
        let fee = nft_listing.price * fee_percent;
        token::transfer(
            CpiContext::new(
                ctx.accounts.token_program.to_account_info(),
                token::Transfer {
                    from: ctx.accounts.initializer_trade_token_account.to_account_info(),
                    to: ctx.accounts.marketplace_fee_account.to_account_info(),
                    authority: ctx.accounts.initializer_account.to_account_info()
                },
            ),
            fee
        )?;

        // SPL TRANSFER TO CREATORS
        let creator_accounts = ctx.remaining_accounts;
        let data = Metadata::from_account_info(&ctx.accounts.nft_metadata_account.to_account_info())?.data;
        let creator_fee = (nft_listing.price * data.seller_fee_basis_points as u64) / 10000; // normalize
        let creators = data.creators.unwrap();

        for creator in creators.iter() {
            let creator_account = creator_accounts.iter().find(|a| creator.address == a.key());

            // Check if creator exists
            if let Some(creator_account) = creator_account {
                // Ceheck if associated token account exists
                let address = get_associated_token_address(
                    &creator_account.key(),
                    &ctx.accounts.trade_spl_token_mint_account.key()
                );
                let creator_associated_token_account = creator_accounts.iter().find(|a| address == a.key()).unwrap();
                if creator_associated_token_account.get_lamports() == 0 {
                    
                    let cpi_ctx = CpiContext::new(
                        ctx.accounts.associated_token_program.to_account_info(),
                        associated_token::Create {
                            payer: ctx.accounts.initializer_account.to_account_info(),
                            associated_token: creator_associated_token_account.clone(),
                            authority: creator_account.clone(),
                            mint: ctx.accounts.trade_spl_token_mint_account.to_account_info(),
                            system_program: ctx.accounts.system_program.to_account_info(),
                            token_program: ctx.accounts.token_program.to_account_info()
                        }
                    );
                    associated_token::create(cpi_ctx)?;
                }


                let spl = (creator_fee * creator.share as u64) / 100;
                let cpi_ctx = CpiContext::new(
                    ctx.accounts.system_program.to_account_info(),
                    token::Transfer {
                        from: ctx.accounts.initializer_trade_token_account.to_account_info(),
                        to: creator_associated_token_account.to_account_info(),
                        authority: ctx.accounts.initializer_account.to_account_info()
                    }
                );
                token::transfer(
                    cpi_ctx,
                    spl
                )?;
            }
        }

        // SPL TRANSFER TO SELLER
        let amount = nft_listing.price - fee - creator_fee;
        token::transfer(
            CpiContext::new(
                ctx.accounts.system_program.to_account_info(),
                token::Transfer {
                    from: ctx.accounts.initializer_trade_token_account.to_account_info(),
                    to: ctx.accounts.trade_spl_token_seller_account.to_account_info(),
                    authority: ctx.accounts.initializer_account.to_account_info()
                },
            ),
            amount
        )?;

        // TRANSFER NFT TO BUYER
        let global_state_account_address = ctx.accounts.global_state_account.key();
        let nft_mint_account_address = ctx.accounts.nft_mint_account.key();
        let initializer_account_address = ctx.accounts.initializer_account.key();

        let seed = [
            b"nft_listing_",
            global_state_account_address.as_ref(),
            nft_mint_account_address.as_ref(),
            initializer_account_address.as_ref(),
            &[ctx.accounts.nft_listing_account.bump]
            ];
            let seed = [seed.as_slice()];
            
            let cpi_ctx = CpiContext::new_with_signer(
                ctx.accounts.token_program.to_account_info(),
                token::Transfer {
                    from: ctx.accounts.nft_holder_account.to_account_info(),
                    to: ctx.accounts.initializer_nft_account.to_account_info(),
                    authority: ctx.accounts.nft_listing_account.to_account_info(),
                },
                &seed
            );

            // Transfer nft to buyer and update listing count
            let global_state = &mut ctx.accounts.global_state_account;
            token::transfer(cpi_ctx, 1)?;
            global_state.total_listed_count_spl -= 1;
            global_state.all_time_sale_count_spl += 1;
            Ok(())
        }
    }
    
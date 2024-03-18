use anchor_lang::prelude::*;
// use anchor_spl::token::{self, Mint, Token, TokenAccount};

#[account]
#[derive(Default)]
pub struct GlobalState {
	pub initializer: Pubkey, // 32
	pub total_listed_count_sol: u32, // 4
	pub total_listed_count_spl: u32, // 4

	pub total_volume_all_time_sol: u128, // 16

	pub all_time_sale_count_spl: u64, // 8
	pub all_time_sale_count_sol: u64, // 8
	pub marketplace_fee_percentage: u64, // 8

    pub bump: u8 // 1
    // 8
}

#[account]
#[derive(Default)]
pub struct Listing {
    // Marketplace instance global state address
    pub global_state_address: Pubkey, // 32

    // User who listed this nft
    pub initializer: Pubkey, // 32
    // NFT mint address
    pub nft_mint_address: Pubkey, // 32
    // Program PDA account address, who holds NFT now
    pub nft_holder_address: Pubkey, // 32
    // Price of this NFT.
    pub price: u64, // 8

    // listing creation time
    pub creation_time: i64, // 8
    pub updated_at: i64, // 8

    // if trade payment is in spl token currency
    pub is_spl_listing: bool, // 1
    // trade spl token address
    pub trade_spl_token_mint_address: Pubkey, // 32
    pub trade_spl_token_seller_account_address: Pubkey, // 32

    pub bump: u8 // 1
    // 8
} // 32 + 32 + 32 + 32 + 8 + 8 + 8 + 1 + 32 + 32 + 1 + 8
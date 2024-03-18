import { Nft } from "@metaplex-foundation/js";
import { PublicKey } from "@solana/web3.js"
import BN from "bn.js"

export type GlobalState = {
    initializer: PublicKey,
    totalListedCountSol: BN;
    totalListedCountSpl: BN;

    totalVolumeAllTimeSol: BN;

    allTimeSaleCountSpl: BN;
    allTImeSaleCountSol: BN;
}

export type Listing = {
    globalStateAddress: PublicKey,

    // User who listed this nft
    initializer: PublicKey,
    // NFT mint address
    nft: Nft,
    // Program PDA account address, who holds NFT now
    nftHolderAddress: PublicKey,
    // Price of this NFT.
    price: BN,

    // listing creation time
    creationTime: BN,
    updatedAt: BN,

    // if trade payment is in spl token currency
    isSplListing: boolean,
    // trade spl token address
    tradeSplTokenMintAddress: PublicKey,
}
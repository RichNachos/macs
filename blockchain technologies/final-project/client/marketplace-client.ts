import { AnchorProvider } from "@coral-xyz/anchor";
import { Connection, Keypair, clusterApiUrl } from "@solana/web3.js";
import NodeWallet from "@coral-xyz/anchor/dist/cjs/nodewallet";
import { GlobalState, Listing } from "./types";
import { Nft } from "@metaplex-foundation/js";

// define your private key here
const keypair = Keypair.fromSecretKey(Uint8Array.from([]));
export class MarketplaceClient{
    connection: Connection;
    provider: AnchorProvider;

    /**
     *
     */
    constructor() {
        this.connection = new Connection(clusterApiUrl("devnet"));
        this.provider = new AnchorProvider(this.connection, new NodeWallet(keypair), {});

    }

    public async initializeMarketplace() {

    }

    public async listNft() {

    }

    public async listNftInSpl() {

    }

    public async updatePrice() {

    }

    public async cancelListing() {

    }

    public async buyNft() {

    }

    public async buyNftWithSpl() {

    }

    public async getMarketplaceMetadata(): Promise<GlobalState> {
        throw "unimplemented!"
    }

    public async getUserListings(address: string): Promise<Listing[]> {
        throw "unimplemented"
    }

    public async getAllListings(): Promise<Listing[]> {
        throw "unimplemented"
    }
    
    public async getUserNfts(address: string): Promise<Nft> {
        throw "unimplemented"
    }
}
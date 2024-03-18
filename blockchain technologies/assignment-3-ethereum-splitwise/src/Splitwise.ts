import {MoneyRequest} from "./models";
import {Contract, ContractTransactionReceipt, ContractTransactionResponse, Interface, JsonRpcProvider, Provider, Result, Wallet, ethers} from "ethers";

export class Splitwise {

    private wallet: Wallet;
    private abi: Array<any>;
    private splitwise: Contract;
    private iface: Interface
    public gasLimit: bigint = 3000000n; // 3mil gas limit

    constructor(private contractAddress: string, private signerKey: string) {

        const provider = new JsonRpcProvider('https://rpc.notadegen.com/eth/sepolia');
        this.wallet = new Wallet(signerKey,provider);
        
        this.abi = [
            {
                "inputs": [
                    {
                        "internalType": "uint256",
                        "name": "requestId",
                        "type": "uint256"
                    }
                ],
                "name": "cancelMoneyRequest",
                "outputs": [],
                "stateMutability": "nonpayable",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getAllCreditors",
                "outputs": [
                    {
                        "internalType": "address[]",
                        "name": "",
                        "type": "address[]"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getAllDebtors",
                "outputs": [
                    {
                        "internalType": "address[]",
                        "name": "",
                        "type": "address[]"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "address",
                        "name": "from",
                        "type": "address"
                    }
                ],
                "name": "getAmountOwedTo",
                "outputs": [
                    {
                        "internalType": "uint256",
                        "name": "",
                        "type": "uint256"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "address",
                        "name": "to",
                        "type": "address"
                    }
                ],
                "name": "getAmountRequestedFrom",
                "outputs": [
                    {
                        "internalType": "uint256",
                        "name": "",
                        "type": "uint256"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getParticipatingAddresses",
                "outputs": [
                    {
                        "internalType": "address[]",
                        "name": "",
                        "type": "address[]"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getReceivedRequests",
                "outputs": [
                    {
                        "components": [
                            {
                                "internalType": "uint256",
                                "name": "requestId",
                                "type": "uint256"
                            },
                            {
                                "internalType": "address",
                                "name": "from",
                                "type": "address"
                            },
                            {
                                "internalType": "address",
                                "name": "to",
                                "type": "address"
                            },
                            {
                                "internalType": "uint256",
                                "name": "amount",
                                "type": "uint256"
                            }
                        ],
                        "internalType": "struct Splitwise.MoneyRequest[]",
                        "name": "",
                        "type": "tuple[]"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getSentRequests",
                "outputs": [
                    {
                        "components": [
                            {
                                "internalType": "uint256",
                                "name": "requestId",
                                "type": "uint256"
                            },
                            {
                                "internalType": "address",
                                "name": "from",
                                "type": "address"
                            },
                            {
                                "internalType": "address",
                                "name": "to",
                                "type": "address"
                            },
                            {
                                "internalType": "uint256",
                                "name": "amount",
                                "type": "uint256"
                            }
                        ],
                        "internalType": "struct Splitwise.MoneyRequest[]",
                        "name": "",
                        "type": "tuple[]"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getTotalAmountOwed",
                "outputs": [
                    {
                        "internalType": "uint256",
                        "name": "",
                        "type": "uint256"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "getTotalAmountRequested",
                "outputs": [
                    {
                        "internalType": "uint256",
                        "name": "",
                        "type": "uint256"
                    }
                ],
                "stateMutability": "view",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "uint256",
                        "name": "balance",
                        "type": "uint256"
                    },
                    {
                        "internalType": "address",
                        "name": "addr",
                        "type": "address"
                    },
                    {
                        "internalType": "uint256",
                        "name": "amount",
                        "type": "uint256"
                    }
                ],
                "name": "pay",
                "outputs": [],
                "stateMutability": "payable",
                "type": "function"
            },
            {
                "inputs": [],
                "name": "payForAllTheRequests",
                "outputs": [],
                "stateMutability": "payable",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "uint256",
                        "name": "requestId",
                        "type": "uint256"
                    }
                ],
                "name": "payForRequestedAmount",
                "outputs": [],
                "stateMutability": "payable",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "address",
                        "name": "from",
                        "type": "address"
                    }
                ],
                "name": "payToAddress",
                "outputs": [],
                "stateMutability": "payable",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "uint256",
                        "name": "requestId",
                        "type": "uint256"
                    }
                ],
                "name": "rejectMoneyRequest",
                "outputs": [],
                "stateMutability": "nonpayable",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "uint256",
                        "name": "amount",
                        "type": "uint256"
                    },
                    {
                        "internalType": "address[]",
                        "name": "addresses",
                        "type": "address[]"
                    }
                ],
                "name": "splitTheBill",
                "outputs": [],
                "stateMutability": "nonpayable",
                "type": "function"
            },
            {
                "inputs": [
                    {
                        "internalType": "address",
                        "name": "to",
                        "type": "address"
                    },
                    {
                        "internalType": "uint256",
                        "name": "amount",
                        "type": "uint256"
                    }
                ],
                "name": "submitMoneyRequest",
                "outputs": [],
                "stateMutability": "nonpayable",
                "type": "function"
            },
            {
                "stateMutability": "payable",
                "type": "receive"
            }
        ]

        this.splitwise = new Contract(contractAddress, this.abi, this.wallet);
        this.iface = new ethers.Interface(this.abi);
        

    }

    // commands

    /*
        this method submits a money request, which should be saved on a blockchain.
        returns hash of submitted transaction
     */
    public async submitMoneyRequest(to: string, amount: bigint) : Promise<string> {
        this.validateAddress(to);
        this.validateAmount(amount);

        this.validateDifferent(this.wallet.address, to);

        const result = await this.splitwise.submitMoneyRequest(to, amount) as ContractTransactionResponse;
        return result.hash;
    }

    /*
        this method is similar to `submitMoneyRequest` but works on multiple addresses and total
        amount if split among those addresses.
        error should be raised if an amount is not evenly divisible on all addresses.
     */
    public async splitTheBill(totalAmount: string, addresses: string[]): Promise<string> {
        addresses.forEach(address => {
            this.validateAddress(address);
        });
        this.validateAmount(BigInt(totalAmount));
        if (BigInt(totalAmount) % BigInt(addresses.length) !== 0n) {
            throw new Error("Amount not evenly divisible");
        }
        const to = addresses;
        const amount = BigInt(totalAmount)


        const result = await this.splitwise.splitTheBill(totalAmount, addresses) as ContractTransactionResponse;
        console.log(result);
        return result.hash;
    }

    /*
        this method rejects a specific money request, if the request if sent to the signer.
        Money request should be removed from a storage after rejection.
        method accepts requestId as a parameter, contract should be generating unique request ID
        for each request
     */
    public async rejectMoneyRequest(requestId: bigint) : Promise<string> {
        this.validateAmount(requestId);

        const result = await this.splitwise.rejectMoneyRequest(requestId) as ContractTransactionResponse;
        return result.hash;
    }

    /*
        this method is mean to revoke your sent money request, Revoked MoneyRequest should also
        be deleted
     */
    public async cancelMoneyRequest(requestId: bigint) : Promise<string> {
        this.validateAmount(requestId);

        const result = await this.splitwise.cancelMoneyRequest(requestId) as ContractTransactionResponse;
        return result.hash;
    }

    /*
        transfers amount of wei to the requesting party, paid requests should also be deleted from a list of
        incoming and outcoming requests. You probably need to declare corresponding solidity function as payable
     */
    public async payForRequestedAmount(requestId: bigint) : Promise<string> {
        this.validateAmount(requestId);
        
        const result = await this.splitwise.payForRequestedAmount(requestId, {value: await this.getUsableBalance()}) as ContractTransactionResponse;
        return result.hash;
    }

    /*
        this method pays for a money request by address. if several requests are sent from that address,
        the method should pay from all of them.
     */
    public async payToAddress(address: string): Promise<string> {
        this.validateAddress(address);
        
        const result = await this.splitwise.payToAddress(address, {value: await this.getUsableBalance()}) as ContractTransactionResponse;
        return result.hash;
    }


    /*
        This method should pay from all the incoming requests for the signer.
     */
    public async payForAllTheRequests() : Promise<string> {
        const result = await this.splitwise.payForAllTheRequests({value: await this.getUsableBalance()}) as ContractTransactionResponse;
        return result.hash;
    }

    // queries

    /*
        Fetch all the addresses which received or sent money requests througout the history of a smart contract
     */
    public async getParticipatingAddresses(): Promise<string[]> {
        return await this.splitwise.getParticipatingAddresses();
    }

    /*
        fetch requests sent by the signer
     */
    public async getSentRequests(): Promise<MoneyRequest[]>{
        const result = await this.splitwise.getSentRequests();
        return this.parseRequests(result);
    }

    /*
        fetch requests sent to the signer by other users.
     */
    public async getReceivedRequests(): Promise<MoneyRequest[]> {
        const result = await this.splitwise.getReceivedRequests();
        return this.parseRequests(result);
    }


    /*
        get all the addresses who have sent money requests to the signer. Payed or Rejected requests
        should not be returned
     */
    public async getAllCreditors(): Promise<string[]> {
        return await this.splitwise.getAllCreditors();
    }

    /*
        fetch all addresses to whom signer have sent the money requests. This method should return only active
        requests as well.
     */
    public async getAllDebtors(): Promise<string[]> {
        return await this.splitwise.getAllDebtors();
    }

    /*
        method fetches total amount owed by combining all the incomming active requests' amounts.
     */
    public async getTotalAmountOwed() : Promise<bigint> {
        return await this.splitwise.getTotalAmountOwed();
    }

    /*
        Fetches total amount requested by the signer from other users
     */
    public async getTotalAmountRequested() : Promise<bigint> {
        return await this.splitwise.getTotalAmountRequested();
    }

    /*
        gets total amount owed to specific address by signer
     */
    public async getAmountOwedTo(address: string): Promise<bigint> {
        this.validateAddress(address);
        return await this.splitwise.getAmountOwedTo(address);
    }

    /*
        gets total amount which signer requested from specific address.
     */
    public async getAmountRequestedFrom(address: string): Promise<bigint> {
        this.validateAddress(address);
        return await this.splitwise.getAmountRequestedFrom(address);
    }

    private validateAddress(address: string) {
        const regex = /^(0x)?[0-9a-fA-F]{40}$/;
        if (!regex.test(address)) {
            throw new Error("Invalid address");
        }
    }

    private validateAmount(amount: bigint) {
        if (amount <= 0) {
            throw new Error("Invalid amount");
        }
    }

    private validateDifferent(address1: string, address2: string) {
        if (address1 == address2) {
            throw new Error("Same addresses supplied");
        }
    }

    private async getUsableBalance(): Promise<bigint> {
        return await this.wallet.provider.getBalance(this.wallet.address) - this.gasLimit * ((await this.wallet.provider.getFeeData()).gasPrice)
    }

    private parseRequests(data): MoneyRequest[] {
        const requests: MoneyRequest[] = [];

        for (var i = 0; i < data.length; i++) {
            const req = this.parseRequest(data[i]);
            requests.push(req);
        }

        return requests;
    }

    private parseRequest(data): MoneyRequest {
        const req: MoneyRequest = {
            requestId: data.requestId,
            from: data.from,
            to: data.to,
            amount: data.amount
        }
        return req;
    }
}
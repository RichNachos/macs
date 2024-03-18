import {Splitwise} from "./Splitwise";

const contract_addr = "0x305d620e7Eaedec6571046b452026a0676C7a148";

const acc1 = "0x368bEf30B17eAFA20587bd2a2a8D0703876B0F43"
const acc2 = "0x6E728D793f42f5Aa692D96441dC32878F74b9FeA"
const acc3 = "0xbd80aa8B6E774e4F3172361C9f8A2F5A7B202330"
const acc1_private = "6afcc23947e1bd4ea4c1e5f2f640acf70fc45d291a1489e1fd74b0563ae68f69";
const acc2_private = "51257b4b6c288a9ff73a25cdf419cb96a61e2509821e3715bf2edf8c6d370142";
const acc3_private = "8f7ad38929c8d25d39b9d1ada3d42ab11e6dc8cb96484b5ce71554d7daf2b90b";

async function main() {
    // Init splitwise class
    const splitwise = new Splitwise(
        contract_addr,
        acc1_private
    );
    var result = null;

    // submitMoneyRequest
    // result = await splitwise.submitMoneyRequest(acc1, BigInt(200000));
    // console.log(result);

    // splitTheBill
    // result = await splitwise.splitTheBill('1000', ['0x95F64e1B611CB804194997A5B2b4dcE957CF800B', '0x6E728D793f42f5Aa692D96441dC32878F74b9FeA']);
    // console.log(result);

    // getParticipatingAddresses
    // result = await splitwise.getParticipatingAddresses();
    // console.log(result);

    // getSentRequests
    // result = await splitwise.getSentRequests();
    // console.log(result);

    // getReceivedRequests
    // result = await splitwise.getReceivedRequests();
    // console.log(result);

    // getAllCreditors
    // result = await splitwise.getAllCreditors();
    // console.log(result);

    // getAllDebtors
    // result = await splitwise.getAllDebtors();
    // console.log(result);

    // getTotalAmountOwed
    // result = await splitwise.getTotalAmountOwed();
    // console.log(result);

    // getTotalAmountRequested
    // result = await splitwise.getTotalAmountRequested();
    // console.log(result);

    // getAmountOwedTo
    // result = await splitwise.getAmountOwedTo(acc2);
    // console.log(result);

    // getAmountRequestedFrom
    // result = await splitwise.getAmountRequestedFrom(acc2)
    // console.log(result);

    // payForRequestedAmount
    // result = await splitwise.payForRequestedAmount(1n);
    // console.log(result);
}   

main();
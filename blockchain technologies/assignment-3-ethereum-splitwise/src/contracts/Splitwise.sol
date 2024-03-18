// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Splitwise {
    struct MoneyRequest {
        uint requestId;
        address from;
        address to;
        uint256 amount;
    }

    address[] users;
    mapping(address => MoneyRequest[]) requests;
    uint currentRequest = 1;


    function submitMoneyRequest(address to, uint256 amount) external {
        require(msg.sender != to);

        requests[to].push(MoneyRequest({
            requestId: currentRequest,
            from: msg.sender,
            to: to,
            amount: amount
        }));
        currentRequest++;

        addUser(msg.sender);
        addUser(to);
    }

    function splitTheBill(uint256 amount, address[] calldata addresses) external {
        require(amount % addresses.length == 0);

        uint256 single = amount / addresses.length;
        for (uint i = 0; i < addresses.length; i++) {
            this.submitMoneyRequest(addresses[i], single);
        }
    }

    function rejectMoneyRequest(uint requestId) external {
        MoneyRequest storage request = getMoneyRequest(requestId);
        if (request.requestId == requestId && request.to == msg.sender) {
            request.requestId = 0;
        }
    }

    function cancelMoneyRequest(uint requestId) external {
        MoneyRequest storage request = getMoneyRequest(requestId);
        if (request.requestId == requestId && request.from == msg.sender) {
            request.requestId = 0;
        }
    }

    function payForRequestedAmount(uint256 requestId) external payable {
        uint256 balance = msg.value;

        MoneyRequest storage request = getMoneyRequest(requestId);
        if (request.requestId == requestId && request.to == msg.sender) {
            pay(balance, request.from, request.amount); // send wei to 'from' address
            balance -= request.amount;
            
            request.requestId = 0;
        }

        // return excess wei to sender address
        if (balance - request.amount > 0) {
            pay(balance, msg.sender, balance);
        }
    }

    function payToAddress(address from) external payable {
        uint256 balance = msg.value;

        for (uint j = 0; j < requests[msg.sender].length; j++) {
            MoneyRequest storage request = requests[msg.sender][j];
            if (request.requestId != 0 && request.from == from) {
                pay(balance, request.from, request.amount);  // send wei to 'from' address
                balance -= request.amount;
                request.requestId = 0;
            }
        }

        // return excess wei to sender address
        if (balance > 0) {
            pay(balance, msg.sender, balance);
        }
    }

    function payForAllTheRequests() external payable {
        uint256 balance = msg.value;

        for (uint j = 0; j < requests[msg.sender].length; j++) {
            MoneyRequest storage request = requests[msg.sender][j];
            if (request.requestId != 0) {
                pay(balance, request.from, request.amount);
                balance -= request.amount;
                request.requestId = 0;
            }
        }
        
        // return excess wei to sender address
        if (balance > 0) {
            pay(balance, msg.sender, balance);
        }
    }







    //------------------ QUERY FUNCTIONS ---------------------



    function getParticipatingAddresses() external view returns (address[] memory) {
        return users;
    }

    function getSentRequests() public view returns (MoneyRequest[] memory) {
        uint length = 0;
        uint idx = 0;

        // Find total length of allSent array
        for (uint i = 0; i < users.length; i++) {
            if (users[i] == msg.sender) {
                continue;
            }

            for (uint j = 0; j < requests[users[i]].length; j++) {
                MoneyRequest memory curr = requests[users[i]][j];
                if (curr.from == msg.sender && curr.requestId != 0) {
                    length++;
                }
            }
        }

        MoneyRequest[] memory allSent = new MoneyRequest[](length);

        for (uint i = 0; i < users.length; i++) {
            if (users[i] == msg.sender) {
                continue;
            }

            for (uint j = 0; j < requests[users[i]].length; j++) {
                MoneyRequest memory curr = requests[users[i]][j];
                if (curr.from == msg.sender && curr.requestId != 0) {
                    allSent[idx] = curr;
                    idx++;
                }
            }
        }

        return allSent;
    }


    function getReceivedRequests() public view returns (MoneyRequest[] memory) {
        
        uint length = 0;
        for (uint j = 0; j < requests[msg.sender].length; j++) {
            MoneyRequest storage curr = requests[msg.sender][j];
            if (curr.requestId != 0) {
                length++;
            }
        }

        MoneyRequest[] memory myRequests = new MoneyRequest[](length);
        uint idx = 0;
        for (uint j = 0; j < requests[msg.sender].length; j++) {
            MoneyRequest storage curr = requests[msg.sender][j];
            if (curr.requestId != 0) {
                myRequests[idx] = curr;
                idx++;
            }
        }

        return myRequests;
    }


    function getAllCreditors() public view returns (address[] memory) {
        address[] memory creditors = new address[](users.length);
        uint idx = 0;

        for (uint i = 0; i < users.length; i++) {
            address from = users[i];

            for (uint j = 0; j < requests[msg.sender].length; j++) {
                MoneyRequest storage req = requests[msg.sender][j];
                if (req.requestId != 0 && req.from == from) {
                    creditors[idx] = from;
                    idx++;
                    break;
                }
            }
        }

        address[] memory newCreditors = new address[](idx);
        for (uint i = 0; i < idx; i++) {
            newCreditors[i] = creditors[i];
        }

        return newCreditors;
    }


    function getAllDebtors() public view returns (address[] memory) {
        address[] memory debtors = new address[](users.length);
        uint idx = 0;

        for (uint i = 0; i < users.length; i++) {
            address to = users[i];

            for (uint j = 0; j < requests[to].length; j++) {
                MoneyRequest storage req = requests[to][j];
                if (req.requestId != 0 && req.from == msg.sender) {
                    debtors[idx] = to;
                    idx++;
                    break;
                }
            }
        }

        address[] memory newDebtors = new address[](idx);
        for (uint i = 0; i < idx; i++) {
            newDebtors[i] = debtors[i];
        }

        return newDebtors;
    }

    function getTotalAmountOwed() public view returns (uint256) {
        uint256 amountOwed = 0;
        for (uint i = 0; i < requests[msg.sender].length; i++) {
            if (requests[msg.sender][i].requestId != 0) {
                amountOwed += requests[msg.sender][i].amount;
            }
        }

        return amountOwed;
    }

    function getTotalAmountRequested() public view returns (uint256) {
        uint256 amountRequested = 0;
        for (uint i = 0; i < users.length; i++) {
            for (uint j = 0; j < requests[users[i]].length; j++) {
                MoneyRequest storage req = requests[users[i]][j];
                if (req.requestId != 0 && req.from == msg.sender) {
                    amountRequested += req.amount;
                }
            }
        }
        return amountRequested;
    }

    function getAmountOwedTo(address from) public view returns (uint256) {
        uint256 amountOwed = 0;
        for (uint i = 0; i < requests[msg.sender].length; i++) {
            MoneyRequest storage req = requests[msg.sender][i];
            if (req.requestId != 0 && req.from == from) {
                amountOwed += requests[msg.sender][i].amount;
            }
        }

        return amountOwed;
    }

    function getAmountRequestedFrom(address to) public view returns (uint256) {
        uint256 amountRequested = 0;
        for (uint i = 0; i < users.length; i++) {
            for (uint j = 0; j < requests[users[i]].length; j++) {
                MoneyRequest storage req = requests[users[i]][j];
                if (req.requestId != 0 && req.from == msg.sender && req.to == to) {
                    amountRequested += req.amount;
                }
            }
        }
        return amountRequested;
    }

    // FALLBACK

    receive() external payable {
        this.payForAllTheRequests();
    }


    // HELPER FUNCTIONS

    function addUser(address user) private {
        bool flag = true;
        for (uint i = 0; i < users.length; i++) {
            if (users[i] == user) {
                flag = false;
                break;
            }
        } 
        if (flag) {
            users.push(user);
        }
    }

    function getMoneyRequest(uint requestId) private view returns (MoneyRequest storage) {
        require(requestId > 0);

        for (uint i = 0; i < users.length; i++) {
            for (uint j = 0; j < requests[users[i]].length; j++) {
                MoneyRequest memory request = requests[users[i]][j];
                if (request.requestId == requestId) {
                    return requests[users[i]][j];
                }
            }
        }
        revert();
    }

    function pay(uint256 balance, address addr, uint256 amount) public payable {
        require(balance >= amount);
        payable(addr).transfer(amount);
    }
}
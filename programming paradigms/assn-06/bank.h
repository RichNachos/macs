#ifndef _BANK_H
#define _BANK_H
#include "semaphore.h"

typedef unsigned long BranchID;

typedef unsigned long AccountNumber;
typedef signed long AccountAmount;

typedef struct Bank {
  unsigned int numberBranches;
  struct       Branch  *branches;
  struct       Report  *report;
  unsigned int unfinishedWorkers;
  sem_t reportCheck;
  sem_t startDay;
  sem_t lockReportTransfer;
} Bank;

#include "account.h"

int Bank_Balance(Bank *bank, AccountAmount *balance);

Bank *Bank_Init(int numBranches, int numAccounts, AccountAmount initAmount,
                AccountAmount reportingAmount,
                int numWorkers);

int Bank_Validate(Bank *bank);
int Bank_Compare(Bank *bank1, Bank *bank2);



#endif /* _BANK_H */

# Assignment 2

## Intro

Within the scope of this assignment, we are going to design a simple Point of Sales (POS) system. We will use our design to simulate a day in the "life" of a store that has Cash Registers with three shifts per day. **IMPORTANT**: UI, Persistence, and Concurrency are out of the scope.


## User Stories

> As a *cashier*, I would like to open a receipt so that, I can start serving customers.

> As a *cashier*, I would like to add items to an open receipt so that, I can calculate how much the customer needs to pay.

> As a *customer*, I would like to see a receipt with all my items so that, I know how much I have to pay.

> As a *customer*, I would like to pay (by cash or card) for a receipt so that, I can receive my items.

> As a *cashier*, I would like to close the paid receipt so that, I can start serving the next customer.

> As a *store manager*, I would like to make X reports so that, I can see the current state of the store.

> As a *cashier*, I would like to make Z reports so that, I can close my shift and go home.

## Technical Details

- Store can sell items as singles
- Store can sell items as batches/packs. (think 6-pack of beer cans :D)
- Store can have discounts of various types:
  * Items can have discount
  * Batches/packs can have discount (e.g. if a customer buys a pack of tissues they get -10% off the total price)
  * Combination of items can have discount (e.g. if a customer buys bread and cheese together they get -5% on each)
- Persistence is out of scope (store the data in memory) but designs your solution in a way that adding persistence will be straightforward
- For simplicity X reports only contain revenue and count of each item sold.
- For simplicity Z report only "clears" the Cash Register. After this operation, revenue and the number of items sold are zero.


## Simulation

Repeat:
  - Customers with randomly selected items arrive at POS.
  - Cashier opens the receipt.
  - Cashier registers items one by one in the receipt.
  - Once the cashier registers all items, print the receipt (see example below)
  - Customer pays (picks payment method randomly)
    * with cash: print "Customer paid with cash"
    * with card: print "Customer paid with card"
  - Once the cashier confirms payment they close the receipt.

After every 20th customer, prompt the store manager if they want to make X report. A simple y/n question will suffice.
  * If they pick "y" print out X Report (see example below)
  * If they pick "n" continue the simulation

After every 100th customer, prompt the store manager if they want to end the shift. A simple y/n question will suffice.
  * If they pick "y" simulate cashier making Z Report.
  * If they pick "n" continue the simulation

After three shifts, end the simulation.

## Examples

### Receipt:

| Name          | Units | Price |  Total  |
|---------------|-------|-------|---------|
| Milk          | 1     | 4.99  |  4.99   |
| Mineral Water | 6     | 3.00  |  18.00  |

Sum: 10.99

### X Report:

| Name    | Sold |
|---------|------|
| Milk    | 1    |
| Bread   | 6    |
| Diapers | 2    |

Total Revenue: 12.57

## Linting/formatting

- Format your code using `black` auto formatter
- Sort your imports with `isort` 
- Check your static types with `mypy`
- Check your code with `flake8`

See configuration for these tools [here](https://raw.githubusercontent.com/MadViper/nand2tetris-starter-py/main/setup.cfg)

## Testing

Provide automated tests that will falsify regressions (change in behaviour) in your software artifacts.

## Grading

We will not grade solutions:
  - without decomposition
  - with needlessly long methods or classes
  - with code duplications
In all these cases you will automatically get 0% so, we sincerely ask you to 
not make a mess of your code and not put us in an awkward position.

- 20%: It is tested.
- 20%: It is easy to change.
- 20%: It demonstrates an understanding of design patterns.
- 20%: It demonstrates an understanding of S.O.L.I.D principles.
- 20%: It follows linting/formatting rules.

## Disclaimer

We reserve the right to resolve ambiguous requirements (if any) as we see fit just like a real-life stakeholder would.
So, do not assume anything, ask for clarifications.

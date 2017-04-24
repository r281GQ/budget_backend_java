# Budgeting REST API built with Spring MVC, Hibernate, Hateoas, Spring Security
The app is meant to track incomes and expenses in a fairly straightforward manner.

Its basic element is the transaction which contains the following informations:
  -name
  -amount
  -account
  -budget
  -budgetPeriod
  -grouping
  -equity
  -currency
  -date
  -period
  
Every transaction belongs to an account. These have balances. A transaction can increase or decrease these based whether they are incomes
or expenses. To distinguish between these categories I use groupings. So a simple example would be:

transaction:
  name: example
  amount: 60
  account: main
  grouping: rent
  
account:
  name: main
  balance: 100
  
grouping:
  name: rent
  type: expense
  
So as soon as that transaction hits the corresponding api endpoint it lowers the main account's balance to 40 since the transactions grouping type is expense.

Account and grouping are manditory. 

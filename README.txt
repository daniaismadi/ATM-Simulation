(Important note) To use Stock functions follow these steps:
Install Maven Library for JSON:
File > Project Structure > Project Settings > Libraries
Click on the + sing on the top left corner > From Maven
Paste this address: org.json:json:20180813
Click on the search button.
Add this.

Starting Up
To start the program, run the main function in the class Run.

Shutting down the system
To shut down the system, log in as the Bank Manager, and select the option “Turn off System.” All data will be serialized and stored for the next time you boot the system.

To start the program again, run the main function in the class Run.

Logging in to Bank Manager
In order to identify yourself as the bank manager, login with the following username and password:
Username: manager
Password: password

Enter a number from the list of options listed in the interface to perform tasks that a bank manager is able to perform.

Option 0: Set Date
Use this to go to a specific point in time. Due to the fact that the API for stock data only has information that is at least 1 year behind in time, please input a date that is at least 1 year behind in time.

Option 1: Create User
Select this option to create a new user. As the bank manager, you will set the username and password for the new user. One of each type of account (chequing, credit card, line of credit and savings) will automatically be created when a new user is created.

Option 2: Create Account
Select this option to create a new user. You will have to enter the username of the user and select the type of account (chequing, credit card, line of credit or savings) you would want to create for them.

Option 3: Check Alerts
Select this option to see the alerts.txt file which contains alerts every time the amount of any denomination in the ATM goes below 20.

Option 4: Restock Machine
Select this option to restock the number of bills in the ATM. Once you have selected an option of which bill you want to restock, the ATM will now have 100 of those bills in the machine.

Option 5: Undo Transaction
Select this undo the most recent transaction from a certain user’s account. You would need their username and their account number. Cannot undo transaction if there is insufficient funds in the account (for eg. Undo Withdraw).

Option 6: Logout
Select this option to logout of the Bank Manager account and go back to the Login interface.


Option 7: Turn Off System
The ATM will shut down if you select this option. To reboot the system, simply run ATM.main() again. The date will increment by one day when you reboot.

Logging into Broker

A Broker’s job is to manage mutual funds in the bank.

In order to identify yourself as the broker, login with the following username and password:
Username: broker
Password: password

Option 1: Login as User
Broker has a user account, this option Logs in as user for the broker and performs all user functions.
Option 2: Login as Broker(options)
1. Buy Funds
In this option the broker can buy stocks for a mutual fund owned by the bank.
2. Sell Funds
In this option the broker can sell stocks for a mutual fund owned by the bank.
3. Log Out

Logging into User

Once a bank manager has created a new user, login with the username and password that the bank manager has set to view the user menu.

Enter a number from the list of options listed in the interface to perform tasks that a user is able to perform.


Option 1: Perform Transactions (options):

1: Deposit
Select this option to deposit cash or cheque into your primary chequing account.

2: Withdraw
Select this option to withdraw money from the ATM. You will have to specify the account number in which you would like to withdraw money from.

Note that you can only withdraw whole numbers that are multiples of 5 since the ATM only holds $5, $10, $20 and $50 bills.

3: Transfer In
Select this option to transfer in money between your accounts. Note that you cannot transfer out money from a credit card account.

4: Transfer Out
Select this option to transfer out money between your accounts. Note that you cannot transfer out money from a credit card account.

5: Pay Bills
Select this option to pay a bill. You will have to specify the account number to pay from and the name of the payee. Note that you cannot pay a bill from a credit card account.

6: Go back to main menu

Option 2: Manage Accounts (options):

1. Request account creation
Creates a new account for the user.
2. Request joint account creation
Creates a joint account with another user.
3. Add user to existing account
4. View summary of accounts
Select this option to view a summary of all your accounts. You will be able to see:
The types of accounts you have
The account numbers of all the accounts you have
The balance of each account
The last transaction performed in each account
The date in which each account was created
The net total of all your account balances
5. Go back to main menu


Option 3: Manage Investments
	1. Buy Stocks
	Buys shares of a certain stock (searched using it’s symbol)
2. Sell Stocks
Sells shares of a certain stock (if it is owned by the user)
3. Buy Mutual Funds
Buys a percentage of a mutual fund (Selected by the broker by given risk level)
4. Sell Mutual Funds
Sells the percentage of a given mutual fund (if owned).
5. View your Stocks Investment Portfolio
Shows all stocks and shares owned by user
6. View total money in stocks
Shows available assets in stocks.
7. View your Mutual Funds Investment Portfolio
Shows available assets owned in mutual funds.
8. Go Back


Option 4: Update User Profile
	1. Change password
2. Go back to main menu


Option 5: Manage Subscriptions
	A User can make a subscription if they have a credit card.
1: Add Subscription
Adds a new subscription to the user’s subscriptions list.
2: Remove Subscription
Removes a given subscription from the user’s list (if they have it)
3: View current subscriptions
Shows all the user’s current subscriptions
4: View available subscriptions
Shows all the subscriptions currently in the atm.

Option 6: Logout
Select this option to logout of your user account to go back to the login menu.

Explanation of Text Files

deposits.txt
Stores the deposits made to the accounts, each line being of the format: “AccountNumber,DepositAmount”

alerts.txt
Writes to this file when the ATM is low on a certain bill. Each line is of the format:
“Alert {Bill number}$ bills are low”

outgoing.txt
Stores Bills paid to outgoing accounts/places. Each line is of the format:
“{Amount} paid to {Name}”

Stockfiles.txt
Contains a Map of all available StockNames to Symbols

Explanation of Stocks:
Stocks have a symbol, name and share price, the price is updated every day.
A user can buy shares of stocks through the balance in their stock account.
A user can also sell shares of stocks they own, and the money earned will go to their stocks Account.


Explanation of Mutual Funds:

User can invest in a mutual fund of low, medium or high risk.
The user earns a percent profit from the variance of the value of the  mutual fund they purchase.
A fee is paid to the broker.

Explanation of Subscriptions:

A user can subscribe to a to given subscription, for which there is a cost. The user must have a credit card account, and is charged monthly in that account for their subscriptions.



Stocks
BankManager can open a new stocks account for a user.
Can only buy/sell stocks.
How much money you want to invest.
Which sector you want to invest in (5 sectors).
Users can pick and buy stocks by themselves. Users can also pick and buy mutual funds, which is a pool of funds that the StockBroker can use to invest in a portfolio of stocks.
Mutual funds are rated by risk level, which indicates how volatile the asset is.

Stock Account
Asset account that is used to buy stocks and mutual funds. Money earned from selling stocks and mutual funds also go to this account.

Investment Portfolio:
Stores the user’s assets in stocks and mutual funds.

Broker
Handles stocks account.
Could also be a user with their own accounts.
Buys and Sell funds for the atm.

Subscriber
Handles subscription functionalities for the user (subscribe, de-subscribe, view, etc)

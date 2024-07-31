import sys
import os
from collections import defaultdict

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

# import the required dependencies
from mongoClient import fetchDataMongo

def userTransactionCategory(userId):
    # Fetch the expenditures of the user
    transactions = fetchDataMongo("transactions", {"senderAccNo": userId})

    # Initialize a dictionary to hold the total expenditure for each category
    expenditureByType = defaultdict(float)

    # Process each transaction
    for transaction in transactions:
        transaction_type = transaction.get('type')
        amount = transaction.get('amount', 0)
        # Sum up the amount for each type
        expenditureByType[transaction_type] += amount

    # Print the total expenditures by type
    for category, total_amount in expenditureByType.items():
        print(f"Type: {category}, Total Amount: {total_amount}")

    return expenditureByType




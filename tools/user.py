import sys
import os
from collections import defaultdict
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from datetime import datetime
from langchain.agents import tool, initialize_agent
from mongoClient import fetchDataMongo
from repo.user import userTransactionCategory
import json
import re



@tool
def dateTool(query: str) -> str:
    """Date tool : To get the lastest / current  date"""
    response = " Today is " + datetime.today().strftime("%Y-%m-%d")
    return response

@tool 
def userTransactionCategoryTool(userId: str) -> str:
    """User Transaction Category tool : To get the user transaction by the transaction category"""
    accNo = re.findall(r'\d+', userId)
    print(type(userId))
    print("This is the userid " ,accNo)
    transactions  = userTransactionCategory(int(accNo[0]))
    response = transactions
    return response

@tool
def userLast10Transactions(userId: str) -> str:
    """User Last 10 Transactions tool : To get the last 10 transactions of the user"""
    accNo = re.findall(r'\d+', userId)
    transactions = fetchDataMongo("transactions", {"senderAccNo": int(accNo[0])})
    response = transactions[-10:]
    print("Last 10 Transactions: ", response)
    return response

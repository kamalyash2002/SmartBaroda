import sys
import os
from collections import defaultdict
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from langchain_community.tools import DuckDuckGoSearchRun
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

@tool
def currentBalance(userId: str) -> str:
    """User Current Balance tool : To get the user current balance"""
    accNo = re.findall(r'\d+', userId)
    currentBalance = fetchDataMongo("users", {"accNo": int(accNo[0])})
    print("Current Balance ", currentBalance)
    response = currentBalance[0].get('balance', 'Unknown')
    print("Current Balance ", currentBalance)
    return response

@tool
def incomeCategoryTool(userId: str) -> str:
    """User Income tool : To get the user income category wise"""
    accNo = re.findall(r'\d+', userId)
    income = fetchDataMongo("transactions", {"recieverAccNo": int(accNo[0])})
    ## Categories the income according to the transaction type
    incomeByType = defaultdict(float)
    for transaction in income:
        transaction_type = transaction.get('type')
        amount = transaction.get('amount', 0)
        incomeByType[transaction_type] += amount

    response = incomeByType
    print("Income by type: ", response)
    return response

@tool
def loanIssuedTool(userId: str) -> str:
    """Loan Issued tool : To get the user loan issued details"""
    accNo = re.findall(r'\d+', userId)
    loanIssued = fetchDataMongo("loansissueds", {"recieverAccNo": int(accNo[0])})
    response = loanIssued
    return response

@tool 
def generalTool(query: str) -> str:
    """General tool : To get the general context like latest news related to stocks, scams , investements, finance updates etc"""
    search = DuckDuckGoSearchRun()
    response  = search.invoke(query)
    return response

@tool
def policiesSubscribedTool(userId: str) -> str:
    """Policies Subscribed tool : To get the user policies subscribed details"""
    accNo = re.findall(r'\d+', userId)
    policiesSubscribed = fetchDataMongo("policiessubscribeds", {"recieverAccNo": int(accNo[0])})
    print("Policies Subscribed: ", policiesSubscribed)
    response = policiesSubscribed
    return response




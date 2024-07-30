import sys
import os
from collections import defaultdict
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from datetime import datetime
from langchain.agents import tool, initialize_agent
from mongoClient import fetchDataMongo
from repo.user import userTransactionCategory
import json



@tool
def dateTool(query: str) -> str:
    """Date tool : To get the lastest / current  date"""
    response = " Today is " + datetime.today().strftime("%Y-%m-%d")
    return response

@tool 
def userTransactionCategoryTool(userId: str) -> str:
    """User Transaction Category tool : To get the user transaction by the transaction category"""
    ## fetch the loan details from the json fil and convert it to string 
    transactions  = userTransactionCategory(int(userId))
    response = transactions
    return response
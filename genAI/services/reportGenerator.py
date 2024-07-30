import json
from langchain.agents import tool, initialize_agent
from langchain_openai import AzureChatOpenAI
from dotenv import load_dotenv
import sys
import os
import warnings

load_dotenv()
warnings.filterwarnings("ignore")
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

#tools
from tools.loan import dateTool, loanTool
from tools.user import userTransactionCategoryTool
from mongoClient import fetchDataMongo

#repos
from repo.user import userTransactionCategory

def reportGenerator(userId: str):
    userData = fetchDataMongo("users", {"accNo": int(userId)})[0]
    print(f"User Data: {userData}")

    username = userData.get('username', 'Unknown')
    cibil_score = userData.get('cibilScore', 'Unknown')

    llm = AzureChatOpenAI(
        openai_api_version="2024-04-01-preview",
        azure_deployment="llm_model",
        model_name="gpt3.5-turbo",
    )

    userTransactions =  userTransactionCategory(int(userId))
    print(f"User Transactions: {userTransactions}")
    print(type(userTransactions))
    
    prompt  = (
        f"""
        Analyse the user transaction by the transaction category.
        Suggest the user {username} the best practices to save money for the future in points.
        Consider different factors like health, wealth, child education and lifestyle etc.
        
        These are {username} user's total amount spent in each category:
        {userTransactions}
        """ 
    )

    savingSuggestions = llm.invoke(prompt)
    return {
        "expenditure" : userTransactions,
        "savingsSuggestions" : savingSuggestions.content
    }


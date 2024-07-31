import json
from langchain.agents import tool, initialize_agent
from langchain_openai import AzureChatOpenAI
from dotenv import load_dotenv
import warnings

# load the environment variables
load_dotenv()
warnings.filterwarnings("ignore")

import sys
import os
# Add the parent directory to the path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from tools.loan import dateTool, loanTool
from utils.fetchJsonData import fetchJsonData
from mongoClient import fetchDataMongo

# tools 
from tools.user import userTransactionCategoryTool
from tools.user import userLast10Transactions

def financialAdvisor(userId: str, userPrompt: str) -> list:

    userData = fetchDataMongo("users", {"accNo": int(userId)})[0]


    username = userData.get('username', 'Unknown')
    cibil_score = userData.get('cibilScore', 'Unknown')

    llm = AzureChatOpenAI(
        openai_api_version="2024-04-01-preview",
        azure_deployment="llm_model",
        model_name="gpt3.5-turbo",
    )

    prompt  = f"""
        You are an experienced and highly skilled financial advisor with a deep understanding of various financial instruments, investment strategies, and economic trends. Your goal is to greet and assist the user by offering well-informed and comprehensive responses to their financial inquiries.Feel free to leverage your expertise to provide actionable advice, and don't hesitate to seek further details from the user to tailor your responses to their specific financial situation.
        User {username} with userId is {userId} want to ask you about finance understand the user question and provide the best possible answer in 150 words in markdown format.
        User Question : {userPrompt}
        """ 
    print(prompt)

    tools = [dateTool, userTransactionCategoryTool , userLast10Transactions]

    # Initialize the agent with the specified tools and model
    agent = initialize_agent(
        tools=tools,
        llm=llm,
        agent="zero-shot-react-description",
        verbose=True,
        handle_parsing_errors=True
    )

    ans = agent.run(prompt)
    
    # answer = llm.invoke(prompt)
    return ans
 
# # Example usage
# if __name__ == "__main__":
#     userId = "123456"
#     userPrompt = "Analyse my last 10 transactions" 
#     recommendations = financialAdvisor(userId, userPrompt)
#     print(recommendations)
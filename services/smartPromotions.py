import json
import os
import boto3
import botocore
from datetime import datetime
from typing import Dict, Any, Optional
from langchain.agents import initialize_agent
from langchain_openai import AzureChatOpenAI
from langchain_community.llms import Bedrock
from langchain_community.embeddings import BedrockEmbeddings
import logging
import sys
import shutil
from dotenv import load_dotenv
import warnings

# load the environment variables
load_dotenv()
warnings.filterwarnings("ignore")


import sys
import os
# Add the parent directory to the path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

# Custom Dependencies (file imports):
from tools.loan import dateTool
from tools.promotions import promotionsTool
from utils.fetchJsonData import fetchJsonData
from mongoClient import fetchDataMongo


def smartPromotions(userId: str):

    # #user data
    # userData = json.loads(fetchJsonData("testData/userData.json"))
    # print(type(userData))
    # print("UserData : ")
    # print(userData)
    userData = fetchDataMongo("users", {"accNo" : int(userId)})[0]

    # Variables to be used in the query
    username = userData['username']
    # Assuming these fields are available in the user data
    # annual_income = userData.get('annual_income', 'not provided')
    # employment_status = userData.get('employment_status', 'not provided')

    ## Initialising the Auzre Chat OpenAI model
    llm = AzureChatOpenAI(
        openai_api_version="2024-04-01-preview",
        azure_deployment="llm_model",
        model_name="gpt3.5-turbo",
    )
    ## emebedding model instance

    # tools list
    tools = []
    tools.append(dateTool)
    tools.append(promotionsTool)

    print("Initializing Agent")

    agent = initialize_agent(
        tools=tools, llm=llm, agent="zero-shot-react-description", verbose=True
    )

    print("Agent Initialized")
    agentAns = agent.run(
        f"""
        Generate personalized promotion content for the user : {username} based on the promotions fetched using the Promotions Tool. The content should address the user by their username and highlight the specific benefits of the promotion in a compelling manner. Use the following example as a reference:

        Example: 'Hey [Username], love dining out? Enjoy a 30% discount up to ₹125 on orders over ₹249 at your favorite restaurants with your BOB card!'

        You can name mimicks like Tushar-Tushu, Ujjwal-Ujju, Yash-Yashu etc. to make the content more engaging.

        Please ensure the promotion content is engaging and directly relevant to the user’s interests.
        """ 
    )

    finalAnswer = agentAns
    return finalAnswer

# # Example usage
if __name__ == "__main__":

    userId = "123456"
    recommendations = smartPromotions(userId)
    print(recommendations)



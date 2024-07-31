# 3rd Party Dependencies:
import json
import os
import boto3
import botocore
from datetime import datetime
from typing import Dict, Any, Optional
from langchain.agents import tool, initialize_agent
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
from tools.policy import policyTool
from utils.fetchJsonData import fetchJsonData
from mongoClient import fetchDataMongo


def policyRecommender(userId: str, userPrompt: str):

    ##user data
    # userData = json.loads(fetchJsonData("testData/userData.json"))
    # print(type(userData))
    # print("UserData : ")
    # print(userData)
    userData = fetchDataMongo("users", {"accNo" : int(userId)})[0]

    # Variables to be used in the query
    username = userData['username']
    cibil_score = userData['cibilScore']
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
    tools.append(policyTool)

    agent = initialize_agent(
        tools=tools, llm=llm, agent="zero-shot-react-description", verbose=True
    )

    agentAns = agent.run(
        f"""
        User requirements: {userPrompt}
        Provide the policy recommendation for the user whose CIBIL score is {cibil_score} and use the User requirements.
        Use the policyTool to fetch current available policies and return the recommended multiple policies as the output in an array of JSON objects format as given in JSON example.
        """ +
        """
        JSON Example:

        [{
            "policyType": "health",
            "policyDescription": "this is good policy",
            "premiumAmount": "123",
            "policyTenure": "2"
        }]
        """
    )

   # Parse the response into a list of dictionaries (JSON objects)
    try:
        finalAnswer = json.loads(agentAns)
    except json.JSONDecodeError:
        finalAnswer = []

    print(f"Final Answer: {finalAnswer}")
    return finalAnswer



# # # Example usage
# if __name__ == "__main__":
#     userId = "123456"
#     recommendations = policyRecommender(userId ,"I want Child Policies")
#     print(recommendations)

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
from mongoClient import fetchDataMongo

def loanRecommender(userId: str, userPrompt: str) -> list:
    # Fetch user data from MongoDB
    userData = fetchDataMongo("users", {"accNo": int(userId)})[0]

    # Extract necessary user details
    username = userData.get('username', 'Unknown')
    cibil_score = userData.get('cibilScore', 'Unknown')

    # Initialize the Azure Chat OpenAI model
    llm = AzureChatOpenAI(
        openai_api_version="2024-04-01-preview",
        azure_deployment="llm_model",
        model_name="gpt3.5-turbo",
    )

    # List of tools available to the agent
    tools = [dateTool, loanTool]

    # Initialize the agent with the specified tools and model
    agent = initialize_agent(
        tools=tools,
        llm=llm,
        agent="zero-shot-react-description",
        verbose=True
    )

    # Run the agent with the user prompt and extract the answer
    
    agentAns = agent.run(
        f"""
        User requirements: {userPrompt}
        Provide the loan recommendation for the user {username} whose CIBIL score is {cibil_score} and use the User requirements.
        Use the loanTool to fetch current available loans and return the recommended multiple loans as the output in an array of JSON objects format and no text.
        """ + 
        """
        JSON Example:

        [{
            "loanType": "Home Loan",
            "loanAmount": "100",
            "loanTenure": "10 months",
            "interestRate": "4%",
            "emi": "2200",
            "processingFee": "0.5%",
            "totalCost": "132000"
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

# # Example usage
if __name__ == "__main__":
    userId = "123456"
    recommendations = loanRecommender(userId ,"I want buy house")
    print(recommendations)
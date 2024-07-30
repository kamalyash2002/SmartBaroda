from datetime import datetime
from langchain.agents import tool, initialize_agent
from mongoClient import fetchDataMongo
import json
def fetchJsonData (file_path: str) -> str:
    """Fetches the data from the json file and converts it to string"""
    try:
        with open(file_path, 'r') as json_file:
            data = json.load(json_file)
            return json.dumps(data, indent=4) 
    except Exception as e:
        return str(e)

@tool
def dateTool(query: str) -> str:
    """Date tool : To get the lastest / current  date"""
    response = " Today is " + datetime.today().strftime("%Y-%m-%d")
    return response

@tool 
def loanTool(query: str) -> str:
    """Loan tool : To get details of the loan that is available"""
    ## fetch the loan details from the json fil and convert it to string 
    loans = fetchDataMongo("loandetails", {})
    response = loans
    return response
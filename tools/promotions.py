from datetime import datetime
from langchain.agents import tool, initialize_agent

import sys
import os
# Add the parent directory to the path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from mongoClient import fetchDataMongo
import json
import random



@tool 
def promotionsTool(query: str) -> str:
    """Promotions Tool : To get details of the promotions that is available"""
    ## fetch the loan details from the json fil and convert it to string 
    promotions = fetchDataMongo("promotion", {})
    ## choose the random promotion from the list of promotions
    randomPromotion = promotions[random.randint(0, len(promotions) - 1)]
    return randomPromotion


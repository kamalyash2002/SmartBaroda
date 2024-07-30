from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from services.loanRecommenderAgent import loanRecommender
from services.policyRecommenderAgent import policyRecommender
from services.financialAdvisor import financialAdvisor
from services.smartPromotions import smartPromotions
from services.reportGenerator import reportGenerator
import json

app = FastAPI()

app = FastAPI()

class PromptItem(BaseModel):
    userPrompt: str

@app.post("/loanRecommender/{userId}")
def loan_handler(userId: str, prompt: PromptItem):
    userPrompt = prompt.userPrompt
    print(f"User ID in Loan Recommender: {userId}")
    print(f"User Prompt in Loan Recommender: {userPrompt}")
    
    # Call the loanRecommender function
    recommendations = loanRecommender(userId, userPrompt)
    print(f"Loan Recommendations (raw): {recommendations}")
    
    try:
        # If the result is already a Python object (list of dicts), no need to parse as JSON
        if isinstance(recommendations, str):
            parsed_recommendations = json.loads(recommendations)
        else:
            parsed_recommendations = recommendations
    except (json.JSONDecodeError, TypeError) as e:
        print(f"Error parsing recommendations: {e}")
        raise HTTPException(status_code=500, detail="Failed to parse recommendations")
    
    return parsed_recommendations


@app.post("/policyRecommender/{userId}")
def policy_handler(userId: str, prompt: PromptItem):
    userPrompt = prompt.userPrompt
    print("User Prompt:", userPrompt)
    
    # Call the policyRecommender function
    recommendations = policyRecommender(userId, userPrompt)
    print("Policy Recommendations (raw):", recommendations)
    
    try:
        # If the result is already a Python object (list of dicts), no need to parse as JSON
        if isinstance(recommendations, str):
            parsed_recommendations = json.loads(recommendations)
        else:
            parsed_recommendations = recommendations
    except (json.JSONDecodeError, TypeError) as e:
        print(f"Error parsing recommendations: {e}")
        raise HTTPException(status_code=500, detail="Failed to parse recommendations")
    
    return parsed_recommendations


@app.post("/financialAdvisor/{userId}")
def financial_handler(userId: str, prompt: PromptItem):
    userPrompt = prompt.userPrompt
    print("User Prompt:", userPrompt)
    
    # Call the financialAdvisor function
    recommendations = financialAdvisor(userId, userPrompt)
    print("Financial Recommendations (raw):", recommendations)

    # return in customised format
    return {"answer": recommendations}

@app.get("/smartPromotions/{userId}")
def smartPromotions_handler(userId: str):
    smartNotifications = smartPromotions(userId)
    return {"smartNotifications": smartNotifications}

@app.get("/reportGenerator/{userId}")
def report_handler(userId: str):
    report = reportGenerator(userId)
    print("Report:", report)
    return report

@app.get("/")
def read_root():
    return {"Hello": "Uphar Randi hai"}

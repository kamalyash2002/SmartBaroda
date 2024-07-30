from datetime import datetime
import sys
import os
# Add the parent directory to the path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from mongoClient import fetchDataMongo
import json
import random


def randomPromotions():
    promotions = fetchDataMongo("promotion", {})
    ## choose the random promotion from the list of promotions
    randomPromotion = promotions[random.randint(0, len(promotions) - 1)]
    return randomPromotion


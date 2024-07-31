from pymongo import MongoClient
from pymongo.server_api import ServerApi
import pymongo
import os 
from dotenv import load_dotenv  
import certifi

load_dotenv()

# Constants
userName = os.environ.get("MONGO_USERNAME")     
password = os.environ.get("MONGO_PASSWORD") 

MONGO_URI = f"mongodb+srv://{userName}:{password}@cluster0.vbt4ktw.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
MONGO_URI1 = "mongodb+srv://admin:DkW0i3OLRt2y3jcf@cluster0.vbt4ktw.mongodb.net/"
DB_NAME = "test"

def fetchDataMongo(collection_name, query={}):
    """
    Connects to MongoDB and fetches data from the specified collection based on the query.

    :param collection_name: Name of the collection
    :param query: MongoDB query (default is empty query to fetch all documents)
    :return: List of documents from the collection
    """
    # Create a MongoClient to the running mongod instance
    client = MongoClient(MONGO_URI1, tlsCAFile=certifi.where() )
    print("Client created")
    
    # Connect to the database
    db = client[DB_NAME]
    
    # Get the collection
    collection = db[collection_name]
    print("Checking collection")
    
    # Fetch data based on the query
    cursor = collection.find(query)
    
    # Convert the cursor to a list of documents
    documents = list(cursor)
    
    # Close the connection
    client.close()
    
    return documents





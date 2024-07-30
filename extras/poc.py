import os
from langchain_openai import AzureOpenAI
from langchain_openai import AzureChatOpenAI
from langchain.schema import SystemMessage, HumanMessage
from langchain.callbacks import get_openai_callback
from langchain_community.vectorstores import FAISS
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough
from langchain.prompts import PromptTemplate
from langchain_core.documents import Document
from dotenv import load_dotenv
import warnings

# load the environment variables
load_dotenv()
warnings.filterwarnings("ignore")

# check the os environment variable settings
azure_openai_api_key = os.environ["AZURE_OPENAI_API_KEY"]
azure_openai_endpoint = os.environ["AZURE_OPENAI_ENDPOINT"]


## Initialising the Auzre Chat OpenAI model
llm = AzureChatOpenAI(
    openai_api_version="2024-04-01-preview",
    azure_deployment="llm_model",
    model_name="gpt3.5-turbo"
)

messages = [
    SystemMessage(
        content=(
            "You are Loan Recommendation Chatbot.With Loan Data and customer data recommend the best loan for the customer in Markdown format."
        )
    ),
    HumanMessage(content="What is capital of India  ?"),

]


res = llm(messages)
print (res)

# # print the response
# print(res.content)
# # print the total tokens used
# with get_openai_callback() as cb:
#     print(cb.total_tokens)

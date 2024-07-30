import json
def fetchJsonData (file_path: str) -> str:
    """Fetches the data from the json file and converts it to string"""
    try:
        with open(file_path, 'r') as json_file:
            data = json.load(json_file)
            return json.dumps(data, indent=4) 
    except Exception as e:
        return str(e)
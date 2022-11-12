import requests
from api_secrets import *
import json
import time

# The AssemblyAI url and API key for transcribing the audio file
transcript_endpoint = "https://api.assemblyai.com/v2/transcript"
assemblyai_headers = {'authorization': API_KEY_ASSEMBLYAI}


# The ListenNotes endpoint url and API key
listennotes_episode_endpoint = "https://listen-api.listennotes.com/api/v2"
listennotes_headers = {'X-ListenAPI-Key': API_KEY_LISTENNOTES}


def get_episode_audio_url(episode_id):
    url = listennotes_episode_endpoint + '/' + episode_id
    response = requests.request('GET', url, headers=listennotes_headers)

    data = response.json()

    print(data)


# 2. Start transcription
def transcribe(audio_url, sentiment_analysis):
    transcript_request = {"audio_url": audio_url,
                          "sentiment_analysis": sentiment_analysis}

    transcript_response = requests.post(
        transcript_endpoint, json=transcript_request, headers=headers)

    job_id = transcript_response.json()['id']

    return job_id


# 3. Keep polling AssemblyAI api to see when transcription is done
def poll(transcript_id):
    polling_endpoint = transcript_endpoint + '/' + transcript_id
    polling_response = requests.get(polling_endpoint, headers=headers)
    return polling_response.json()


def get_transcription_results_url(audio_url, sentiment_analysis):
    transcript_id = transcribe(audio_url, sentiment_analysis)
    while True:
        data = poll(transcript_id)
        if data['status'] == 'completed':
            return data, None
        elif data['status'] == 'error':
            return data, data['error']

        print("Waiting 30 seconds...")
        time.sleep(30)


# 4. Save transcript
def save_transcript(audio_url, filename, sentiment_analysis=False):

    data, error = get_transcription_results_url(audio_url, sentiment_analysis)

    if data:
        text_filename = filename + ".txt"
        with open(text_filename, "w") as f:
            f.write(data['text'])

        if sentiment_analysis:
            json_filename = filename + "_sentiments.json"
            with open(json_filename, "w") as f:
                sentiments = data["sentiment_analysis_results"]
                json.dump(sentiments, f, indent=4)

        print("Transcription saved")

    elif error:
        print("Error")

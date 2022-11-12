import json
import time
import pprint

import requests
from api_secrets import API_KEY_ASSEMBLYAI, API_KEY_LISTENNOTES

# The AssemblyAI url and API key for transcribing the audio file
transcript_endpoint = "https://api.assemblyai.com/v2/transcript"
assemblyai_headers = {'authorization': API_KEY_ASSEMBLYAI}


# The ListenNotes endpoint url and API key
listennotes_episode_endpoint = "https://listen-api.listennotes.com/api/v2/episodes"
listennotes_headers = {'X-ListenAPI-Key': API_KEY_LISTENNOTES}


def get_episode_audio_url(episode_id):
    url = listennotes_episode_endpoint + '/' + episode_id
    response = requests.request('GET', url, headers=listennotes_headers)

    data = response.json()

    pprint.pprint(data)

    audio_url = data['audio']
    episode_thumbnail = data['thumbnail']
    podcast_title = data['podcast']['title']
    episode_title = data['title']

    return audio_url, episode_thumbnail, podcast_title, episode_title


# 2. Start transcription
def transcribe(audio_url, auto_chapters):
    transcript_request = {"audio_url": audio_url,
                          "auto_chapters": auto_chapters}

    transcript_response = requests.post(
        transcript_endpoint, json=transcript_request, headers=assemblyai_headers)

    job_id = transcript_response.json()['id']

    return job_id


# 3. Keep polling AssemblyAI api to see when transcription is done
def poll(transcript_id):
    polling_endpoint = transcript_endpoint + '/' + transcript_id
    polling_response = requests.get(
        polling_endpoint, headers=assemblyai_headers)
    return polling_response.json()


def get_transcription_results_url(audio_url, auto_chapters):
    transcript_id = transcribe(audio_url, auto_chapters)
    while True:
        data = poll(transcript_id)
        if data['status'] == 'completed':
            return data, None
        elif data['status'] == 'error':
            return data, data['error']

        print("Waiting 60 seconds...")
        time.sleep(60)


# 4. Save transcript
def save_transcript(episode_id):
    audio_url, episode_thumbnail, podcast_title, episode_title = get_episode_audio_url(
        episode_id)

    data, error = get_transcription_results_url(audio_url, auto_chapters=True)

    pprint.pprint(data)

    if data:
        text_filename = episode_id + ".txt"
        with open(text_filename, "w") as f:
            f.write(data['text'])

        chapters_filename = episode_id + "_chapters.json"
        with open(chapters_filename, "w") as f:
            chapters = data['chapters']

            episode_data = {"chapters": chapters}
            episode_data["episode_thumbnail"] = episode_thumbnail
            episode_data["episode_title"] = episode_title
            episode_data["podcast_title"] = podcast_title

            json.dump(episode_data, f)
            print("Transcript saved")
            return True

    elif error:
        print("Error")
        return False

from api_communication import *
import sys

# Allows us to get the file from the terminal
filename = sys.argv[1]

audio_url = upload(filename)
save_transcript(audio_url, filename)

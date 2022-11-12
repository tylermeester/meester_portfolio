from pydub import AudioSegment

audio = AudioSegment.from_wav(
    "Python/speech_recognition_projects/wave_file_practice/output.wav")

# Increase the volume by 6dB
audio = audio + 6

# Repeat clip
audio = audio * 2

# Fade in
audio = audio.fade_in(2000)

audio.export(
    "Python/speech_recognition_projects/wave_file_practice/mashup.mp3", format="mp3")

audio2 = AudioSegment.from_mp3(
    "Python/speech_recognition_projects/wave_file_practice/mashup.mp3")

print("Done!")

# Audio file formats
# .mp3
# .flac
# wav
import wave

# Audio signal parameters
# - number of channels (stereo has two channels)
# - sample width (number of bytes for each sample)
# - framerate/sample_rate (the number of samples for each second) 44,100hz = 44,100 sample values/sec
# - number of frames
# - values of a frame

# Opens a wav file in read only mode
obj = wave.open(
    "Python/speech_recognition_projects/wave_file_practice/test_audio.wav", "rb")

print("Number of channels", obj.getnchannels())
print("Sample width", obj.getsampwidth())
print("Frame rate", obj.getframerate())
print("Number of frames", obj.getnframes())
print("Parameters", obj.getparams())

time_audio = obj.getnframes() / obj.getframerate()  # time in seconds
print(time_audio)

frames = obj.readframes(-1)  # reads all frames
print(type(frames), type(frames[9]))
print(len(frames))

obj.close()


# Creating a wav file in write only mode
obj_new = wave.open("test_new.wav", "wb")
obj_new.setnchannels(1)
obj_new.setsampwidth(2)
obj_new.setframerate(48000.0)
obj_new.writeframes(frames)
obj_new.close()

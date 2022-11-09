import wave
import matplotlib.pyplot as plt
import numpy as np

obj = wave.open(
    "Python/speech_recognition_projects/wave_file_practice/test_audio.wav", "rb")

sample_freq = obj.getframerate()
n_samples = obj.getnframes()
signal_wave = obj.readframes(-1)
obj.close()

time_audio = n_samples / sample_freq
print(time_audio)


signal_array = np.frombuffer(signal_wave, dtype=np.int16)

times = np.linspace(0, time_audio, num=n_samples)

plt.figure(figsize=(15, 5))
plt.plot(times, signal_array)
plt.title("Audio Signal")
plt.ylabel("Signal Wave")
plt.xlabel("Time in Seconds")
plt.xlim(0, time_audio)
plt.show()
plt.savefig("audio_plot")

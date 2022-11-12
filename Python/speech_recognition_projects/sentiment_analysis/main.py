import json
from youtube_extractor import get_audio_url, get_video_info
from api_communication import save_transcript


def save_video_sentiment(url):
    video_info = get_video_info(url)
    audio_url = get_audio_url(video_info)
    title = video_info["title"]
    title = title.strip().replace(" ", "_")
    save_transcript(audio_url, title, sentiment_analysis=True)


if __name__ == "__main__":
    # save_video_sentiment("https://www.youtube.com/watch?v=e-kSGNzu0hM")
    # save_video_sentiment("https://www.youtube.com/watch?v=4JrHJOyQ1dQ")

    with open("/Users/tylermeester/meester_portfolio/Python/speech_recognition_projects/sentiment_analysis/data/iPhone_13_Review:_Pros_and_Cons_sentiments.json", "r") as f:
        data = json.load(f)

    with open("/Users/tylermeester/meester_portfolio/Python/speech_recognition_projects/sentiment_analysis/data/2021_SkiEssentials.com_Ski_Test_-_Black_Crows_Corvus_sentiments.json", "r") as f:
        data = json.load(f)

    positives = []
    neutrals = []
    negatives = []

    for result in data:
        text = result["text"]
        if result["sentiment"] == "POSITIVE":
            positives.append(text)
        elif result["sentiment"] == "NEGATIVE":
            negatives.append(text)
        else:
            neutrals.append(text)

    positive_count = len(positives)
    neutral_count = len(neutrals)
    negative_count = len(negatives)

    sentiment_ratio = positive_count/(positive_count + negative_count)
    print("Num Positive: " + str(positive_count))
    print("Num Neutral: " + str(neutral_count))
    print("Num Negative: " + str(negative_count))

    print(f"Positive ratio: {sentiment_ratio:.3f}")

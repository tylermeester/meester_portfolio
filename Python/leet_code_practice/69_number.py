# Solution that converts to string
def maximum69number(number):

    number_char_list = list(str(number))

    for i, char in enumerate(number_char_list):
        if char == "6":
            number_char_list[i] = "9"
            break

    return int("".join(number_char_list))


if __name__ == "__main__":
    number = 6699

    print(maximum69number(number))

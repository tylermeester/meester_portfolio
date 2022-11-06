def selection_sort(array):

    for i in range(len(array) - 1):
        smallest_value_index = i

        for j in range(i + 1, len(array)):
            if array[j] < array[smallest_value_index]:
                smallest_value_index = j

        if smallest_value_index != i:
            temp = array[i]
            array[i] = array[smallest_value_index]
            array[smallest_value_index] = temp

        i += 1

    return array


if __name__ == "__main__":
    elements = [11, 9, 29, 7, 2, 15, 28]
    selection_sort(elements)
    print(elements)

def bubble_sort(array):
    size = len(array)

    for i in range(size - 1):
        swapped = False
        for j in range(size - 1):
            if array[j] > array[j+1]:
                temp = array[j+1]
                array[j+1] = array[j]
                array[j] = temp
                swapped = True
        if not swapped:
            break

    return array


if __name__ == "__main__":
    elements = [11, 9, 29, 7, 2, 15, 28]
    bubble_sort(elements)
    print(elements)

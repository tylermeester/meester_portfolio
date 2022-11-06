def partition(array, start, end):
    pivot_index = start
    pivot = array[pivot_index]

    while start < end:

        while start < len(array) and array[start] <= pivot:
            start += 1

        while array[end] > pivot:
            end -= 1

        if start < end:
            array[start], array[end] = array[end], array[start]

    array[pivot_index], array[end] = array[end], array[pivot_index]

    return end


def quick_sort(array, start, end):

    if start < end:

        partition_index = partition(array, start, end)

        quick_sort(array, start, partition_index - 1)  # left partition

        quick_sort(array, partition_index + 1, end)  # right partition

    return array


if __name__ == "__main__":
    elements = [11, 9, 29, 7, 2, 15, 28]
    quick_sort(elements, 0, len(elements) - 1)
    print(elements)

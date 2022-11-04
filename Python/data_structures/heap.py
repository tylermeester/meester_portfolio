class Heap:
    def __init__(self) -> None:
        self.data = []

    # Get the root node of the heap
    def root_node(self):
        return self.data[0]

    # Get the last node of the heap
    def last_node(self):
        return self.data[-1]

    # Get the index of the left child node of the inputted index
    def left_child_index(self, index):
        return (index * 2) + 1

    # Get the index of the right child node of the inputted index
    def right_child_index(self, index):
        return (index * 2) + 2

    # Get the index of the parent node of the node at the inputted index
    def parent_index(self, index):
        if index >= 0:
            return (index - 1) // 2
        else:
            return None

    # Determines whether the node value at the index has a child with a larger value
    def has_greater_child(self, index):
        if ((self.left_child_index(index) and self.left_child_index(index) > self.data[index])
                or (self.right_child_index(index) and self.right_child_index(index) > self.data[index])):
            return True

    # Determines the index of the child node that has a larger value,
    # assumes there is a child with a greater value from has_greater_child function

    def greater_child_index(self, index):
        if not self.right_child_index(index):
            return self.left_child_index(index)

        if self.right_child_index(index) > self.left_child_index(index):
            return self.right_child_index(index)
        else:
            return self.left_child_index(index)

    def insert(self, value):
        # Insert value at the end of the heap
        self.data.append(value)

        # Get the index of the inputted value
        value_index = len(self.data) - 1

        # Get the index of the parent node of the inputted value
        parent_index = self.parent_index(value_index)

        # While the value is greater than the parent node, swap the value with the parent node
        # Update the value index and parent index for the next iteration of the loop
        while parent_index is not None and self.data[value_index] > self.data[parent_index]:
            self.data[value_index], self.data[parent_index] = self.data[parent_index], self.data[value_index]

            value_index = parent_index
            parent_index = self.parent_index(value_index)

    # Deletes the root node, and then rebalances the heap

    def delete(self):
        self.data[0], self.data[-1] = self.data[-1], self.data[0]

        self.data.pop

        trickle_node_index = 0

        while self.has_greater_child(trickle_node_index):
            larger_child_index = self.greater_child_index(trickle_node_index)

            self.data[larger_child_index], self.data[trickle_node_index] = self.data[trickle_node_index], self.data[larger_child_index]

            trickle_node_index = larger_child_index

    def delete(self):
        # Swap the root node with the last node
        self.data[0], self.data[-1] = self.data[-1], self.data[0]

        # Delete the last node
        self.data.pop()

        # Get the index of the root node
        root_index = 0

        # Get the index of the left child node of the root node
        left_child_index = self.left_child_index(root_index)

        # Get the index of the right child node of the root node
        right_child_index = self.right_child_index(root_index)

        # While the root node is less than either of its child nodes, swap the root node with the larger child node
        # Update the root index, left child index, and right child index for the next iteration of the loop
        while left_child_index < len(self.data) and right_child_index < len(self.data) and (self.data[root_index] < self.data[left_child_index] or self.data[root_index] < self.data[right_child_index]):
            if self.data[left_child_index] > self.data[right_child_index]:
                self.data[root_index], self.data[left_child_index] = self.data[left_child_index], self.data[root_index]

                root_index = left_child_index
                left_child_index = self.left_child_index(root_index)
                right_child_index = self.right_child_index(root_index)
            else:
                self.data[root_index], self.data[right_child_index] = self.data[right_child_index], self.data[root_index]

                root_index = right_child_index
                left_child_index = self.left_child_index(root_index)
                right_child_index = self.right_child_index(root_index)


if __name__ == '__main__':

    numbers = [100, 88, 25, 87, 16, 8, 12, 86, 50, 2, 15, 3]

    heap_list = Heap()
    for number in numbers:
        heap_list.insert(number)

    print(heap_list.data, "\n")

    for i in range(len(heap_list.data) - 1):
        parent_node_index = heap_list.parent_index(i)

        print("Node value: ", heap_list.data[i])

        if heap_list.data[i] == heap_list.root_node():
            print("Parent node: this is the root node")
        else:
            print("Parent node value: ", heap_list.data[parent_node_index])

        print(' ')

    heap_list.delete()

    print("DELETION HERE \n")

    for i in range(len(heap_list.data) - 1):
        parent_node_index = heap_list.parent_index(i)

        print("Node value: ", heap_list.data[i])

        if heap_list.data[i] == heap_list.root_node():
            print("Parent node: this is the root node")
        else:
            print("Parent node value: ", heap_list.data[parent_node_index])

        print(' ')

    print("Goodbye!")

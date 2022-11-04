class BinarySearchTreeNode:
    def __init__(self, data):
        self.data = data
        self.left = None
        self.right = None

    def add_child(self, data):
        if data == self.data:
            return
        if data < self.data:
            if self.left:
                self.left.add_child(data)
            else:
                self.left = BinarySearchTreeNode(data)

        if data > self.data:
            if self.right:
                self.right.add_child(data)
            else:
                self.right = BinarySearchTreeNode(data)

    def in_order_traversal(self):
        elements = []

        # Visit left node first
        if self.left:
            elements += self.left.in_order_traversal()

        # Visit base node
        elements.append(self.data)

        # Visit right node
        if self.right:
            elements += self.right.in_order_traversal()

        return elements

    def pre_order_traversal(self):
        elements = []

        # Visit root node first
        elements.append(self.data)

        # Visit left node
        if self.left:
            elements += self.left.pre_order_traversal()

        if self.right:
            elements += self.right.pre_order_traversal()

        return elements

    def post_order_traversal(self):
        elements = []

        # Visit left node first
        if self.left:
            elements += self.left.post_order_traversal()

        # Visit right node
        if self.right:
            elements += self.right.post_order_traversal()

        # Visit root node
        elements.append(self.data)

        return elements

    def search(self, value):
        if self.data == value:
            return True

        if value < self.data:
            if self.left:
                return self.left.search(value)
            else:
                return False

        if value > self.data:
            if self.right:
                return self.right.search(value)
            else:
                return False

    def build_tree(elements):
        root = BinarySearchTreeNode(elements[0])

        for i in range(1, len(elements)):
            root.add_child(elements[i])

        return root

    def find_min(self):
        if self.left is None:
            return self.data
        return self.left.find_min()

    def find_max(self):
        if self.right is None:
            return self.data
        return self.right.find_max()

    def calculate_sum(self):
        left_sum = self.left.calculate_sum() if self.left else 0
        right_sum = self.right.calculate_sum() if self.right else 0
        return self.data + left_sum + right_sum

    def calculate_product(self):
        left_product = self.left.calculate_product() if self.left else 1
        right_product = self.right.calculate_product() if self.right else 1
        return self.data * left_product * right_product

    def delete(self, val):
        if val < self.data:
            if self.left:
                self.left = self.left.delete(val)

        elif val > self.data:
            if self.right:
                self.right = self.right.delete(val)

        else:
            if self.left is None and self.right is None:
                return None

            if self.left is None:
                return self.right

            if self.right is None:
                return self.right

            #min_value = self.right.find_min()
            #self.data = min_value
            #self.right = self.right.delete(min_value)

            max_value = self.left.find_max()
            self.data = max_value
            self.left = self.left.delete(max_value)

        return self


if __name__ == '__main__':

    numbers = [17, 4, 1, 20, 9, 23, 18, 34]
    numbers_tree = BinarySearchTreeNode.build_tree(numbers)

    countries = ["India", "Pakistan", "Germany",
                 "USA", "China", "India", "UK", "USA", "Canada"]
    countries_tree = BinarySearchTreeNode.build_tree(countries)

    print(numbers_tree.in_order_traversal())
    print(numbers_tree.search(20))
    print(numbers_tree.find_min())

    print(countries_tree.in_order_traversal())
    print(countries_tree.find_min())

    print(numbers_tree.calculate_sum())

    numbers_tree.delete(1)
    print(numbers_tree.in_order_traversal())

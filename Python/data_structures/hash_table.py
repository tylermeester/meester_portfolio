class HashTable:
    def __init__(self):
        self.MAX = 100
        self.array = [None for i in range(self.MAX)]

    def get_hash(self, key):
        h = 0
        for char in key:
            # Ord gives the ASCII value of a character
            h += ord(char)

            # By finding the remainder after dividing by 100, we can calculate a
            # unique index value for the hash table. This 100 is assuming a fixed
            # size of 100 for the hash table
            return h % 100

    def add(self, key, value):
        # Uses the hash function to calculate a unique index value to store the value with
        hash = self.get_hash(key)
        self.array[hash] = value

    def get(self, key):
        # Uses the hash function to determine the index of the value we want to retrieve
        hash = self.get_hash(key)
        return self.array[hash]

    # 'add' and 'get' functions will work, but we can override the '__setitem__' and '__getitem__'
    # python operators so that we can more easily assign and retrieve values

    def __setitem__(self, key, value):
        # Uses the hash function to calculate a unique index value to store the value with
        hash = self.get_hash(key)
        self.array[hash] = value

    def __getitem__(self, key):
        # Uses the hash function to determine the index of the value we want to retrieve
        hash = self.get_hash(key)
        return self.array[hash]


if __name__ == '__main__':
    # Creating the hash table
    hash_table = HashTable()

    # Using the __setitem__ and __getitem__ operators
    hash_table["Nov 30"] = 100
    hash_table["March 1"] = 120
    print(hash_table["Nov 30"])
    print(hash_table["March 1"])
    print(hash_table.array)

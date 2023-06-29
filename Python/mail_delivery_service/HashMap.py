class HashMap:
    """
    A simple implementation of a hash map. It is implemented as a list of lists where each sub-list (bucket)
    stores key-value pairs.

    Attributes:
        list (list): The main list which contains the sub-lists (buckets).
    """
    
    def __init__(self, initial_capacity = 50):
        """
        Initializes the hash map with a default initial capacity. 

        Args:
            initial_capacity (int): The initial capacity of the hash map. Defaults to 50.
        """
        self.list = []
        for i in range(initial_capacity):
            self.list.append([])

    def __iter__(self):
        """
        Flattens out the list of lists to iterate over it.

        Returns:
            self: An instance of the class.
        """
        self.flat_list = [item for sublist in self.list for item in sublist]
        self.iter_index = 0
        return self

    def __next__(self):
        """
        Gets the next key in the flattened list.

        Returns:
            The next key in the flattened list.

        Raises:
            StopIteration: If there is no next item.
        """
        if self.iter_index < len(self.flat_list):
            result = self.flat_list[self.iter_index][0]  # get the key
            self.iter_index += 1
            return result
        else:
            raise StopIteration

    def insert(self, key, item):
        """
        Inserts a new item into the hash table.

        Args:
            key: The key for the item.
            item: The item to insert.

        Returns:
            True if the insertion was successful, False otherwise.
        """
        bucket = hash(key) % len(self.list)
        bucket_list = self.list[bucket]

        for kv in bucket_list:
            if kv[0] == key:
                kv[1] = item
                return True

        key_value = [key, item]
        bucket_list.append(key_value)
        return True

    def lookup(self, key):
        """
        Looks up an item in the hash table using a given key.

        Args:
            key: The key for the item.

        Returns:
            The item if the key was found, None otherwise.
        """
        bucket = hash(key) % len(self.list)
        bucket_list = self.list[bucket]

        for pair in bucket_list:
            if key == pair[0]:
                return pair[1]

        return None

    def remove(self, key):
        """
        Removes a key-value pair from the hash table using the key.

        Args:
            key: The key for the item.

        Returns:
            True if the removal was successful, False otherwise.
        """
        slot = hash(key) % len(self.list)
        destination = self.list[slot]

        for pair in destination:
            if key == pair[0]:
                destination.remove(pair)
                return True

        return False

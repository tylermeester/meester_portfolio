class Truck:
    """
    The Truck class represents a delivery truck.

    Attributes:
    capacity (int): The maximum package capacity of the truck.
    speed (float): The speed of the truck.
    packages (list): The list of packages on the truck.
    mileage (float): The total mileage of the truck.
    address (str): The current location of the truck.
    departureTime (datetime.timedelta): The time when the truck departed from the hub.

    Methods:
    __str__: Returns a string representation of the truck.
    """
    def __init__(self, capacity, speed, packages, mileage, address, departureTime):
        self.capacity = capacity
        self.speed = speed
        self.packages = packages
        self.mileage = mileage
        self.address = address
        self.departureTime = departureTime
        self.time = departureTime
        
    def __str__(self): 
        """
        Returns a string representation of the Truck.
        """
        return "%s, %s, %s, %s, %s, %s" % (self.capacity, self.speed, self.packages, self.mileage,
                                           self.address, self.departureTime)

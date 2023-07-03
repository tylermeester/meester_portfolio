class Package:
    """
    The Package class represents a package to be delivered. 
    
    Attributes:
        ID (str): Unique identifier for the package.
        address (str): The address where the package is to be delivered.
        city (str): The city where the package is to be delivered.
        state (str): The state where the package is to be delivered.
        zipcode (str): The zip code where the package is to be delivered.
        deadline (str): The delivery deadline for the package.
        weight (float): The weight of the package in kilos.
        status (str): The current status of the package. Possible values are 'At hub', 'En route', and 'Delivered'.
        departureTime (datetime): The time when the package leaves the hub.
        deliveryTime (datetime): The time when the package is delivered.
    
    Methods:
        __str__(): Returns a string representation of the package.
        
        statusUpdate(timedelta): Updates the package's status based on the provided timedelta.
        
        statusCheck(inputTime): Checks the package's status at a specific point in time.
    """
    
    def __init__(self, ID, address, city, state, zipcode, deadline, weight, status):
        self.ID = ID
        self.address = address
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.deadline = deadline
        self.weight = weight
        self.status = status
        self.departureTime = None
        self.deliveryTime = None

    def __str__(self): 
        return "%s, %s, %s, %s, %s, %s, %s, %s, %s" % (self.ID, self.address, self.city, self.state, self.zipcode,
                                                       self.deadline, self.weight, self.deliveryTime,
                                                       self.status)

    def statusUpdate(self, timedelta):
        """
        Updates the package's status based on the provided timedelta.
        
        Args:
            timedelta (datetime.timedelta): The timedelta to compare with the package's delivery and departure times.
        
        Returns:
            None
        """
        if self.deliveryTime < timedelta:
            self.status = "Delivered"
        elif self.departureTime > timedelta:
            self.status = "En route"
        else:
            self.status = "At hub"
            
    def statusCheck(self, inputTime):
        """
        Checks the package's status at a specific point in time.
        
        Args:
            inputTime (datetime.datetime): The point in time to check the package's status at.
        
        Returns:
            str: The package's status at the inputTime.
        """
        if inputTime < self.departureTime:
            return 'At hub'
        elif self.departureTime <= inputTime < self.deliveryTime:
            return 'En route'
        else:  # self.deliveryTime <= inputTime
            return 'Delivered'

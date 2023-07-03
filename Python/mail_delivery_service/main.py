# Author: Tyler Meester
# Student ID: 009476045
# Title: C950 WGUPS MAIL DELIVERY SERVICE

#Importing necessary packages
import csv
import datetime
import Truck
from Package import Package
from HashMap import HashMap

##########################################
########## LOADING CSV FILES #############
##########################################
# Read the distance file
with open("csv_files/Distance.csv") as file1:
    Distance_CSV = csv.reader(file1)
    Distance_CSV = list(Distance_CSV)

# Read the address file
with open("csv_files/Address.csv") as file2:
    Address_CSV = csv.reader(file2)
    Address_CSV = list(Address_CSV)

# Read the package file
with open("csv_files/Package.csv") as file3:
    Package_CSV = csv.reader(file3)
    Package_CSV = list(Package_CSV)
    
    
##########################################
######## DATA LOADING FUNCTIONS ##########
##########################################
def loadPackageData(filename, packageHashMap):
    """ 
    Reads the Package CSV file and saves the data to packageData. 
    Iterates through packageData and creates package objects that 
    are added to the inputed HashMap.

    Args:
        filename (file-like object): The CSV file that is being read
        packageHashMap (HashMap): HashMap to put the package data into
    """
    with open(filename) as packageFile:
        packageData = csv.reader(packageFile)
        for p in packageData:
            packageID = int(p[0])
            packageAddress = p[1]
            packageCity = p[2]
            packageState = p[3]
            packageZipcode = p[4]
            packageDeadline = p[5]
            packageWeight = p[6]
            packageStatus = "At Hub"

            # Package object
            package = Package(packageID, packageAddress, packageCity, packageState, packageZipcode, packageDeadline, packageWeight, packageStatus)

            # Insert data into hash map
            packageHashMap.insert(packageID, package) 


##########################################
###### PACKAGE DELIVERY FUNCTIONS ########
##########################################
def getAddressID(address):
    """
    Gets the ID associated with a given address in the Address_CSV list

    Args:
        address (str): The address for which to find the ID

    Returns:
        int: The ID associated with the given address
    """
    for row in Address_CSV:
        if address in row[2]:
            return int(row[0])
        
def calculateDistance(x, y):
    """
    Calculates the distance between two addresses

    Args:
        x (int): The index of the first address in the Distance_CSV list
        y (int): The index of the second address in the Distance_CSV list

    Returns:
        float: Distance between the two addresses
    """
    distance = Distance_CSV[x][y]
    if distance == '':
        distance = Distance_CSV[y][x]

    return float(distance)


def sortAndDeliver(truck):
    """
    This function is used to "sort and deliver" the packages in the truck. It starts with the current 
    location of the truck and iteratively selects the closest undelivered package and adds it to truck.packages 
    until all packages are 'delivered'. It updates the truck and package parameters accordingly. 
    Though not inputed, it relies on a packageHashMap with all packages to interact with.

    Args:
        truck (Truck object): The truck object which contains properties of the truck 
                              (like its location, delivery packages, mileage, etc).
    """
    
    # Create a list 'undeliveredPackages'. Iterate through the packageHashMap to add relevant data
    undeliveredPackages = [packageHashMap.lookup(packageID) for packageID in truck.packages]

    # Empty the truck's current package list to update it with the new delivery order
    truck.packages = []

    # While there are packages left to deliver, find the closest one and deliver it.
    while undeliveredPackages:
        # Set the minimum distance to a high value (infinity equivalent).
        closestDistance = float('inf')
        # Initialize closest package as None
        closestPackage = None

        # Go through each undelivered package to find the one that is closest.
        for package in undeliveredPackages:
            # Get the ID of the location for the truck and the package.
            truckLocationId = getAddressID(truck.address)
            packageLocationId = getAddressID(package.address)
            
            # Use the calculateDistance function to find the distance between the truck and the package.
            currentDistance = calculateDistance(truckLocationId, packageLocationId)

            # If the current distance is shorter than the current shortest distance, update closestPackage and closestDistance.
            if currentDistance < closestDistance:
                closestDistance = currentDistance
                closestPackage = package

        # Add the closest package to the delivery list.
        truck.packages.append(closestPackage.ID)
        
        # Remove the delivered package from the undelivered list.
        undeliveredPackages.remove(closestPackage)
        
        # Update truck's mileage to include the distance to the closest package.
        truck.mileage += closestDistance

        # The truck is now at the closest package's location.
        truck.address = closestPackage.address
        
        # Update the truck's current time to account for the delivery.
        deliveryTime = datetime.timedelta(hours=closestDistance / truck.speed)
        truck.time += deliveryTime
        
        # Update the delivery time and departure time for the delivered package.
        closestPackage.deliveryTime = truck.time
        closestPackage.departureTime = truck.departureTime
      

##########################################
###### USER INTERFACE FUNCTIONS ##########
##########################################
def timeInputToTimeDelta(timeInput):
    """
    Converts a time input string into a datetime timedelta object.

    Args:
        timeInput (str): Time as a string in the format 'HH:MM'.

    Returns:
        datetime.timedelta: The timedelta object representing the input time.
    """

    # Split the timeInput string into hours and minutes
    hours, minutes = map(int, timeInput.split(':'))
    
    # Return a timedelta object using the hours and minutes
    return datetime.timedelta(hours=hours, minutes=minutes)

def displayAllPackageStatuses(packageHashMap):
    """
    This function displays the delivery status of all packages in the provided hashmap.
    
    Parameters:
        packageHashMap (dict or custom hashmap): A hashmap where the key is the package ID and 
            the value is a Package object.
    
    Returns:
        None: This function does not return a value. It prints the package ID and delivery time
            of each package in the hashmap.
    """
    print("\n--------- Package Delivery Times -----------")
    for item in packageHashMap:
        package = packageHashMap.lookup(item)
        print(f"   Package ID: {package.ID}, Address: {package.address}, Deadline: {package.deadline}, City: {package.city}, Zip Code: {package.zipcode}, Weight: {package.weight}, Delivery Time: {package.deliveryTime}")

def userInterface():
    while True:
        print("\n|-------------------------------------------------------------|")
        print("|----------------- WGUPS Parcel Interface --------------------|")
        print("|-------------------------------------------------------------|")
        print("| 1: Check status of individual package at specific time      |")
        print("| 2: Check status of all packages at specific time            |")
        print("| 3. Check delivery times for all packages                    |")
        print("| 4: Check total mileage of all trucks                        |")
        print("| 5: Exit                                                     |")
        print("|_____________________________________________________________|")
        print("\n")
        choice = input("Select an option: ")
        
        if choice == '1':
            packageID = int(input("Enter the package ID: "))
            timeInput = input("Enter the time (HH:MM): ")
            inputTime = timeInputToTimeDelta(timeInput)
            package = packageHashMap.lookup(packageID)
            if package is not None:
                status = package.statusCheck(inputTime)
                print("\n------------------------------------------")
                print(f"Package ID: {package.ID}")
                print(f"Address: {package.address}")
                print(f"Delivery Deadline: {package.deadline}")
                print(f"City: {package.city}")
                print(f"Zip Code: {package.zipcode}")
                print(f"Weight: {package.weight}")
                print(f"Status at {timeInput}: {status}")
                print("------------------------------------------")

            else:
                print(f"No package found with ID: {packageID}")
                
        
        elif choice == '2':
            timeInput = input("Enter the time (HH:MM): ")
            inputTime = timeInputToTimeDelta(timeInput)
            print(f"\n--------------- {timeInput} DELIVERY STATUS ---------------------")

    		# Initialize an empty list to store the package information
            packageStatuses = []

    		# Iterate over all packages in the hash map
            for packageID in packageHashMap:
        	  # Lookup package
                package = packageHashMap.lookup(packageID)
                if package is not None:
                    status = package.statusCheck(inputTime)
                    # Append package info to list
                    packageStatuses.append(f"Package ID: {package.ID}, Address: {package.address}, Deadline: {package.deadline}, City: {package.city}, Zip Code: {package.zipcode}, Weight: {package.weight}, Status: {status}")
                    

                
            # Define your sorting function
            def sort_status(status):
                # Define the order of status
                status_order = ['Delivered', 'En route', 'At hub']
                # Split the status string to get the actual status
                status_split = status.split(": ")
                # Get the index of the status from the status_order list
                # If status not found in list, give it a high index
                return status_order.index(status_split[-1]) if status_split[-1] in status_order else len(status_order)
                
            # Use the sorted function to sort packageStatuses based on your sorting function
            packageStatuses = sorted(packageStatuses, key=sort_status)
            
            for status in packageStatuses:
                print(status)
    
        elif choice == '3':
            displayAllPackageStatuses(packageHashMap)
            
        elif choice == '4':
            totalMileage = truck1.mileage + truck2.mileage + truck3.mileage
            print("\n-----------------------------------------------")
            print(f"Mileage of delivery truck 1: {round(truck1.mileage, 2)}")
            print(f"Mileage of delivery truck 2: {round(truck2.mileage, 2)}")
            print(f"Mileage of delivery truck 3: {round(truck3.mileage, 2)}")
            print(f"Total mileage traveled by all trucks: {round(totalMileage, 2)} miles.")
            print("-----------------------------------------------")


        elif choice == '5':
            break
        
        else:
            print("Invalid choice. Please enter a valid option.")

  
##########################################
#### CREATE AND LOAD DELIVERY TRUCKS #####
##########################################
# Create truck 1 and manually add the necessary packages
truck1 = Truck.Truck(16, 18, [1, 13, 14, 15, 16, 20, 29, 30, 31, 34, 37, 40], 
                     0.0, "4001 South 700 East", datetime.timedelta(hours=8))

# Create truck 2 and manually add the necessary packages
truck2 = Truck.Truck(16, 18, [2, 4, 5, 6, 7, 8, 10, 11, 25, 28, 32, 33], 
                     0.0, "4001 South 700 East", datetime.timedelta(hours=9, minutes=5))

# Create truck 3 and manually add the necessary packages
truck3 = Truck.Truck(16, 18, [3, 9, 12, 17, 18, 19, 21, 22, 23, 24, 26, 27, 35, 36, 38, 39], 
                     0.0, "4001 South 700 East", datetime.timedelta(hours=10, minutes=20))

# Create the packageHashMap and load all of the package data into it
packageHashMap = HashMap()
loadPackageData("csv_files/Package.csv", packageHashMap)

# Put truck1 and truck2 through the loading process
sortAndDeliver(truck1)
sortAndDeliver(truck2)

# Ensure that truck 3 does not leave until either of the first two trucks finish and package #9 has the correct address

truck3.departureTime = datetime.timedelta(hours=10, minutes=20)

updatedPackage = Package(9, "410 S State St", "Salt Lake City", "UT", "84103", "EOD", "2 Kilos", "At hub")
packageHashMap.update(9, updatedPackage)


#9,300 State St,Salt Lake City,UT,84103,EOD,2 Kilos,'Wrong address listed',,,,,
sortAndDeliver(truck3)

##########################################
###### LAUNCH THE USER INTERFACE #########
##########################################
class Main:
    print("Western Governors University Parcel Service (WGUPS)")
    userInterface()
    
    
    
    
    
    
    
    
    

     
     
     
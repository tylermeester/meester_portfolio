#include "std_lib_facilities.h"

int main(){

    cout << "This program will convert miles to kilometers. Please enter a distance in miles that you would like to convert to kilometers.\n";
    
    //Instantiating necessary variables
    double miles;
    double kilometers;

    //Receiving input and performing conversion calculation
    cin >> miles;
    kilometers = miles * 1.609;
    
    //Printing out the results
    cout << miles << " miles is equal to " << kilometers << " kilometers.\n";





    return 0;
}
#include "std_lib_facilities.h"

/* This program takes an integer as input and determines whether it is odd or even.*/

int main(){

    //Prompting user for number to test
    cout << "Enter a number\n";
    int number;
    cin >> number;

    //Determining whether the inputed number has a non-zero remainder when divided by 2 and printing the result.
    if(number%2 != 0)
        cout << number << " is odd.\n";
        else
            cout << number << " is even.\n";

    return 0;
}
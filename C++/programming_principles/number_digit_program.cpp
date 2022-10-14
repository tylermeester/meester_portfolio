#include "std_lib_facilities.h"

int main(){

    //Prompting the user for a number 
    cout << "Which number would you like to be turned into digits?" << endl;
    string number;
    cin >> number;

    //Outputting the equivalent digit using if/else if statements
    if(number == "zero")
        cout << 0 << endl;
    
    else if(number == "one")
        cout << 1 << endl;

    else if(number == "two")
        cout << 2 << endl;

    else if(number == "three")
        cout << 3 << endl;
    
    else if(number == "four")
        cout << 4 << endl;
        
    else if(number == "five")
        cout << 5 << endl;

    else if(number == "six")
        cout << 6 << endl;
    
    else if(number == "seven")
        cout << 7 << endl;
        
    else if(number == "eight")
        cout << 8 << endl;

    else if(number == "nine")
        cout << 9 << endl;
        
    else
        cout << "Enter a number between zero and nine.\n";

    return 0;
}
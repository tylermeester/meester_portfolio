#include "std_lib_facilities.h"

int main(){

    //Instantiating number variables
    int numberOne;
    int numberTwo;
    int numberThree;
    
    //Inputing first number
    cout << "Please input first number.\n";
    cin >> numberOne;

    //Inputing second number
    cout << "Please input second number.\n";
    cin >> numberTwo;

    //Inputing third number
    cout << "Please input third number.\n";
    cin >> numberThree;

    //Displaying the user inputed numbers
    cout << "You have entered these three numbers to be ordered: " << numberOne << ", " << numberTwo << ", " << numberThree << ".\n";

    //Print ordered integers
    cout << "Here are the ordered (from largest to smallest) numbers: ";

    //Arrange in order
    if(numberOne >= numberTwo && numberOne >= numberThree){
        cout << numberOne << ", ";
        if(numberTwo >= numberThree)
            cout << numberTwo << ", " << numberThree << endl;
            else
            cout << numberThree << ", " << numberTwo << endl;}

    if(numberTwo >= numberOne && numberTwo >= numberThree){
        cout << numberTwo << ", ";
        if(numberOne >= numberThree)
            cout << numberOne << ", " << numberThree << endl;
            else
                cout << numberThree << ", " << numberOne << endl;}

    if(numberThree >= numberOne && numberThree >= numberTwo){
        cout << numberThree << ", ";
        if(numberOne >= numberTwo)
            cout << numberOne << ", " << numberTwo << endl;
            else
                cout << numberTwo << ", " << numberOne << endl;}

    return 0;
}
#include "std_lib_facilities.h"

int main(){

    cout << "Please enter you first name, last name, and age." << endl;
    string firstName;
    string lastName;
    double age;
    cin >> firstName >> lastName >> age;
    cout << "Hello " << firstName << " " << lastName << " (Age " << age << ")!" << endl;

    double ageMonths {age * 12};

    cout << firstName << ", you are " << ageMonths << " months old!" << endl;

    return 0;
}   

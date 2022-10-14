#include "std_lib_facilities.h"

int main(){

    constexpr double cm_per_inch = 2.54;

    double length = 1;

    char unit = 0;

    cout << "Please enter a length followed by a unit (c or i):\n";
    cin >> length >> unit;

    if(unit == 'i')
        cout << length << " inches == " << length*cm_per_inch << " centimeters\n";
    else if (unit == 'c')
        cout << length << " centimeters == " << length/cm_per_inch << " inches\n";
    else
        cout << "Sorry, I do not know a unit called " << unit << endl;



    return 0;
}
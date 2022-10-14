#include "std_lib_facilities.h"

int main(){

    //Instantiating driving age limit at constant integer
    const int drivingAge{16};

    //Prompting the user for their age
    int age{};
    cout << "Please input your age.\n";
    cin >> age;

    //If statement checking if user age is greater than or equal to the driving age
    if(age >= drivingAge)
        cout << "Yes, you can legally drive!\n";

    //If statement checking if the user age is less than the driving age
    else
        cout << "You are not old enough to drive yet! Come back in " << drivingAge - age << " years!\n";
    



    return 0;
}
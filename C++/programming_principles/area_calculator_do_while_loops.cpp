#include "std_lib_facilities.h"

int main(){

    //Instantiating necessary variables
    char selection {};
    double width{}, height{};
    double area{};

    //This do-while loop will perform a calculation and then ask the user if they want to perform another calculation.
    //It will continue to loop until the user inputs no ('N' or 'n')
    do {
        //Prompting user for height and weight dimensions
        cout << "Enter the width and height, seperated by a space.\n";
        cin >> width >> height;

            //If user inputs are non-zero: perform the calculation, output answer, 
            //and prompt user about doing another calculation
            if(width != 0 && height != 0){   
                area = width*height;
                cout << "The area is " << area << endl;
                cout << "Do you want to perform another calculation? (Y/N)" << endl;
                cin >> selection;
                    if(selection == 'N' || selection == 'n')
                    cout << "Have a nice day!" << endl;
            }

            //If the user inputs include a 0: prompt user about trying another calculation
            else if(width == 0 || height == 0){
                cout << "Please enter valid measurements. Do you want to try perform another calculation? (Y/N)" << endl;
                cin >> selection;
                    if(selection == 'N' || selection == 'n')
                        cout << "Have a nice day!" << endl;
            }

        //If the user inputs 'yes' to doing another calculation, 
        //loop back to the beginning and prompt the user for inputs. 
        } while(selection == 'Y' || selection == 'y');
    
    return 0;
}
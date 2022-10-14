#include "std_lib_facilities.h"

int main(){

    /*Shipping Cost Calculator*/

    //Instantiating package dimension variables
    int length{}, height{}, width{};
    
    //Prompting user for variable inputs
    cout << "Please input the width of the package: " << endl;
    cin >> width;

    cout << "Please input the height of the package: " << endl;
    cin >> height;

    cout << "PLease input the length of the package: " << endl;
    cin >> length;

    //Instantiating volume variable now that dimensions have been input
    int volume{length*height*width};

    //Instantiating cost variable with base cost of $2.50
    double cost{2.5};
    
    //If else statements
    if(length <= 10 && height <= 10 && width <= 10){
        if(volume >= 100)
            cost += cost*0.10;
        if(volume >= 500)
            cost += cost*0.25;
        cout << "The package volumne is " << volume << " square inches and will cost $" << cost << " dollars to ship." << endl;
    }
    else
        cout << "Package too large to ship, sorry!" << endl;

    return 0;
}
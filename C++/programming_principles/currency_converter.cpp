#include "std_lib_facilities.h"

int main(){

    //Instantiating variables and conversion rates
    constexpr double yen_per_usd = 0.0086;
    constexpr double kroner_per_usd = 0.11;
    constexpr double pound_per_usd = 1.32; 
    char currency = 0;
    double value = 1;

    //Prompting user for the currency and value they would like converted to USD
    cout << "Please enter the currency (yen = y, kroner = k, pound = p) you would like converted to USD:\n";
    cin >> currency;

    cout << "Please enter how much " << currency << " you would like to convert to USD:\n";
    cin >> value;


    //Switch statements to make conversions and output conversion
    switch(currency){
        case 'y':
            cout << value << " yen == " << value*yen_per_usd << " USD\n";
            break;

        case 'k':
            cout << value << " kroner == " << value*kroner_per_usd << " USD\n";
            break;

        case 'p':
            cout << value << " pounds == " << value*pound_per_usd << " USD\n";
            break;

        default:
            cout << "Sorry, I do not know a currency called " << currency << endl;
            break;
    }


    return 0;
}
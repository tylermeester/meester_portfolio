#include "std_lib_facilities.h"

int main(){

    char letter;
    letter = 'a';

    while(int(letter) < 123){
        cout << letter << "\t" << int(letter) << endl;
        ++ letter;
    }

    return 0;
}
#include "std_lib_facilities.h"
#include <vector>

int main(){

    vector <int> vec{2, 4, 6, 8};
    int result{};

    for(size_t i = 0; i < vec.size(); ++i)
        for(size_t j = i+1; j < vec.size(); ++j)
            result = result + vec.at(i) * vec.at(j);

    cout << result << endl;


    return 0;
}
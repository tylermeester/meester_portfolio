#include <iostream>
#include <string>
#include <typeinfo>
#include <vector>

using namespace std;

void print(const int *const, size_t);
int *apply_all(const int *const, size_t, const int *const, size_t);

/*--------------------------------------------------------------
This function prints an array of a given size
---------------------------------------------------------------*/

void print(const int *const array, size_t size){
    cout << "[ ";
    for(size_t i{0}; i < size; i++)
        cout << array[i] << " ";
    cout << "]";
    cout << endl;
}

/*--------------------------------------------------------------
This function takes two arrays and generates a new array that is
the product of each element in the two arrays. Returning an int of 
a pointer to the array.
---------------------------------------------------------------*/

int *apply_all(const int *const array1, size_t array1_size, const int *const array2, size_t array2_size){
    int *final_array{};
    final_array = new int[array1_size * array2_size];

    int position{0};
    for(size_t i{0}; i < array2_size; i++){
        for(size_t j{0}; j < array1_size; j++){
            final_array[position] = array2[i] * array1[j];
            position++;
        }
    }
    return final_array;
}

int main(){
    
    int array1[]{1,2,3,4,5};
    const size_t array1_size{5};
    print(array1, array1_size);


    int array2[]{10,30,30};
    const size_t array2_size{3};
    print(array2, array2_size);


    int *final_array = apply_all(array1, array1_size, array2, array2_size);
    constexpr size_t final_size{array1_size * array2_size};

    print(final_array, final_size);

    delete [] final_array;

        return 0;
} 
#include "std_lib_facilities.h"

int main(){

    vector <int> vector1{};
    vector <int> vector2{};

    //Vector 1
    vector1.push_back(10);
    vector1.push_back(20);

    cout << "\n Vector 1\n";
    cout << "Size: " << vector1.size() << endl;
    cout << vector1.at(0) << endl;
    cout << vector1.at(1) << endl;


    //Vector 2
    vector2.push_back(100);
    vector2.push_back(200);

    cout << "\n Vector 2\n";
    cout << "Size: " << vector2.size() << endl;
    cout << vector2.at(0) << endl;
    cout << vector2.at(1) << endl;


    //2D-Vector with Vector1 and Vector2
    vector<vector<int>> vector_2d{};
    vector_2d.push_back(vector1);
    vector_2d.push_back(vector2);

    cout << "\n 2d-Vector\n";
    cout << "Size: " << vector_2d.at(0).size() + vector_2d.at(1).size() << endl;
    cout << vector_2d.at(0).at(0) << " " << vector_2d.at(0).at(1) << endl;
    cout << vector_2d.at(1).at(0) << " " << vector_2d.at(0).at(1) << endl;

    //Manipulating original vectors
    vector1.at(0) = 1000;

    //vector1 after manipulation
    cout << "\n Vector 1 after manipulation\n";
    cout << "Size: " << vector1.size() << endl;
    cout << vector1.at(0) << endl;
    cout << vector1.at(1) << endl;


    //2d_vector after manipulation
    cout << "\n 2d-Vector after manipulation\n";
    cout << "Size: " << vector_2d.at(0).size() + vector_2d.at(1).size() << endl;
    cout << vector_2d.at(0).at(0) << " " << vector_2d.at(0).at(1) << endl;
    cout << vector_2d.at(1).at(0) << " " << vector_2d.at(0).at(1) << endl;
    

    return 0;
}
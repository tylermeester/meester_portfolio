#include "std_lib_facilities.h"
#include <vector>
#include <algorithm>
#include <typeinfo>
#include <cmath>

//Function prototypes for diplaying the menu and getting user selection
void display_menu();
char get_selection();

//Menu handling function prototypes
void handle_display(const vector<int> &vec);
void handle_add(vector<int> &vec);
void handle_mean(const vector<int> &vec);
void handle_min(const vector<int> &vec);
void handle_max(const vector<int> &vec);
void handle_quit();
void handle_unknown();

//Additional function prototypes
void display_list(const vector<int> &vec);
double calculate_mean(const vector<int> &vec);
int get_min(const vector<int> &vec);
int get_max(const vector<int> &vec);

int main(){

    vector <int> vec{};
    char selection{};

    do{
        display_menu();
        selection = get_selection();
        switch(selection){
            case 'P':
                handle_display(vec);
                break;
            case 'A':
                handle_add(vec);
                break;
            case 'M':
                handle_mean(vec);
                break;
            case 'S':
                handle_min(vec);
                break;
            case 'L':
                handle_max(vec);
                break;
            case 'Q':
                handle_quit();
                break;
            default:
                handle_unknown();
        } 
    } while(selection != 'Q');
    cout << endl;
    
    return 0;
}

//Function definitions

//Displaying Menu
void display_menu(){
    cout << "\n\n    =================== Menu ===================\n"
            "   |             P - Print Numbers              |\n"
            "   |             A - Add a Number               |\n"
            "   |         M - Calculate Mean Value           |\n"
            "   |       S - Display the Smallest Number      |\n"
            "   |       L - Display the Largest Number       |\n"
            "   |                 Q - Quit                   |\n"
            "   ============================================="
            "\n       Please Select an Item from the Menu\n"
            "   _____________________________________________\n\n";
}

//Getting selection
char get_selection(){
    char selection{};
    cin >> selection;
    return toupper(selection);
}

//Printing the vector of numbers
void handle_display(const vector<int> &vec){        
    if(vec.size() == 0)
        cout << "The list is empty" << endl;
    else
        display_list(vec);
}

//Display list
void display_list(const vector<int> &vec){
    cout << "[ ";
    for(auto num: vec)
        cout << num << " ";
    cout << "]" << endl;
}


//Adding a number to the vector, using pass by reference to change the actual value of vec
void handle_add(vector<int> &vec){
    int num_to_add{};
    cout << "Enter an integer to add to the list: ";
    cin >> num_to_add;
    vec.push_back(num_to_add);
    cout << num_to_add << " added" << endl;
}

//Handling mean
void handle_mean(const vector<int> &vec){
    if(vec.size() == 0)
        cout << "Unable to calculate mean - list is empty" << endl;
    else
        cout << "The mean is " << calculate_mean(vec) << endl;
}

//Calculating mean
double calculate_mean(const vector<int> &vec){
    int total{};
    for(auto num: vec)
        total += num;
    return static_cast<double>(total)/vec.size();
}

//Displaying minimum value
void handle_min(const vector<int> &vec){
    cout << "\nThe minimum value is: " << get_min(vec) << endl;
} 

//Calculating minimum value
int get_min(const vector<int> &vec){
    int min = *min_element(vec.begin(), vec.end());
    return min;
}

//Displaying maximum value
void handle_max(const vector<int> &vec){
    int max = *max_element(vec.begin(), vec.end());
    cout << "\nThe maximum value is: " << max << endl;
}

//Quiting the program
void handle_quit(){
    cout << "Have a nice day!" << endl;
}

void handle_unknown(){
    cout << "Unknown input." << endl;
}
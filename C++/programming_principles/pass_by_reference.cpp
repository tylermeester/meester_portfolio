#include <iostream>
#include <string>
#include <typeinfo>
#include <vector>

using namespace std;

//Function prototypes
string print_guest_list(const string&, const string&, const string&);
void clear_guest_list(string&, string&, string&);
void event_guest_list();

//Function definitions

void event_guest_list(){

    string guest_1{"Larry"};
    string guest_2{"Moe"};
    string guest_3{"Curly"};

    print_guest_list(guest_1, guest_2, guest_3);
    clear_guest_list(guest_1, guest_2, guest_3);
    print_guest_list(guest_1, guest_2, guest_3);
}


string print_guest_list(const string &guest_1, const string &guest_2, const string &guest_3){
    cout << guest_1 << "\n" << guest_2 << "\n" << guest_3 << endl;

    string test_1 = typeid(guest_1).name(), test_2 = typeid(guest_2).name(), test_3 = typeid(guest_3).name();
    return test_1 + test_2 + test_3;}

void clear_guest_list(string &guest_1, string &guest_2, string &guest_3){
    guest_1 = guest_2 = guest_3 = " ";
}

int main(){

    event_guest_list();




        return 0;
}
#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Account{
private:
double balance;

public:
    void set_balance(double bal){
        balance = bal;
    }

    double get_balance(){
        return balance;
    }
};

void Account::set_balance(double bal) {
    balance = bal;
}

double Account::get_balance() {
    return balance;
}



int main(){






    return 0;
}
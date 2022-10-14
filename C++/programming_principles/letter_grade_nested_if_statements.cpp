#include "std_lib_facilities.h"

int main(){

    //Instantiating score variable and prompting user
    int score{};
    cout << "Enter your score on the exam (0-100): \n";
    cin >> score;

    //Instantiating letter grade variable
    char letter_grade{};
    cout << letter_grade;

    //Nested if loops to display letter grade
    if(score >= 0 && score <= 100){
        if(score >= 90)
            letter_grade = 'A';
        else if(score >= 80)
            letter_grade = 'B';
        else if(score >= 70)
            letter_grade = 'C';
        else if(score >= 60)
            letter_grade = 'D';
        else if(score < 60)
            letter_grade = 'F';

        cout << "You received a " << letter_grade << " on the exam!" << endl;
    }

    else
        cout << "Please enter a valid score." << endl;
    


    return 0;
}
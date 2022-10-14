#include "std_lib_facilities.h"

int main(){

    //Instantiating letter to append string inputs to
    string letter;

    //Prompting the user for the name of the recipient of the letter
    cout << "Enter the name of the person you want to write to.\n";
    string firstName;
    cin >> firstName;
    
    //Start of letter, including firstName
    letter = "Dear " + firstName + ",\n" + "\n    How are you? I have been fine, but I am definitely missing you! " + 
            "This last weekend was incredibly fun, I was able to go mountain biking and rock climbing!";
    
    //Prompting user for name of mutual friend
    cout << "Enter the name of a mutual friend.\n";
    string friendName;
    cin >> friendName;

    //Appending letter with friend name
    letter += " Have you seen " + friendName + " recently?";

    //Prompting user for the gender of mutual friend
    cout << "Enter the gender (m/f) of " << friendName << ".\n";
    char friendGender {0};
    cin >> friendGender;

    //If statement to output different sentences based on friendGender
    if (friendGender == 'm')
        letter += " If you see " + friendName + " please have him call me.\n";
    if (friendGender == 'f')
        letter += " If you see " + friendName + " please have her call me.\n";    

  
   //Prompting the user for the age of the recipient of the letter
    cout << "What is the age of " << firstName << "?\n";   
    int age;
    cin >> age;
    letter += "I heard you just had a birthday and that you are " + to_string(age) + " years old, congratulations! ";

    //If-statements to personalize letter about the friend's birthday
    if(age < 0 || age > 110)
        simple_error("You're kidding!");
    if(age > 0 && age < 12)
        letter += "Next year you will be " + to_string(age+1) + " years old. You're growing up so fast!\n";
    if(age == 17)
        letter += "Next year you will be able to vote!\n";
    if(age > 70)
        letter += "I hope you are enjoying retirement!\n";
   
    //Signing letter
    letter += "\n \n Yours sincerely,\n Tyler";

    //Displaying completed letter.
    cout << letter << endl;

    return 0;
}
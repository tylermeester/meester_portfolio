#include <iostream>
#include <string>
#include "roster.h"

using namespace std;

int main(){
    
    //F1. Printing course, language, WGU student id, and name
    cout << "\nC867 - Scripting & Programming: Applications" << endl;
    cout << "Language: C++" << endl;
    cout << "Student ID: 009476045" << endl;
    cout << "Name: Tyler Meester\n" << endl;
    
    //studentData array
    const int number_students = 5;
    const string studentData[] =
        {"A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
         "A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
         "A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
         "A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
         "A5,Tyler,Meester,tmeest1@wgu.edu,28,29,31,20,SOFTWARE"};

    //F2. Creating classRoster Roster object
    Roster classRoster;
    
    //F3. Looping through studentData and applying parse function
    for (int i = 0; i < number_students; i++){
        classRoster.parse(studentData[i]);}
    
    //F4. Calling printAll for classRoster
    cout << "---------- Displaying Students in classRoster ----------" << endl;
    classRoster.printAll();
    cout << '\n';
    
    //F4. Calling printInvalidEmails for classRoster
    cout << "---------- Displaying Invalid Emails ----------" << endl;
    classRoster.printInvalidEmails();
    cout << '\n';
    
    //F4. Calling printAverageDaysInCourse for all students in classRoster
    cout << "---------- Displaying Average Days in Course ----------" << endl;
    for(int i = 0; i < number_students; i++){
        classRoster.printAverageDaysInCourse(classRoster.getStudents()[i] -> getStudentID());}
    cout << '\n';
    
    //F4. Calling printByDegreeProgram for classRoster
    cout << "---------- Displaying Students in SOFTWARE ----------" << endl;
    classRoster.printByDegreeProgram(DegreeProgram::SOFTWARE);
    cout << '\n';
    
    //F5. Calling removeStudent for student with 'A3' student id
    cout << "---------- Removing Student 'A3' From classRoster -----------" << endl;
    classRoster.removeStudent("A3");
    cout << '\n';
    
    //F5. Calling removeStudent for student with 'A3' student id again
    cout << "---------- Removing Student 'A3' From classRoster Again -----------" << endl;
    classRoster.removeStudent("A3");
    cout << '\n';
    
    //F5. Destructors will be called at the end of main.cpp execution

    
    return 0;
}



        

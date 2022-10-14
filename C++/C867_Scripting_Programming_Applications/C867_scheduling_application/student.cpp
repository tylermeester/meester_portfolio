#include <iostream>
#include <string>
#include "student.h"
using namespace std;

/*====================================================================
                    D. Student Class Definitions
=====================================================================*/

/*--------------------------------------------------------------------
D2a. Student Accessor/Getter Method Definitions
--------------------------------------------------------------------*/
string Student::getStudentID(){return this -> student_id;}
string Student::getFirstName(){return this -> first_name;}
string Student::getLastName(){return this -> last_name;}
string Student::getEmailAddress(){return this -> email_address;}
int Student::getAge(){return this -> age;}
const int* Student::getCourseDays(){return this -> course_days;}
DegreeProgram Student::getDegreeProgram(){return this -> degree_program;}


/*--------------------------------------------------------------------
D2b. Student Mutator/Setter Method Definitions
--------------------------------------------------------------------*/
void Student::setStudentID(string student_id){this -> student_id = student_id;}
void Student::setFirstName(string first_name){this -> first_name = first_name;}
void Student::setLastName(string last_name){this -> last_name = last_name;}
void Student::setEmailAddress(string email_address){this -> email_address = email_address;}
void Student::setAge(int age){this -> age = age;}
void Student::setCourseDays(const int course_days[]){
    for (int i = 0; i < num_courses; i++)
        this -> course_days[i] = course_days[i];}
void Student::setDegreeProgram(DegreeProgram degree_program){this -> degree_program = degree_program;}


/*--------------------------------------------------------------------
D2d. Student Constructor Definitions
--------------------------------------------------------------------*/
Student::Student(){
    this -> student_id = "No Student ID";
    this -> first_name = "No First Name";
    this -> last_name = "No Last Name";
    this -> email_address = "No Email Address";
    this -> age = 0;
    for (int i = 0; i < num_courses; i++){
        this -> course_days[i] = 0;}
    this -> degree_program = DegreeProgram::SOFTWARE;}

Student::Student(string student_id, string first_name, string last_name, string email_address, int age,
                int course_days[], DegreeProgram degree_program){
    this -> student_id = student_id;
    this -> first_name = first_name;
    this -> last_name = last_name;
    this -> email_address = email_address;
    this -> age = age;
    for (int i = 0; i < num_courses; i++){
        this -> course_days[i] = course_days[i];}
    this -> degree_program = degree_program;}


/*--------------------------------------------------------------------
D2e. Student Print Method Definition
--------------------------------------------------------------------*/
void Student::print(){
    
    //Creating a DegreeProgram string variable
    string degree_string;
    if(degree_program == DegreeProgram::SECURITY)
        degree_string = "Security";
    else if(degree_program == DegreeProgram::NETWORK)
        degree_string = "Network";
    else if(degree_program == DegreeProgram::SOFTWARE)
        degree_string = "Software";

    //Printing Student Information
    cout << this -> student_id << '\t';
    cout << this -> first_name << '\t';
    cout << this -> last_name << '\t';
    cout << this -> email_address << '\t';
    cout << this -> age << '\t';
    cout << "{" << this -> course_days[0] << ", " <<
                   this -> course_days[1] << ", " <<
                   this -> course_days[2] << "}\t";
    cout << degree_string << '\t' << endl;
}


/*--------------------------------------------------------------------
Destructor Definition
--------------------------------------------------------------------*/
Student::~Student(){}

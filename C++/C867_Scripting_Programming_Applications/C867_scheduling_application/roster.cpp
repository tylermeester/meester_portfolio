#include <iostream>
#include "roster.h"
using namespace std;

/*====================================================================
                    E. Roster Class Definitions
=====================================================================*/
    
/*--------------------------------------------------------------------
Constructor Definition
--------------------------------------------------------------------*/
Roster::Roster(){}
    
    
/*--------------------------------------------------------------------
Getter Definition
--------------------------------------------------------------------*/
Student** Roster::getStudents(){
    return this -> classRosterArray;}


/*--------------------------------------------------------------------
E2a. Parse Method Definition
--------------------------------------------------------------------*/
void Roster::parse(string studentData){
    
    size_t rhs = studentData.find(",");
    string student_id = studentData.substr(0, rhs);

    size_t lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    string first_name = studentData.substr(lhs, rhs-lhs);

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    string last_name = studentData.substr(lhs, rhs-lhs);

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    string email_address = studentData.substr(lhs, rhs-lhs);

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    int age = stoi(studentData.substr(lhs, rhs-lhs));

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    int days_course1 = stoi(studentData.substr(lhs, rhs-lhs));

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    int days_course2 = stoi(studentData.substr(lhs, rhs-lhs));

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    int days_course3 = stoi(studentData.substr(lhs, rhs-lhs));

    lhs = rhs + 1;
    rhs = studentData.find(",", lhs);
    string str_degree_program = studentData.substr(lhs, rhs-lhs);

    DegreeProgram degree_program = DegreeProgram::SOFTWARE;

    if(str_degree_program == "SECURITY")
        degree_program = DegreeProgram::SECURITY;
    

    else if(str_degree_program == "NETWORK")
        degree_program = DegreeProgram::NETWORK;
    

    else if(str_degree_program == "SOFTWARE")
        degree_program = DegreeProgram::SOFTWARE;
        
    Roster::addStudent(student_id, first_name, last_name, email_address, age,
            days_course1, days_course2, days_course3, degree_program);

}


/*--------------------------------------------------------------------
E2b/E3a. Add Student Object Method Definition
--------------------------------------------------------------------*/
void Roster::addStudent(string student_id_val, string first_name_val, string last_name_val, string email_address_val, int age_val,
            int days_course1_val, int days_course2_val, int days_course3_val, DegreeProgram degree_program_val){
                
                int course_days_array[3] = {days_course1_val, days_course2_val, days_course3_val};
                
                classRosterArray[++last_index] = new Student(student_id_val, first_name_val, last_name_val, email_address_val, age_val,
                                                            course_days_array, degree_program_val);
                                            
}


/*--------------------------------------------------------------------
E3b. Remove Student Object Method Definition
--------------------------------------------------------------------*/
void Roster::removeStudent(string student_id){
    bool success = false;
    for (int i = 0; i <= Roster::last_index; i++){
        if (classRosterArray[i] -> getStudentID() == student_id){
            success = true;
            
            Student* temp = classRosterArray[i];
            
            classRosterArray[i] = classRosterArray[num_students - 1];
            
            classRosterArray[num_students - 1] = temp;
            
            Roster::last_index--;
        }
    }
    
    if (success){
        cout << "Student " << student_id << " removed.\n" << endl;
        printAll();
    }
    else
        cout << "Student " << student_id << " not found." << endl;
}


/*--------------------------------------------------------------------
E3c. Print All Student Objects Method Definition
--------------------------------------------------------------------*/
void Roster::printAll(){
    for (int i = 0; i <= Roster::last_index; i++){
        Roster::classRosterArray[i] -> print();
    }
}


/*--------------------------------------------------------------------
E3d. Student Average Days in Course Method Definition
--------------------------------------------------------------------*/
void Roster::printAverageDaysInCourse(string student_id_val){
    for (int i = 0; i <= Roster::last_index; i++){
        if (classRosterArray[i] -> getStudentID() == student_id_val){
            cout << "Student ID: " << student_id_val << ", average days in course is: " <<
                    (classRosterArray[i] -> getCourseDays()[0] +
                    classRosterArray[i] -> getCourseDays()[1] +
                    classRosterArray[i] -> getCourseDays()[2])/3.0 << endl;
        }
    }
}

/*--------------------------------------------------------------------
E3e. Print Invalid Email Method Definition
--------------------------------------------------------------------*/
void Roster::printInvalidEmails(){
    
    for (int i = 0; i <= Roster::last_index; i++){
        
        string email = Roster::getStudents()[i] -> getEmailAddress();
        
        if (email.find('.') == string::npos || email.find('@') == string::npos || email.find(' ') != string::npos){
            cout << email << " is invalid!" << endl;}
    }
}

/*--------------------------------------------------------------------
E3f. Print By Degree Type Method Definition
--------------------------------------------------------------------*/
void Roster::printByDegreeProgram(DegreeProgram degree_program_val){
    for (int i=0; i <= Roster::last_index; i++){
        if (Roster::classRosterArray[i] -> getDegreeProgram() == degree_program_val)
            classRosterArray[i] -> print();
    }
}

/*--------------------------------------------------------------------
F5. Destructor Definition
--------------------------------------------------------------------*/
Roster::~Roster(){
    for (int i = 0; i < num_students; i++){
        //Outputing string to confirm destructor implementation
        cout << "Destructor called for " << classRosterArray[i]->getStudentID() << endl;
        delete classRosterArray[i];
        classRosterArray[i] = nullptr;
    }
}
    
    



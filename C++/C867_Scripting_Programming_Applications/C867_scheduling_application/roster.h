#include <string>
#include "student.h"
using namespace std;

/*====================================================================
                    E. Roster Class Declarations
=====================================================================*/

class Roster {
private:
    //E1. Array of pointers to Student objects
    const static int num_students = 5;
    Student *classRosterArray[num_students];
    
    //last_index will be used by the remove method
    int last_index = -1;
    
public:
    /*--------------------------------------------------------------------
    Constructor Declaration
    --------------------------------------------------------------------*/
    Roster();
    
    
    /*--------------------------------------------------------------------
    Getter Declaration
    --------------------------------------------------------------------*/
    //This will be utilized by the printInvalidEmails method
    Student** getStudents();


    /*--------------------------------------------------------------------
    E2a. Parse Method Declaration
    --------------------------------------------------------------------*/
    void parse(string row);


    /*--------------------------------------------------------------------
    E2b/E3a. Add Student Object Method Declaration
    --------------------------------------------------------------------*/
    void addStudent(string student_id_val, string first_name_val, string last_name_val, string email_address_val, int age_val,
                    int days_course1_val, int days_course2_val, int days_course3_val, DegreeProgram degree_program_val);


    /*--------------------------------------------------------------------
    E3b. Remove Student Object Method Declaration
    --------------------------------------------------------------------*/
    void removeStudent(string student_id);


    /*--------------------------------------------------------------------
    E3c. Print All Student Objects Method Declaration
    --------------------------------------------------------------------*/
    void printAll();
    
    
    /*--------------------------------------------------------------------
    E3d. Student Average Days in Course Method Declaration
    --------------------------------------------------------------------*/
    void printAverageDaysInCourse(string student_id);


    /*--------------------------------------------------------------------
    E3e. Print Invalid Email Method Declaration
    --------------------------------------------------------------------*/
    void printInvalidEmails();


    /*--------------------------------------------------------------------
    E3f. Print By Degree Type Method Declaration
    --------------------------------------------------------------------*/
    void printByDegreeProgram(DegreeProgram degree_program);


   /*--------------------------------------------------------------------
    F5. Destructor Declaration
    --------------------------------------------------------------------*/
    ~Roster();

};

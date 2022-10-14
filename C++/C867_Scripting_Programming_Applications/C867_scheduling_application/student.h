#include <string>
#include "degree.h"
using namespace std;

/*====================================================================
                    D. Student Class Declarations
=====================================================================*/
class Student {
public:
    //This makes it easier for the user to change the number of courses
    const static int num_courses = 3;
    
private:
    //D1. Student Instance Variables
    string student_id;
    string first_name;
    string last_name;
    string email_address;
    int age;
    int course_days[num_courses];
    DegreeProgram degree_program;


public:
    /*--------------------------------------------------------------------
    D2a. Student Accessor/Getter Method Declarations
    --------------------------------------------------------------------*/
    string getStudentID();
    string getFirstName();
    string getLastName();
    string getEmailAddress();
    int getAge();
    const int* getCourseDays();
    DegreeProgram getDegreeProgram();
    
    
    /*--------------------------------------------------------------------
    D2b. Mutator/Setter Method Declarations
    --------------------------------------------------------------------*/
    void setStudentID(string student_id);
    void setFirstName(string first_name);
    void setLastName(string last_name);
    void setEmailAddress(string email);
    void setAge(int age);
    void setCourseDays(const int course_days[]);
    void setDegreeProgram(DegreeProgram degree_program);


    /*--------------------------------------------------------------------
    D2d. Constructor Declaration
    --------------------------------------------------------------------*/
    Student();
    Student(string student_id, string first_name, string last_name, string email_address, int age, int course_days[], DegreeProgram degree_program);

    
    /*--------------------------------------------------------------------
    D2e. Student Print Method Declaration
    --------------------------------------------------------------------*/
    void print();
    
    
    /*--------------------------------------------------------------------
    Destructor Declaration
    --------------------------------------------------------------------*/
    ~Student();

};


    



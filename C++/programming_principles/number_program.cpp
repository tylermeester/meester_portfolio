#include "std_lib_facilities.h"
#include <vector>
#include <algorithm>
#include <typeinfo>


int main(){

    vector <int> vec{};
    char selection{};
 
    while(selection != 'Q' && selection != 'q'){

        //Displaying menu and prompting user for selection
        cout << "\n\n    =================== Menu ===================\n"
                "   |             P - Print Numbers              |\n"
                "   |             A - Add a Number               |\n"
                "   |         M - Calculate Mean Value           |\n"
                "   |      D - Calculate Standard Deviation      |\n"
                "   |       S - Display the Smallest Number      |\n"
                "   |       L - Display the Largest Number       |\n"
                "   |            O - Order The List              |\n"
                "   |              I - Input Check               |\n"
                "   |               C - Clear List               |\n"
                "   |                 Q - Quit                   |\n"
                "   ============================================="
                "\n       Please Select an Item from the Menu\n"
                "   _____________________________________________\n\n";
        cin >> selection;

        //Invalid Selection
        if(selection == 'P'|| selection == 'A'|| selection == 'M'|| selection == 'D' || selection == 'S'|| selection == 'L' || selection == 'I' ||
            selection == 'C'|| selection == 'Q'|| selection == 'p'|| selection == 'a' || selection == 'm'|| selection == 'd' || selection == 's' ||
            selection == 'l'|| selection == 'i'|| selection == 'c'|| selection == 'q'){
        
            //Printing the vector of numbers
            if(selection == 'P' || selection == 'p'){
                if(vec.size() > 0){
                    cout << "\nThe numbers in the list are: ";
                    for(int i{0}; i < vec.size(); i++)
                        if(i == vec.size() - 1)
                            cout << vec.at(i) << "]";
                        else if(i == 0)
                            cout << "[" << vec.at(0) << ", ";
                        else
                            cout << vec.at(i) << ", ";
                }

                else if(vec.size() <= 0)
                    cout << "\nThere are no numbers in list.\n";    
            }

            //Adding a number to the vector 
            if(selection == 'A' || selection == 'a'){
                int num{};
                bool bad = false;
               
                do{
                    cout << "\nPlease enter the number you wish to add to the list.\n";
                    cin >> num;

		            bad = cin.fail();
		            if (bad)
			            cout << "Please enter a valid number." << endl;
		            cin.clear();
		            cin.ignore(10, '\n');

	            }while(bad);
                
                vec.push_back(num);
            
            }

            //Calculating the mean value of the vector
            if(selection == 'M' || selection == 'm'){
                
                cout << fixed << setprecision(2);
                double sum{};
                double mean{}; 

                for(int i{0}; i < vec.size(); i++)
                    sum = sum + vec.at(i); 
                
                mean = sum/vec.size();
                cout << "\nThe average value in the list is: " << mean << endl;
            }

            //Calculating the Standard Deviation of the List
            if(selection == 'D' || selection == 'd'){
                double sum{};
                double mean{};
                vector <double> differences{};
                double standard_deviation{};

                for(int i{0}; i < vec.size(); i++)
                    sum = sum += vec.at(i);

                mean = sum/vec.size();

                for(int i{0}; i < vec.size(); i++)
                    differences.push_back(abs(mean - vec.at(i)));

                sum = 0;

                for(int i{0}; i < differences.size(); i++)
                    sum = sum += differences.at(i);
                    standard_deviation = sum/differences.size();
                
                cout << "\nThe standard deviation is: " << standard_deviation << endl;
            }   



            //Calculating the min value of the vector
            if(selection == 'S' || selection == 's'){
                int min = *min_element(vec.begin(), vec.end());
                cout << "\nThe minimum value is: " << min << endl;
            }

            //Calculating the min value of the vector
            if(selection == 'L' || selection == 'l'){           
                int max = *max_element(vec.begin(), vec.end());
                cout << "\nThe maximum value is: " << max << endl;
            }

            //Calculating number of times a number appears in list
            if(selection == 'I' || selection == 'i'){
                int number{};
                cout << "\nWhich number would you like to check?\n";
                cin >> number;

                int counter{0};
                for(int i{0}; i < vec.size(); i++)
                    if(vec.at(i) == number)
                        counter++;
                        if(counter > 1)
                            cout << "\nThe number " << number << " occurs " << counter << " times in the list.\n";
                        else
                            cout << "\nThe number " << number << " occurs " << counter << " time in the list.\n";
            }

            //Clearing list
            if(selection == 'C' || selection == 'c'){
                vec.clear();
                cout << "\nThe list has been cleared.\n";
            }


            //Exiting program
            if(selection == 'Q'){
                cout << "\nHave a good day!";
            }
        } 
        else
            cout << "\nInput not recognized, please enter a valid character.\n";   
    }
    return 0;
}
package org.example;
import org.example.Course.*;
import org.example.user.*;
import org.example.user.admin.*;
import org.example.user.faculty.FacultyDetails;
import org.example.user.student.StudentDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main() throws IOException, SQLException {
        boolean exit=false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(!exit){
            int user_type=User.getUserType();
            switch(user_type){
                case 1:{
                    String user = User.getId();
                    String password = User.getPassword();
                    String enrol_year = User.find_User(user, password, "admin");
                    String validity = User.IsInvalid(enrol_year);
                    if (validity.equals("Yes")){
                        User.InvalidUser();
                        break;
                    }
                    AdminDetails admin = new AdminDetails(user, enrol_year, password);
                    int admin_on=1;
                    while(admin_on==1){
                        int choice = admin.ChooseFunction();
                        switch (choice){
                            case 1:{
                                Course.display_courses();
                            }break;
                            case 2:{
                                admin.add_course();
                            }break;
                            case 3:{
                                admin.delete_course();
                            }break;
                            case 4:{
                                admin.change_condition();
                            }break;
                            case 5:{
                                admin.generate_transcript();
                            }break;
                            case 6:{
                                admin.view_grades();
                            }break;
                            case 7:{
                                admin.changeYear();
                            }break;
                            case 8:{
                                admin.changeSem();
                            }break;
                            case 9:{
                                admin.checkGraduation();
                            }break;
                            case 10:{
                                admin_on =0;
                            }break;
                        }
                    }
                }break;
                case 2:{
                    String user = User.getId();
                    String pass = User.getPassword();
                    String enrol_year = User.find_User(user, pass, "student");
                    String validity = User.IsInvalid(enrol_year);
                    if (validity.equals("Yes")) {
                        User.InvalidUser();
                        break;
                    }
                    StudentDetails student = new StudentDetails(user, enrol_year, pass);
                    int student_on=1;
                    while(student_on==1){
                        int choice = student.ChooseFunction();
                        switch (choice) {
                            case 1: {
                                float stu_cgpa = student.CGPA_calc(user);
                                student.register(user,stu_cgpa,enrol_year);
                                //register
                            }break;
                            case 2: {
                                student.deregister(user);
                            }break;
                            case 3: {
                                float cgpa = student.CGPA_calc(user);
                                System.out.println("CGPA: " + cgpa);
                            }break;
                            case 4: {
                                student.view_grades(user);
                            }break;
                            case 5: {
                                student_on = 0;
                            }break;
                        }
                    }
                }
                break;
                case 3:{
                    String user = User.getId();
                    String pass = User.getPassword();
                    String enrol_year = User.find_User(user, pass, "faculty");
                    String validity = User.IsInvalid(enrol_year);
                    if (validity.equals("Yes")) {
                        User.InvalidUser();
                        break;
                    }
                    FacultyDetails faculty = new FacultyDetails(user, enrol_year, pass);
                    int faculty_on=1;
                    while(faculty_on==1){
                        int choice = faculty.ChooseFunction();
                        switch (choice) {
                            case 1: {
                                faculty.course_offer(user);
                                //Offer a course
                            }break;
                            case 2: {
                                faculty.uploadCSV();
                                //upload .csv
                            }break;
                            case 3: {
                                faculty.view_grades();
                            }break;
                            case 4: {
                                faculty.deregister();
                            }break;
                            case 5: {
                                faculty_on = 0;
                            }break;
                        }
                    }
                }break;
                case 4:{
                    exit=true;
                }break;
            }
        }
    }
}
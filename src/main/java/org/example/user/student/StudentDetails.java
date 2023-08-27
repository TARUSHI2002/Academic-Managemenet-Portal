package org.example.user.student;

import org.example.Course.Course;
import org.example.user.User;
import org.example.user.admin.AdminDetails;
import org.example.user.database.Delete;
import org.example.user.database.Search;
import org.example.user.faculty.FacultyDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Scanner;
import java.lang.Integer;
public class StudentDetails extends User {
    public StudentDetails(String id, String enrol_year, String password){
        super(id,enrol_year,password);
    }

    public static int ChooseFunction(){
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Hi!!Choose what you want to do?\n1.Register for a course\n2.DeRegister for a course\n3.View Current CGPA\n4.View Grades\n5.Logout");
        int choice = sc.nextInt();
        return choice;
    }

    public static String getId() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Student ID: ");
        String user = br.readLine();
        return user;
    }

    public static int grades_calc(String user) throws SQLException {
        int grades=0;
        String query = "SELECT grade,credits FROM id_"+user+" where status = 'complete' ";
        ResultSet rs = Search.find(query);
        while (rs.next()) {
            String found = rs.getString("grade");
            int credits = rs.getInt("credits");
            if(found.equals("10")){
                grades=grades+(10*credits);
            }
            else if(found.equals("9")){
                grades=grades+(9*credits);
            }
            else if(found.equals("8")){
                grades=grades+(8*credits);
            }
            else if(found.equals("7")){
                grades=grades+(7*credits);
            }
            else if(found.equals("6")){
                grades=grades+(6*credits);
            }
            else if(found.equals("5")){
                grades=grades+(5*credits);
            }
            else if(found.equals("4")){
                grades=grades+(4*credits);
            }
            else{
                grades=grades+(3*credits);
            }
        }
        return grades;
    }

    public static int credits_calc(String user) throws SQLException {
        int credits=0;
        ResultSet rs =  Search.find("SELECT * FROM id_"+user+" where status='complete'");
        while (rs.next()) {
            credits=credits+rs.getInt("credits");
        }
        return credits;
    }

    public static float CGPA_calc(String user) throws SQLException {
        int grades=grades_calc(user);
        int credits=credits_calc(user);
        if(credits==0) return 0;
        float cgpa= (float) grades/credits;
        return cgpa;
    }

    public static int view_grades(String user) throws SQLException {
        int count=0;
        ResultSet rs = Search.find("SELECT * FROM id_"+user+" where status='complete'");
        Formatter fmt = new Formatter();
        fmt.format("%15s %15s\n","Grade", "Course");
        while (rs.next()) {
            fmt.format("%15s %15s\n",rs.getString("grade"), rs.getString("course_code"));
            ++count;}
        System.out.println(fmt);
        return count;
    }
    public static int deregister(String user) throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course = Course.getCourseCode(br);
        String curr_year = AdminDetails.getCurrYear();
        int allowed = AdminDetails.getCurrCond();
        if(allowed == 2){
            System.out.println("Not allowed");
            return 0;}
        String query1 = "DELETE FROM c_"+course+"_"+curr_year+" where id='"+user+"'";
        String query2 = "DELETE FROM id_"+user+" WHERE course_code='"+course+"'";
        Delete.delete_course_faculty(query1,query2);
        return 1;}
    public static int prev2cred(String user,String curr_year, int curr_sem,String enrol_year) throws SQLException {
        int credits=0;
        int curr=Integer.parseInt(curr_year);
        int enrol=Integer.parseInt(enrol_year);
        int year=curr-enrol;
        if(curr_sem==1){
            String query = "SELECT * FROM id_"+user+" WHERE year = "+year;
            ResultSet rs = Search.find(query);
            while(rs.next()){
                credits=credits+rs.getInt("credits");
            }
            return credits;
        }
        else{
            String query = "SELECT * FROM id_"+user+" WHERE year = "+year+"AND sem=2";
            ResultSet rs = Search.find(query);
            while (rs.next()) {
                credits=credits+rs.getInt("credits");
            }
            year = year+1;
            String query2 = "SELECT * FROM id_"+user+" WHERE year = "+year+"AND sem=1";
            ResultSet rs2 = Search.find(query2);
            while(rs2.next()){
                credits=credits+rs2.getInt("credits");
            }
            return credits;
        }
    }

    public static int calculatecredits(int course_year, int curr_sem,String user) throws SQLException {
        int credits=0;
        String query = "SELECT * FROM id_"+user+" WHERE year = "+course_year+"AND sem= "+curr_sem;
        ResultSet rs = Search.find(query);
        while (rs.next()) {
            credits=credits+rs.getInt("credits");
        }
        return credits;
    }

    public static boolean passRequisite(String course_code, String user) throws SQLException {
        String query = "SELECT * FROM id_"+user+" where course_code = '"+course_code+"' AND status = 'complete'";
        ResultSet rs = Search.find(query);
        if(rs.next())return true;
        else return false;
    }
    public static int register(String user,Float stu_cgpa,String enrol_year) throws IOException, SQLException {

        int allowed = AdminDetails.getCurrCond();
        if(allowed==2){
            System.out.println("Not allowed");
            return 0;
        }
        //get course code
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course = Course.getCourseCode(br);

        //cgpa criteria match
        float faculty_cgpa = FacultyDetails.getcgpa(course, AdminDetails.getCurrYear());
        boolean isCGPAllowed = Course.IsCgpaAllow(stu_cgpa, faculty_cgpa);
        System.out.println(isCGPAllowed);
        if(isCGPAllowed==false) {System.out.println("Not allowed");return 0;}


        //enrol_year
        int e_year = Integer.parseInt(enrol_year);
        //curr_year
        int curr_year = Integer.parseInt(AdminDetails.getCurrYear());
        //year=curr_year-enrol_year+1
        int year = curr_year - e_year + 1;
        //year==course_catalog(year)
        int course_year = Course.getCourseYear(course);
        boolean isYearAllowed = Course.IsYearAllow(year, course_year);
        System.out.println(isYearAllowed);
        if(isYearAllowed==false) {System.out.println("Not allowed");return 0;}
        //sem
        int curr_sem = AdminDetails.getCurrSem();
        int allowed_sem = Course.getCourseSem(course);
        boolean isSemAllowed = Course.IsSemAllow(curr_sem, allowed_sem);
        System.out.println(isSemAllowed);
        if(isSemAllowed==false) {System.out.println("Not allowed");return 0;}
        //branch which of student
        String type = null;
        String stu_type = user.substring(4, 6);
        System.out.println(stu_type);
        //check branch type in course catalog-core
        String core = Course.GetCore(course);
        //elective
        String electives = Course.GetElectives(course);
        //check stu_type in core/elec
        Scanner scan = new Scanner(core);
        scan.useDelimiter(",");
        while (scan.hasNext()) {
            if (scan.next().equals(stu_type)) {
                type = "core";
            }
        }
        scan = new Scanner(electives);
        scan.useDelimiter(",");
        while (scan.hasNext()) {
            if (scan.next().equals(stu_type)) {
                type = "elective";
            }
        }
        System.out.println(type);
        if(type==null){
            System.out.println("Not allowed");
            return 0;
        }
        double credits_possible = 1.25 * ((StudentDetails.prev2cred(user, AdminDetails.getCurrYear(), curr_sem, enrol_year)) / 2);
        System.out.println(credits_possible);
        int credits_now = StudentDetails.calculatecredits(course_year, curr_sem, user);
        System.out.println(credits_now);
        boolean isCreditsAllowed = Course.IsCreditsAllow(credits_possible, credits_now + Course.findCredits(course));
        if(year==1)isCreditsAllowed=true;
        System.out.println(isCreditsAllowed);
        if(isCreditsAllowed==false){
            System.out.println("Not Allowed");
            return 0;
        }
        //clearing pre-requisites
        String pre_req="";
        pre_req = Course.getRequisite(course, year);
        System.out.println(pre_req);
        boolean clear = true;
        if (!pre_req.equals("")) {
            scan = new Scanner(pre_req);
            scan.useDelimiter(",");
//            System.out.println(scan.next());
            while (scan.hasNext()) {
                if (!StudentDetails.passRequisite(scan.next(), user)) clear = false;
//                System.out.println(scan.next());
            }
        }
        boolean isRegisterAllowed = Course.isRegisterAllow(isCGPAllowed, isYearAllowed, isSemAllowed, isCreditsAllowed, clear);
        System.out.println(isRegisterAllowed);
        if (isRegisterAllowed) {
            int credits = Course.findCredits(course);
            Course.register(course, AdminDetails.getCurrYear(), user, year, curr_sem, credits, type);
            return 1;
        }
        System.out.println("Don't match the criteria!!");
        return 0;
    }
}

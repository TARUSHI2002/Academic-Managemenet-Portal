package org.example.user.admin;

import org.example.Course.Course;
import org.example.user.User;
import org.example.user.database.Delete;
import org.example.user.database.Insert;
import org.example.user.database.Search;
import org.example.user.database.Update;
import org.example.user.student.StudentDetails;

import java.io.*;
import java.sql.*;
import java.util.Formatter;
import java.util.Scanner;
import java.lang.Integer;
public class AdminDetails extends User {

    public AdminDetails(String id, String enrol_year, String password){
        super(id,enrol_year,password);
    }

    public static int ChooseFunction(){
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Hi!!Choose what you want to do?\n1.View courses in course catalog\n2.Add courses in course catalog\n3.Delete courses from course catalog\n4.Change the course add/drop condition\n5.Generate Transcript\n6.View Grades of all Students\n7.Change current year\n8.Change current sem\n9.Check Graduation Criteria\n10.Logout");
        int choice = sc.nextInt();
        return choice;
    }

    public static int add_course() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course_code = Course.getCourseCode(br);
        String ltp = Course.getLTP(br);
        String pre_req = Course.getPreRequisite(br);
        String core = Course.getCore(br);
        String elec = Course.getElective(br);
        int year = Course.getYear(br);
        int sem = Course.getSem(br);
        String query1 = "INSERT into course_catalog VALUES('" + course_code + "','" + ltp + "','" + pre_req + "','" + core + "','" + elec + "',"+year+","+sem+")";
        return Insert.add_course(query1);
    }

    public static int delete_course() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course_code = Course.getCourseCode(br);
        int year = Course.getYear(br);
        String query1 = "SELECT * FROM course_catalog where course_code='"+course_code+"' AND year = "+year;
        String query2 = "DELETE FROM course_catalog WHERE course_code='"+course_code+"' AND year="+year;
        return Delete.delete_course_admin(query1,query2);
    }

    public static int getcond(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Press 1 to allow add/drop and 2 to not allow");
        int choice = sc.nextInt();
        return choice;
    }
    public static int change_condition(){
        int audit = getcond();
        if(audit!=1 && audit!=2){System.out.println("Invalid Value");return 0;}
        String query = "UPDATE current SET audit=" + audit;
        return Update.change_condition(query);
    }


    public static int view_grades() throws SQLException {
        int count=0;
        String query = "SELECT * FROM grades_info";
        ResultSet rs = Search.find(query);
        Formatter fmt = new Formatter();
        fmt.format("%15s %15s %15s\n", "Student","Grade", "Course");
        while (rs.next()) {
            fmt.format("%15s %15s %15s\n", rs.getString("student"),rs.getString("grade"), rs.getString("course"));
            ++count;
        }
        System.out.println(fmt);
        return count;
    }

    public static String getYear() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Set the year as: ");
        String year = br.readLine();
        return year;
    }

    public static int changeYear() throws IOException {
        String year = getYear();
        String query = "UPDATE current SET curr_year='" + year+"'";
        return Update.change_condition(query);
    }

    public static int getSem(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Set the current sem as: ");
        int sem = sc.nextInt();
        return sem;
    }

    public static int changeSem(){
        int sem = getSem();
        if(sem!=1 && sem!=2){System.out.println("Invalid Value");return 0;}
        String query = "UPDATE current SET curr_sem=" + sem;
        return Update.change_condition(query);
    }

    public static int generate_transcript() throws IOException {
        String Student_id = StudentDetails.getId();
        String query1= "SELECT course_code, credits, year, sem, grade FROM id_"+Student_id;
        ResultSet rs = Search.find(query1);
        try{
            File file = new File(Student_id+".txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append("Student id: "+Student_id+"\n");
            bw.append("Student Enrollment Year: "+Search.find("SELECT * FROM auth where id = '"+Student_id+"'","enrol_year")+"\n");
            bw.append("CGPA: "+StudentDetails.CGPA_calc(Student_id)+"\n");
            bw.append("Credits earned: "+StudentDetails.credits_calc(Student_id)+"\n");
            while(rs.next()){
                String course_code  = rs.getString("course_code");
                int credits = rs.getInt("credits");
                int year = rs.getInt("year");
                int sem = rs.getInt("sem");
                String grade = rs.getString("grade");
                bw.append("Course code: "+course_code+",Credits earned: "+credits+",Year: "+year+",Semester: "+sem+",Grade Earned: "+grade+"\n");
            }
            rs.close();
            bw.close();
            return 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrYear() throws SQLException {
        ResultSet rs = Search.find("SELECT * from current");
        rs.next();
        return rs.getString("curr_year");
    }

    public static int getCurrSem() throws SQLException {
        ResultSet rs = Search.find("SELECT * from current");
        rs.next();
        return rs.getInt("curr_sem");
    }

    public static int getCurrCond() throws SQLException {
        ResultSet rs = Search.find("SELECT * from current");
        rs.next();
        return rs.getInt("audit");
    }

    public static int checkGraduation() throws IOException, SQLException {
        String Student_id = StudentDetails.getId();
        Integer EarnedCore = Course.earnedCore(Student_id);
        Integer EarnedElective = Course.earnedElective(Student_id);
        if(EarnedCore.equals(15) && EarnedElective.equals(9)){
            return 1;
        }
        return 0;
    }

}

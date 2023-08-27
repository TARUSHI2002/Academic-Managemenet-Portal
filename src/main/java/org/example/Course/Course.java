package org.example.Course;

import org.example.user.database.Insert;
import org.example.user.database.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Course {
    public String courseCode;
    public String LTP;
    public String pre_requisite;
    public String core;
    public String Elec;
    public String Year;

    public static String getCourseCode(BufferedReader br) throws IOException {
        System.out.print("Course Code: ");
        String course_code = br.readLine();
        return course_code;

    }

    public static String getLTP(BufferedReader br) throws IOException {
        System.out.print("L-T-P: ");
        String LTP = br.readLine();
        return LTP;
    }

    public static String getPreRequisite(BufferedReader br) throws IOException {
        System.out.print("Pre Requisite: ");
        String pre = br.readLine();
        return pre;
    }

    public static String getCore(BufferedReader br) throws IOException {
        System.out.print("Core Branches: ");
        String core = br.readLine();
        return core;
    }

    public static String getElective(BufferedReader br) throws IOException {
        System.out.print("Elective Branches: ");
        String elec = br.readLine();
        return elec;
    }

    public static int getYear(BufferedReader br) throws IOException {
        System.out.print("Enter the year(1st/2nd/3rd/4th) for which course should be offered: ");
        String year = br.readLine();
        return Integer.parseInt(year);
    }

    public static int getSem(BufferedReader br) throws IOException {
        System.out.print("Enter the sem(1st/2nd) in which course should be offered: ");
        String sem = br.readLine();
        return Integer.parseInt(sem);
    }

    public static int display_courses() throws SQLException, IOException {
        int count=0;
        String query = "SELECT * FROM course_catalog";
        ResultSet rs = Search.find(query);
        Formatter fmt = new Formatter();
        fmt.format("%15s %15s %15s %15s %15s %15s %15s\n", "Course Code", "L-T-P", "PreRequisite", "Core Branch",
                "Elective Branches","Year","Sem");
        while (rs.next()) {
            fmt.format("%15s %15s %15s %15s %15s %15s %15s\n", rs.getString("course_code"), rs.getString("ltp"),
                    rs.getString("pre_requisite"), rs.getString("core"), rs.getString("branch"),rs.getString("year"),rs.getString("sem"));
        ++count;
        }
        System.out.println(fmt);
        return count;
    }

    public static boolean getCourse(String course_code,String year) throws SQLException {
        ResultSet rs1 = Search.find("SELECT * FROM course_catalog where course_code = '"+course_code+"'");
        boolean rt1 = rs1.next();
        ResultSet rs2 = Search.find("SELECT * FROM courses_offered where course_code = '"+course_code+"' AND year= '"+year+"'");
        boolean rt2=rs2.next();
        if(rt1==true && rt2==false)return true ;
        else return false;
    }

    public static float getCGPA(BufferedReader br) throws IOException {
        System.out.println("Enter the CGPA criteria: ");
        String cgpa = br.readLine();
        return Float.parseFloat(cgpa);
    }

    public static boolean IsCgpaAllow(float stu_cgpa,float faculty_cgpa){
        if(stu_cgpa>=faculty_cgpa) return true;
        else return false;
    }

    public static int getCourseYear(String course_code) throws SQLException {
        String query = "SELECT * FROM course_catalog where course_code='"+course_code+"'";
        ResultSet rs = Search.find(query);
        rs.next();
        return rs.getInt("year");
    }

    public static boolean IsYearAllow(int year, int course_year) {
        if (year == course_year) return true;
        else return false;
    }

    public static int getCourseSem(String course_code) throws SQLException {
        String query = "SELECT * FROM course_catalog where course_code='"+course_code+"'";
        ResultSet rs = Search.find(query);
        rs.next();
        return rs.getInt("sem");
    }

    public static boolean IsSemAllow(int curr_sem,int allowed_sem){
        if (curr_sem == allowed_sem) return true;
        return false;
    }

    public static String GetCore(String course_code) throws SQLException {
        String query = "SELECT * FROM course_catalog where course_code='"+course_code+"'";
        ResultSet rs = Search.find(query);
        rs.next();
        return rs.getString("core");
    }

    public static String GetElectives(String course_code) throws SQLException {
        String query = "SELECT * FROM course_catalog where course_code='"+course_code+"'";
        ResultSet rs = Search.find(query);
        rs.next();
        return rs.getString("branch");
    }

    public static boolean IsCreditsAllow(double credits_possible,int credits_now){
        if(credits_now<=credits_possible)return true;
        return false;
    }

    public static  String getRequisite(String course_code, int year) throws SQLException {
        String query = "SELECT pre_requisite FROM course_catalog where course_code = '"+course_code+"' AND year = "+year;
        ResultSet rs = Search.find(query);
        rs.next();
        System.out.println(rs.getString("pre_requisite"));
        return rs.getString("pre_requisite");
    }

    public static boolean isRegisterAllow(boolean isCGPAllowed,boolean isYearAllowed,boolean isSemAllowed,boolean isCreditsAllowed, boolean choice){
        if(isCGPAllowed && isYearAllowed && isSemAllowed && isCreditsAllowed && choice) return true;
        return false;
    }

    public static int findCredits(String course) throws SQLException {
        String query = "SELECT * FROM course_catalog where course_code='"+course+"'";
        ResultSet rs =Search.find(query);
        rs.next();
        String ltp = rs.getString("ltp");
        Scanner scan = new Scanner(ltp);
        scan.useDelimiter("-");
        int l = Integer.parseInt(scan.next());
        scan.next();
        int p=Integer.parseInt(scan.next());
        return (l+p)/2;
    }

    public static void register(String course_code,String curr_year,String user, int year,int sem,int credits,String type){
        String query = "INSERT into c_"+course_code+"_"+curr_year+" VALUES('"+user+"','','enrolled')";
        Insert.add_course(query);
        String query2 = "INSERT into id_"+user+" VALUES('"+course_code+"',"+year+","+sem+",'',"+credits+",'enrolled','"+type+"')";
        Insert.add_course(query2);
    }

    public static int earnedCore(String id) throws SQLException {
        int credits=0;
        ResultSet rs =  Search.find("SELECT * FROM id_"+id+" where status='complete' and type='core'");
        while (rs.next()) {
            credits=credits+rs.getInt("credits");
        }
        return credits;
    }

    public static int earnedElective(String id) throws SQLException {
        int credits=0;
        ResultSet rs =  Search.find("SELECT * FROM id_"+id+" where status='complete' and type = 'elective'");
        while (rs.next()) {
            credits=credits+rs.getInt("credits");
        }
        return credits;
    }


}

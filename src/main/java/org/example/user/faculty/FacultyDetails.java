package org.example.user.faculty;

import com.opencsv.CSVReader;

import org.example.Course.Course;
import org.example.user.User;
import org.example.user.admin.AdminDetails;
import org.example.user.database.Delete;
import org.example.user.database.Insert;
import org.example.user.database.Search;
import org.example.user.database.Update;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Scanner;

public class FacultyDetails extends User {
    public FacultyDetails(String id, String enrol_year, String password){
        super(id,enrol_year,password);
    }

    public static int ChooseFunction() {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Choose what you want to do?\n1.Offer a course\n2. Upload .csv file containing grades of all students of your course\n3.View grades of all students\n4.Remove a Course Offered\n5.Logout\n");
        int choice = sc.nextInt();
        return choice;
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

    public static int deregister() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course_code = Course.getCourseCode(br);
        String year = AdminDetails.getCurrYear();
        int allowed = AdminDetails.getCurrCond();
        if(allowed == 2){
            System.out.println("Not allowed");
            return 0;
        }
        String query1 = "SELECT id from c_"+course_code+"_"+year;
        ResultSet rs = Search.find(query1);
        while(rs.next()){
            Delete.delete("DELETE from id_"+rs.getString("id")+" where course_code = '"+course_code+"'");
        }
        String query4 = "SELECT * FROM courses_offered WHERE course_code = '"+course_code+"' AND year = '"+year+"'";
        rs = Search.find(query4);
        if(rs.next()==false){
            System.out.println("Course Not offered!!");
            return 0;
        }
        String query2 = "DROP TABLE c_"+course_code+"_"+year;
        String query3 = "DELETE FROM courses_offered WHERE course_code = '"+course_code+"' AND year = '"+year+"'";
       return Delete.delete_course_faculty(query2,query3);
    }

    public static String getPath(BufferedReader br) throws IOException {
        System.out.print("File Path: ");
        String path = br.readLine();
        return path;
    }

    public static String getCourse(BufferedReader br) throws IOException {
        System.out.print("Course: ");
        String course = br.readLine();
        return course;
    }

    public static int uploadCSV() throws SQLException, IOException {
        int count=0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course = getCourse(br);
        String year = AdminDetails.getCurrYear();
        String path = getPath(br);
        FileReader filereader = new FileReader(path);
        try {
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                String id = nextRecord[0];
                String grade = nextRecord[1];
                System.out.println(id);
                System.out.println(grade);
                String query1 = "UPDATE c_"+course+"_"+year+" SET grade = '"+ grade+"',status = 'complete' WHERE id = '"+id+"'";
                String query2 = "UPDATE id_"+id+" SET grade= '" + grade + "',status = 'complete' WHERE course_code = '" + course + "'";
                String query3 = "INSERT INTO grades_info VALUES('"+id+"','"+grade+"','"+course+"')";
                Update.change_condition(query1);
                Update.change_condition(query2);
                Insert.add_course(query3);
                ++count;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int course_offer(String user) throws SQLException, IOException {
        int allowed = AdminDetails.getCurrCond();
        if(allowed == 2){
            System.out.println("Not allowed");
            return 0;
        }
        Course.display_courses();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String course_code = Course.getCourseCode(br);
        boolean isValid = Course.getCourse(course_code,AdminDetails.getCurrYear());
        float cgpa = Course.getCGPA(br);
        String year = AdminDetails.getCurrYear();
        int sem = AdminDetails.getCurrSem();
        if(isValid){
           Insert.add_course("INSERT INTO courses_offered VALUES('"+course_code+"',"+cgpa+",'"+year+"',"+sem+",'"+user+"')");
           Update.change_condition("CREATE table c_"+course_code+"_"+year+"(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT course_info_"+user+"_"+course_code+"_"+year+"_"+sem+" PRIMARY KEY(id));");
           return 1;
        }
        System.out.println("Course not in course Catalog/Already a faculty offered it.");
        return 0;
    }


    public static float getcgpa(String course, String curr_year) throws SQLException {
       String query = "SELECT * FROM courses_offered WHERE course_code='"+course+"' AND year='"+curr_year+"'";
       ResultSet rs = Search.find(query);
       rs.next();
       return rs.getFloat("cgpa");
    }

}

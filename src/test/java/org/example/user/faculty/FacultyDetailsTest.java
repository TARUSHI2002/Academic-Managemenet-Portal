package org.example.user.faculty;

import org.example.user.admin.AdminDetails;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class FacultyDetailsTest {

    Statement CreateConnection(Connection c) {

        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void FacultyCreate(){
        var faculty = new FacultyDetails("F1","2008","23456");
    }

    @Test
    void ChooseFunction(){
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.ChooseFunction();
        assertEquals(ret,1);
    }

    @Test
    void CheckViewGrades() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into grades_info VALUES('2020CEB1135',9,'CE401');");
        int cntprev = FacultyDetails.view_grades();
        stmt.executeUpdate("DELETE from grades_info where course= 'CE401' AND  student= '2020CEB1135' ");
        int cntnow = FacultyDetails.view_grades();
        assertEquals(cntprev,1+cntnow);
    }
    //delete cs101 if present
    @Test
    void CheckOfferCourseYes() throws SQLException, IOException {
        //audit=1
        //course_catalog mei add
        //year=2020
        //courses_offered mei nhi hona chahiye
        //sem=1
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        stmt.executeUpdate("UPDATE current SET audit = 1");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");

        String input = "CS401\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.course_offer("F1");
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' AND year = 4");
        stmt.executeUpdate("DROP table c_cs401_2020");
        stmt.executeUpdate("DELETE from courses_offered WHERE course_code='CS401' AND year = '2020'");
    }

//add cs101 if not present
    @Test
    void CheckOfferCourseNo() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        stmt.executeUpdate("UPDATE current SET audit = 1");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',0,'2020',1,'F10');");

        String input = "CS401\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.course_offer("F10");
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' AND year = 4");
        stmt.executeUpdate("DELETE from courses_offered WHERE course_code='CS401' AND year = '2020'");
    }

//change audit condition to 2
    @Test
    void CheckOfferCourseNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        stmt.executeUpdate("UPDATE current SET audit = 2");
        String input = "CS401\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.course_offer("F1");
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
    }

    @Test
    void CheckGetCgpa() throws SQLException {
        //insert courses_offered
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',8,'2020',1,'F10');");
        Float cgpa = FacultyDetails.getcgpa("CS401","2020");
        assertEquals(cgpa,8);
        stmt.executeUpdate("DELETE from courses_offered WHERE course_code='CS401' AND year = '2020'");

        //delete from courses_offerd
    }

//    @Test
//    void CheckGetCourse() throws IOException {
//        String input = "CS101\n";
//        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(out);
//        System.setOut(ps);
//
//        String ret = FacultyDetails.getCourse();
//        assertEquals(ret,"CS101");
//    }
//add/drop not allowed
    @Test
    void CheckDeregisterNegativeNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        stmt.executeUpdate("UPDATE current SET audit = 2");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.deregister();
        assertEquals(ret,0);
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' AND year = 4");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
    }

    //delete
    @Test
    void CheckDeregisterPositive() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET audit = 1");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
//        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141 PRIMARY KEY(course_code)" +
                ");");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',0,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',1,1,'',3,'enrolled','core')");
        //audit-me
        //set year-me
        //course_catalog-nn
        //create c_course_year-code
        //create id_id-code
        //courses_offered-code
        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.deregister();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("DROP table id_2020csb1141");
    }

    @Test
    void CheckDeregisterPositiveValues() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET audit = 1");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
//        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',0,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into c_cs401_2020 VALUES('2020CSB1141','','enrolled')");

        //audit-me
        //set year-me
        //course_catalog-nn
        //create c_course_year-code
        //create id_id-code
        //courses_offered-code
        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.deregister();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("DROP table id_2020csb1141");
    }

    //not exist course
    @Test
    void CheckDeregisterNegativeCourseNotFound() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET audit = 1");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
//        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141 PRIMARY KEY(course_code)" +
                ");");
        stmt.executeUpdate("INSERT into id_2020CSB1141 VALUES('CS401',1,1,'',3,'enrolled','core')");
//        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',0,'2020',1,'F10');");
        //audit-me
        //set year-me
        //course_catalog-nn
        //create c_course_year-code
        //create id_id-code
        //courses_offered-code
        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.deregister();
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");
        stmt.executeUpdate("DELETE from courses_offered WHERE course_code='CS401' AND year = '2020'");
    }

    @Test
    void CheckUploadCSV() throws SQLException, IOException {

        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");

        stmt.executeUpdate("Create table id_2020meb1135(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020meb1135 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("Create table id_2020meb1136(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020meb1136 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("Create table id_2020meb1137(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020meb1137 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("Create table id_2020meb1138(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020meb1138 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("Create table id_2020meb1139(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020meb1139 PRIMARY KEY(course_code)" +
                ");");

        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1136 VALUES('ME401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1137 VALUES('ME401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1138 VALUES('ME401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1139 VALUES('ME401',1,1,'',3,'enrolled','core')");


        stmt.executeUpdate("CREATE table c_me401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_me401_2020 PRIMARY KEY(id));");
        stmt.executeUpdate("INSERT into c_me401_2020 VALUES('2020meb1135','','enrolled')");
        stmt.executeUpdate("INSERT into c_me401_2020 VALUES('2020meb1136','','enrolled')");
        stmt.executeUpdate("INSERT into c_me401_2020 VALUES('2020meb1137','','enrolled')");
        stmt.executeUpdate("INSERT into c_me401_2020 VALUES('2020meb1138','','enrolled')");
        stmt.executeUpdate("INSERT into c_me401_2020 VALUES('2020meb1139','','enrolled')");

        //string - course,path
        //grades_info-delete krni
        //id_ - 5 different
        //c_course_year -1
        //year
        String input = "ME401\nC:\\Users\\TARUSHI\\Downloads\\F4_2020_MA101 - Sheet1.csv\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = FacultyDetails.uploadCSV();
        assertEquals(ret,5);
        //delete grades_info

        stmt.executeUpdate("DELETE from grades_info WHERE course='ME401' AND student = '2020MEB1135'");
        stmt.executeUpdate("DELETE from grades_info WHERE course='ME401' AND student = '2020MEB1136'");
        stmt.executeUpdate("DELETE from grades_info WHERE course='ME401' AND student = '2020MEB1137'");
        stmt.executeUpdate("DELETE from grades_info WHERE course='ME401' AND student = '2020MEB1138'");
        stmt.executeUpdate("DELETE from grades_info WHERE course='ME401' AND student = '2020MEB1139'");

        //change year
        //drop tables
        stmt.executeUpdate("DROP table id_2020meb1135");
        stmt.executeUpdate("DROP table id_2020meb1136");
        stmt.executeUpdate("DROP table id_2020meb1137");
        stmt.executeUpdate("DROP table id_2020meb1138");
        stmt.executeUpdate("DROP table id_2020meb1139");
        stmt.executeUpdate("DROP table c_me401_2020");
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");

    }


}
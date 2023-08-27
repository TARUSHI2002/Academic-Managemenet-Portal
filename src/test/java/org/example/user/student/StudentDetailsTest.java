package org.example.user.student;

import org.example.user.User;
import org.example.user.admin.AdminDetails;
import org.example.user.faculty.FacultyDetails;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentDetailsTest {

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
    void Student(){
        var student = new StudentDetails("2020CSB1136","2020","23456");
    }

    @Test
    void ChooseFunction(){
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.ChooseFunction();
        assertEquals(ret,1);
    }

    @Test
    void CheckGetId() throws IOException {
        String input = "2020CSB1135\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = StudentDetails.getId();
        assertEquals(ret,"2020CSB1135");
    }
    @Test
    void CheckViewGrades() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
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

        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME401',1,1,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME402',1,2,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME403',2,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME404',2,2,'3',3,'complete','core')");

        int rst = StudentDetails.view_grades("2020MEB1135");
        assertEquals(rst,4);
        stmt.executeUpdate("DROP table id_2020meb1135");
    }




//pass requisite
    @Test
    void CheckPassRequisiteYes() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
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
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',1,1,'5',3,'complete','core')");

        boolean rst = StudentDetails.passRequisite("CS401","2020CSB1141");
        assertEquals(rst,true);
        stmt.executeUpdate("DROP table id_2020csb1141");
    }

    //not pass requisite
    @Test
    void CheckPassRequisiteNo() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
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
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',1,1,'5',3,'enrolled','core')");

        boolean rst = StudentDetails.passRequisite("CS401","2020CSB1141");
        assertEquals(rst,false);
        stmt.executeUpdate("DROP table id_2020csb1141");
    }
    //correct credits
    @Test
    void CheckCredits() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141_new PRIMARY KEY(course_code)" +
                ");");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS402',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS403',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS404',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS405',1,1,'',3,'enrolled','core')");

        int credits = StudentDetails.calculatecredits(1,1,"2020CSB1141");
        assertEquals(credits,15);

        stmt.executeUpdate("DROP table id_2020csb1141");

    }

    //curr_sem=1
    @Test
    void CheckPrev2CreditsSem1() throws SQLException {

        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141_new PRIMARY KEY(course_code)" +
                ");");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS402',1,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS403',1,2,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS404',1,2,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS405',1,2,'',3,'enrolled','core')");

        int credits = StudentDetails.prev2cred("2020CSB1141","2021",1,"2020");
        assertEquals(credits,15);

        stmt.executeUpdate("DROP table id_2020csb1141");

    }

    //curr_sem!=1
    @Test
    void CheckPrev2CreditsSem2() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("Create table id_2020csb1141(" +
                "course_code varchar(10) NOT NULL," +
                "year int NOT NULL," +
                "sem int NOT NULL," +
                "grade text," +
                "credits int NOT NULL," +
                "status varchar(200) NOT NULL, " +
                "type varchar(200) NOT NULL," +
                "CONSTRAINT particular_2020CSB1141_new PRIMARY KEY(course_code)" +
                ");");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS401',2,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS402',2,1,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS403',1,2,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS404',1,2,'',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS405',1,2,'',3,'enrolled','core')");

        int credits = StudentDetails.prev2cred("2020CSB1141","2021",2,"2020");
        assertEquals(credits,15);

        stmt.executeUpdate("DROP table id_2020csb1141");
    }

    //yes deregister
    @Test
    void CheckDeregisterY() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet rs = stmt.executeQuery("SELECT * from current");
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

        int ret = StudentDetails.deregister("2020CSB1141");
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");

    }

    //no deregister
    @Test
    void CheckDeregisterN() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET audit = 2");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.deregister("2020CSB1141");
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
    }
    @Test
    void CheckRegisterPositive() throws SQLException, IOException {
        //audit
        //courses_offered
        //curr_year
        //curr_sem
        //course_catalog
        //String-course_code
        //id_
        //c_

        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','EE,CS','',1,1);");

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

        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2020'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 1");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }
    @Test
    void CheckCourseRegisterElective() throws SQLException, IOException {

        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','MC','EE,CS',1,1);");

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

        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2020'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 1");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }
    //left
    @Test
    void CheckCourseRegisterPreReq() throws SQLException, IOException {

        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 2");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','CS301,CS302','EE,CS','',1,2);");

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
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS301',1,1,'',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS302',1,1,'',3,'complete','core')");

        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2020'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 1");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }
//left
    @Test
    void CheckCourseRegisterPrevYear() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-2','','EE,CS','',2,1);");

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

        stmt.executeUpdate("CREATE table c_cs401_2021(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS301',1,1,'',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS302',1,2,'',3,'complete','core')");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2021");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }
//left
    @Test
    void CheckCourseRegisterCreDiffYearAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 2");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',2,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-2','','EE,CS','',2,2);");

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

        stmt.executeUpdate("CREATE table c_cs401_2021(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS301',2,1,'',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS302',1,2,'',3,'complete','core')");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2021");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckRegisterAuditNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");

        stmt.executeUpdate("UPDATE current SET audit = 2");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("UPDATE current SET audit = "+audit);

    }

    @Test
    void CheckRegisterCGPANotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");

        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',9,'2020',1,'F10');");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2020'");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");

    }

    @Test
    void CheckRegisterYearNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','EE,CS','',1,1);");


        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 1");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckRegisterSemNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','EE,CS','',2,2);");


        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckRegisterCourseTypeNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','EE','',2,1);");


        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckRegisterCreditsNotAllowed1Year() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-2','','EE,CS','',2,1);");

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

        stmt.executeUpdate("CREATE table c_cs401_2021(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS301',1,1,'',1,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS302',1,2,'',1,'complete','core')");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2021");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckCourseRegisterCreDiffYearNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2021'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 2");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2021',2,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-2','','EE,CS','',2,2);");

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

        stmt.executeUpdate("CREATE table c_cs401_2021(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS301',2,1,'',1,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020csb1141 VALUES('CS302',1,2,'',1,'complete','core')");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2021");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2021'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 2");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckRegisterPreReqNotAllowed() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String year = rs.getString("curr_year");
        int sem = rs.getInt("curr_sem");
        //audit allow
        stmt.executeUpdate("UPDATE current SET audit = 1");
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        stmt.executeUpdate("UPDATE current SET curr_sem = 1");
        stmt.executeUpdate("INSERT into courses_offered VALUES('CS401',5,'2020',1,'F10');");
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','CS402','EE,CS','',1,1);");

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

        stmt.executeUpdate("CREATE table c_cs401_2020(id varchar(200) NOT NULL,grade varchar(200),status varchar(200) NOT NULL,CONSTRAINT specific_cs401_2020 PRIMARY KEY(id));");

        String input = "CS401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = StudentDetails.register("2020CSB1141", 8.7F,"2020");
        assertEquals(ret,0);

        stmt.executeUpdate("DROP table id_2020csb1141");
        stmt.executeUpdate("DROP table c_cs401_2020");

        stmt.executeUpdate("DELETE from courses_offered where course_code = 'CS401' and year = '2020'");
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' and year = 1");
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
        //cgpa allow,year allow,sem
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckCGPACal() throws SQLException {

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

        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME401',1,1,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME402',1,1,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME403',1,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME404',1,1,'7',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME405',1,1,'6',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME406',1,1,'5',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME407',1,1,'4',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME408',1,1,'3',3,'complete','core')");
        //string - course,path
        //grades_info-delete krni
        //id_ - 5 different
        //c_course_year -1
        //year
        String input = "ME401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        double ret = StudentDetails.CGPA_calc("2020MEB1135");
        assertEquals(ret,6.5);
        //delete grades_info

        //change year
        //drop tables
        stmt.executeUpdate("DROP table id_2020meb1135");
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");

    }

    @Test
    void CheckCGPACal0Credits() throws SQLException {

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

        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME401',1,1,'10',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME402',1,1,'9',3,'enrolled','core')");
        stmt.executeUpdate("INSERT into id_2020meb1135 VALUES('ME403',1,1,'8',3,'enrolled','core')");
        //string - course,path
        //grades_info-delete krni
        //id_ - 5 different
        //c_course_year -1
        //year
        String input = "ME401\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        double ret = StudentDetails.CGPA_calc("2020MEB1135");
        assertEquals(ret,0);
        //delete grades_info

        //change year
        //drop tables
        stmt.executeUpdate("DROP table id_2020meb1135");
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");

    }

}
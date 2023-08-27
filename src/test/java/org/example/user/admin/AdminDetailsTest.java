package org.example.user.admin;

import org.example.Course.Course;
import org.example.user.database.Insert;
import org.example.user.database.Update;
import org.example.user.faculty.FacultyDetails;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AdminDetailsTest {

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
    void AdminCreate(){
        var admin = new AdminDetails("Admin","2008","23456");
    }

    @Test
    void ChooseFunction(){
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.ChooseFunction();
        assertEquals(ret,1);
    }

    @Test
    void CheckAddCourse() throws IOException, SQLException {
        String input = "CS401\n2-3-4\n\nCS\n\n4\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.add_course();
        assertEquals(ret,1);
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' AND year = 4");
    }

    @Test
    void CheckDeleteCourse() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        String input = "CS401\n4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.delete_course();
        assertEquals(ret,1);
    }

    @Test
    void ChooseCond1() throws SQLException {
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.getcond();
        assertEquals(ret,1);
    }

    @Test
    void ChangeCond1() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.change_condition();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
    }

    @Test
    void ChangeCond3() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String input = "3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.change_condition();
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
    }
    @Test
    void ChangeCond2() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit = rs.getInt("audit");
        String input = "2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.change_condition();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit);
    }

    @Test
    void CheckGetYear() throws IOException {
        String input = "2020\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = AdminDetails.getYear();
        assertEquals(ret,"2020");
    }

    @Test
    void CheckChangeYear() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        String year = rs.getString("curr_year");
        String input = "2020\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.changeYear();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
    }

    @Test
    void CheckGetSem() throws IOException {
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.getSem();
        assertEquals(ret,1);
    }

    @Test
    void CheckChangeSem1() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int sem = rs.getInt("curr_sem");

        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.changeSem();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckChangeSem2() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int sem = rs.getInt("curr_sem");

        String input = "2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.changeSem();
        assertEquals(ret,1);
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckChangeSem3() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int sem = rs.getInt("curr_sem");

        String input = "3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.changeSem();
        assertEquals(ret,0);
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem);
    }

    @Test
    void CheckGetCurrYear2020() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        String year = rs.getString("curr_year");
        stmt.executeUpdate("UPDATE current SET curr_year = '2020'");
        String year2 = AdminDetails.getCurrYear();
        assertEquals(year2,"2020");
        stmt.executeUpdate("UPDATE current SET curr_year = '"+year+"'");
    }
    @Test
    void CheckGetCurrSem1() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int sem2 = rs.getInt("curr_sem");
        stmt.executeUpdate("UPDATE current SET curr_sem = 2");
        int sem1 = AdminDetails.getCurrSem();
        assertEquals(sem1,2);
        stmt.executeUpdate("UPDATE current SET curr_sem = "+sem2);

    }

    @Test
    void CheckGetCurrCond() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        ResultSet  rs = stmt.executeQuery("SELECT * from current");
        rs.next();
        int audit2 = rs.getInt("audit");
        stmt.executeUpdate("UPDATE current SET audit = 1");

        int audit = AdminDetails.getCurrCond();
        assertEquals(audit,1);
        stmt.executeUpdate("UPDATE current SET audit = "+audit2);

    }

    @Test
    void CheckViewGrades() throws SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into grades_info VALUES('2020CEB1135',9,'CE401');");
        int cntprev = AdminDetails.view_grades();
        stmt.executeUpdate("DELETE from grades_info where course= 'CE401' AND  student= '2020CEB1135' ");
        int cntnow = AdminDetails.view_grades();
        assertEquals(cntprev,1+cntnow);
    }

    //    @Test
//    void CheckTranscript() throws IOException {
//        String input = "2020CSB1135\n";
//        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(out);
//        System.setOut(ps);
//
//        int ret = AdminDetails.generate_transcript();
//        assertEquals(ret,1);
//    }

    @Test
    void checkTranscript() throws SQLException, IOException {
        //id_stuentid -me complete courses wd grades
        //auth me add id
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into auth values ('2020MEB1135','23456','student','2020');");
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
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS401',1,1,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS402',1,2,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS403',2,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS404',2,2,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS405',3,1,'8',3,'enrolled','core')");

        String input = "2020MEB1135\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.generate_transcript();
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020meb1135");
        stmt.executeUpdate("DELETE FROM auth WHERE id = '2020MEB1135'");
    }

    @Test
    void CheckGraduation() throws SQLException, IOException {
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
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS401',1,1,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS402',1,2,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS403',2,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS404',2,2,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS405',3,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS406',2,1,'8',3,'complete','elective')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS407',2,2,'9',3,'complete','elective')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS408',3,1,'8',3,'complete','elective')");

        String input = "2020MEB1135\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.checkGraduation();
        assertEquals(ret,1);

        stmt.executeUpdate("DROP table id_2020meb1135");
    }

    @Test
    void CheckGraduationNot() throws SQLException, IOException {
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
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS401',1,1,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS402',1,2,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS403',2,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS404',2,2,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS405',3,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS406',2,1,'8',3,'complete','elective')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS407',2,2,'',3,'enrolled','elective')");

        String input = "2020MEB1135\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.checkGraduation();
        assertEquals(ret,0);

        stmt.executeUpdate("DROP table id_2020meb1135");
    }

    @Test
    void CheckGraduationNotCore() throws SQLException, IOException {
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
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS402',1,2,'10',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS403',2,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS404',2,2,'9',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS405',3,1,'8',3,'complete','core')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS406',2,1,'8',3,'complete','elective')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS407',2,2,'9',3,'complete','elective')");
        stmt.executeUpdate("INSERT into id_2020MEB1135 VALUES('CS408',3,1,'8',3,'complete','elective')");

        String input = "2020MEB1135\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = AdminDetails.checkGraduation();
        assertEquals(ret,0);

        stmt.executeUpdate("DROP table id_2020meb1135");
    }

}
package org.example.Course;

import org.example.user.admin.AdminDetails;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

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
        void CheckRegisterAllow(){
        var course = new Course();
        assertEquals(true, course.isRegisterAllow(true,true,true,true,true));
    }
    @Test
    void CheckDisplayCourses() throws SQLException, IOException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into course_catalog VALUES('CS401','2-3-4','','CS','',4,1);");
        int cntprev = Course.display_courses();
        stmt.executeUpdate("DELETE from course_catalog where course_code = 'CS401' AND year = 4");
        int cntnow = Course.display_courses();
        assertEquals(cntprev,1+cntnow);
        //c.close();
    }

}
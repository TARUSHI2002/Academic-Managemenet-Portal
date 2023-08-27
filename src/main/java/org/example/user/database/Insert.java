package org.example.user.database;

import org.example.Course.Course;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Insert {

    public static int add_course(String query) {
        //insert course
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            int cnt = stmt.executeUpdate(query);
                System.out.println("Entered Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}

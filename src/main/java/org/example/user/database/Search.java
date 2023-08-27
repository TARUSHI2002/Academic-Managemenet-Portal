package org.example.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Search {
    //enrolment_year
    public static String find(String query, String ans) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement(0, 0);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String  need = rs.getString(ans);
                return need;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not found";
    }

    public static ResultSet find(String query) {
        //Display Courses
        Connection c = null;
        Statement stmt = null;
        ResultSet rs=null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement(0, 0);
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}

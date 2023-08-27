package org.example.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Delete {

    public static int delete_course_admin(String query1,String query2) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query1);
            stmt = c.createStatement();
            if(rs.next()==false){System.out.println("Course ID with specified year doesn't exist"); return 0;};
            rs.close();
           stmt.executeUpdate(query2);
           System.out.println("Deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int delete_course_faculty(String query1,String query2) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            stmt.executeUpdate(query1);
            stmt = c.createStatement();
            stmt.executeUpdate(query2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void delete(String query1) {
        //delete Course admin func
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            stmt.executeUpdate(query1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

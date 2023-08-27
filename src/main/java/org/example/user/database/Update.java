package org.example.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Update {

    public static int change_condition(String query){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "postgres", "HK-tr-02");
            stmt = c.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Edited Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}

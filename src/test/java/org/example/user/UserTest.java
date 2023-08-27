package org.example.user;

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

class UserTest {
    @Test
    void User(){
        var user = new User("Admin","2008","23456");
    }
@Test
    void CheckUserType(){
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = User.getUserType();
        assertEquals(ret,1);
    }

    @Test
    void CheckGetId() throws IOException {
        String input = "Admin";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = User.getId();
        assertEquals(ret,"Admin");
    }
    @Test
    void CheckGetPassword() throws IOException {
        String input = "23456";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = User.getPassword();
        assertEquals(ret,"23456");
    }
    @Test
    void CheckIsInvalidNULL() throws IOException {
        String ret = User.IsInvalid(null);
        assertEquals(ret,"Yes");
    }

    @Test
    void CheckIsInvalid() throws IOException {
        String ret = User.IsInvalid("2020");
        assertEquals(ret,"No");
    }

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
        void CheckFindUser() throws IOException, SQLException {
        Connection c = null;
        Statement stmt = CreateConnection(c);
        stmt.executeUpdate("INSERT into auth VALUES('2020EEB1213','23456','student','2020')");
        String ret = User.find_User("2020EEB1213","23456","student");
        assertEquals(ret,"2020");
        stmt.executeUpdate("DELETE from auth where id = '2020EEB1213'");
    }

    @Test
    void CheckWrite(){
        int ret = User.InvalidUser();
        assertEquals(ret,1);
    }


}
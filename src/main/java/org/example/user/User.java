package org.example.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

import org.example.user.database.Insert;
import org.example.user.database.Search;
import org.example.user.database.Search.*;
public class User {
    private String id;
    private String password;
    public String enrol_year;

    public User(String id, String enrol_year, String password) {
        this.id = id;
        this.enrol_year = enrol_year;
        this.password = password;
    }

    public static int getUserType() {
        System.out.println("Choose your user type: 1.Admin  2.Student  3.Faculty 4.Logout\n");
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        return a;
    }

    public static String getId() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Username: ");
        String user = br.readLine();
        return user;
    }

    public static String getPassword() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Password: ");
        String pass = br.readLine();
        return pass;
    }

    public static String IsInvalid(String enrol_year) {
        if (enrol_year == null) return "Yes";
        return "No";
    }

    public static int InvalidUser() {
        System.out.println("Invalid User");
        return 1;
    }


    public static String find_User(String user, String pass, String type) {
        String query = "SELECT * FROM auth WHERE id='" + user + "'AND password='" + pass + "' AND type='" + type + "'";
        String enrolment_year = Search.find(query, "enrol_year");
        return enrolment_year;
    }
}
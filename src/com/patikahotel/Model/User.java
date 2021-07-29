package com.patikahotel.Model;

import com.patikahotel.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private String type;
    private int id;

    public User(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public User(String username, String password, String type, int id) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static boolean add(String username, String password, String type)
    {
        if (type != "Admin" && type != "User") return false;

        boolean isSuccess = false;

        String query = "INSERT INTO Users (username, password, type) VALUES (?,?,?)";
        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            pr.setString(3,type);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
                try {
                    if (pr != null) pr.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }
        return isSuccess;
    }

    public static User get(int user_id)
    {
        String query = "SELECT * FROM Users WHERE id = ?";

        User user = null;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,user_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                user = new User(rs.getString("username"), rs.getString("password"), rs.getString("type"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }

    public static ArrayList<User> getList()
    {
        String query = "SELECT * FROM Users";

        User user = null;

        ArrayList<User> users = new ArrayList<>();

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            rs = pr.executeQuery();
            while (rs.next())
            {
                user = new User(rs.getString("username"), rs.getString("password"), rs.getString("type"), rs.getInt("id"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return users;
    }

    public static boolean update(int user_id, String username, String password, String type)
    {

        if (!type.equals("Admin") && !type.equals("User")) return false;

        boolean isSuccess = false;


        String query = "UPDATE Users SET username = ?, password = ?, type = ? WHERE id = ?";
        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            pr.setString(3,type);
            pr.setInt(4,user_id);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static boolean delete(int user_id)
    {
        String query = "DELETE FROM Users WHERE id = ?";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,user_id);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static int login(String username, String password)
    {
        System.out.println(password);
        String query = "SELECT type FROM Users WHERE username = ? AND password = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        int userType = 0;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            rs = pr.executeQuery();
            if (rs.next())
            {
                switch (rs.getString("type"))
                {
                    case "Admin" -> {
                        userType = 1;
                    }
                    case "User" -> {
                        userType = 2;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        return userType;
    }

}

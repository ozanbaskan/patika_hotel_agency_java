package com.patikahotel.Model;

public class Admin extends User{

    public Admin(String username, String password, String type) {
        super(username, password, type);
    }

    public Admin(String username, String password, String type, int id) {
        super(username, password, type, id);
    }
}

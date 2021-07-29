package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LoginGUI extends JFrame {

    private JPanel wrapper;
    private JLabel lbl_logo;
    private JTextField fld_username;
    private JButton btn_login;
    private JPasswordField fld_password;

    public LoginGUI()
    {
        // set logo and add logo
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        Image image = logo.getImage(); // transform it
        Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH);
        logo = new ImageIcon(newimg);
        lbl_logo.setIcon(logo);
        // ### set logo and add logo

        this.add(wrapper);
        this.setSize(400,400);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_password) || Helper.isFieldEmpty(fld_username))
            {
                Helper.showMessage("Kullanıcı adınızı ve şifrenizi giriniz!", "Giriş yapılamadı!");
                return;
            }
            switch (User.login(fld_username.getText().trim(), String.valueOf(fld_password.getPassword()))) {
                case 0 -> Helper.showMessage("Kullanıcı adı veya şifre hatalı!", "Giriş yapılamadı!");
                case 1 -> {
                    new OperatorGUI();
                    dispose();
                }
                case 2 -> {
                    new UserGUI();
                    dispose();
                }
            }
        });
    }

}

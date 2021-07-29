package com.patikahotel.Helper;

import com.patikahotel.Model.Hotel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Helper {

    public static int centerScreen(String axis, Dimension size)
    {
        int returnValue = 0;
        switch (axis)
        {
            case "x" -> {
                returnValue = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
            }
            case "y" -> {
                returnValue = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
            }
        }
        return returnValue;
    }

    public static void createTables()
    {
        String hotelTable = "CREATE TABLE IF NOT EXISTS Hotel (id SERIAL NOT NULL, city_id INTEGER NOT NULL, district_id INTEGER NOT NULL, name VARCHAR(100) NOT NULL, address VARCHAR(512) NOT NULL, email VARCHAR(255) NOT NULL, phone VARCHAR(15) NOT NULL, star INTEGER NOT NULL, PRIMARY KEY(id),FOREIGN KEY(city_id) REFERENCES City(id), FOREIGN KEY(district_id) REFERENCES District(id))";
        String facilityFeatureTable = "CREATE TABLE IF NOT EXISTS Feature (hotel_id INTEGER NOT NULL, park INTEGER NOT NULL, wifi INTEGER NOT NULL, swimming_pool INTEGER NOT NULL, fitness INTEGER NOT NULL, concierge INTEGER NOT NULL, spa INTEGER NOT NULL, always_service INTEGER NOT NULL, FOREIGN KEY(hotel_id)  REFERENCES Hotel(id))";
        String pansionTypeTable = "CREATE TABLE IF NOT EXISTS Type (hotel_id INTEGER NOT NULL, uEverything INTEGER NOT NULL, everything INTEGER NOT NULL, breakfast INTEGER NOT NULL, fullP INTEGER NOT NULL, halfP INTEGER NOT NULL, onlyBed INTEGER NOT NULL, fullNoAlcohol INTEGER NOT NULL, FOREIGN KEY(hotel_id)  REFERENCES Hotel(id))";
        String adultPriceTable = "CREATE TABLE IF NOT EXISTS adult_price (hotel_id INTEGER NOT NULL,season VARCHAR(12) NOT NULL, uEverything INTEGER, everything INTEGER, breakfast INTEGER, fullP INTEGER, halfP INTEGER, onlyBed INTEGER, fullNoAlcohol INTEGER, FOREIGN KEY(hotel_id)  REFERENCES Hotel(id))";
        String childPriceTable = "CREATE TABLE IF NOT EXISTS child_price (hotel_id INTEGER NOT NULL,season VARCHAR(12) NOT NULL, uEverything INTEGER, everything INTEGER, breakfast INTEGER, fullP INTEGER, halfP INTEGER, onlyBed INTEGER, fullNoAlcohol INTEGER, FOREIGN KEY(hotel_id)  REFERENCES Hotel(id))";
        String seasonTable = "CREATE TABLE IF NOT EXISTS Season (hotel_id INTEGER NOT NULL, summer VARCHAR(11) NOT NULL, winter VARCHAR(11) NOT NULL, FOREIGN KEY(hotel_id) REFERENCES Hotel(id))";
        String roomTable = "CREATE TABLE IF NOT EXISTS Room (id SERIAL NOT NULL, hotel_id INTEGER NOT NULL, bed INTEGER NOT NULL, name VARCHAR(55) NOT NULL, stock INTEGER NOT NULL, area INTEGER NOT NULL, tv BOOLEAN NOT NULL, minibar BOOLEAN NOT NULL, safe BOOLEAN NOT NULL, projection BOOLEAN NOT NULL, game_console BOOLEAN NOT NULL, PRIMARY KEY(id), FOREIGN KEY(hotel_id) REFERENCES Hotel(id))";
        String userTable = "CREATE TABLE IF NOT EXISTS Users (id SERIAL NOT NULL, username VARCHAR(16) NOT NULL UNIQUE, password VARCHAR(16) NOT NULL, type VARCHAR(6) NOT NULL, PRIMARY KEY(id))";
        String reservationTable = "CREATE TABLE IF NOT EXISTS Reservation (id SERIAL NOT NULL, hotel_id INTEGER NOT NULL, room_id INTEGER NOT NULL, enter_date DATE NOT NULL, exit_date DATE NOT NULL, price INTEGER NOT NULL)";
        String contactTable = "CREATE TABLE IF NOT EXISTS contact (reservation_id INTEGER NOT NULL, hotel_id INTEGER NOT NULL, room_id INTEGER NOT NULL, tc VARCHAR(11), fullname VARCHAR(55), phone VARCHAR(15))";
        String visitorTable = "CREATE TABLE IF NOT EXISTS visitor (reservation_id INTEGER NOT NULL, hotel_id INTEGER NOT NULL, room_id INTEGER NOT NULL, tc VARCHAR(11), fullname VARCHAR(55), pansion INTEGER NOT NULL)";

        Statement st = null;

        try {
            st = DBConnector.getConnection().createStatement();
            st.executeUpdate(hotelTable + ";" + facilityFeatureTable + ";" + pansionTypeTable + ";" + seasonTable + ";" + adultPriceTable + ";" + childPriceTable + ";" + roomTable + ";" + userTable + ";" + reservationTable + ";" + contactTable + ";" + visitorTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
                try {
                    if (st != null) st.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }
    }

    public static void warn(String warning)
    {
        optionPageTR();
        JOptionPane.showMessageDialog(null, warning, "Uyarı", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void optionPageTR()
    {
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
        UIManager.put("OptionPane.okButtonText", "Tamam");
    }

    public static ArrayList<String> getCities()
    {
        String query = "SELECT * FROM City";

        ArrayList<String> cities = new ArrayList<>();

        Statement st = null;
        ResultSet rs = null;

        try {
            st = DBConnector.getConnection().createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                cities.add(rs.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return cities;
    }

    public static ArrayList<String> getDistricts(String city)
    {
        String query = "SELECT id FROM City WHERE name = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        int city_id = 0;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,city);
            rs = pr.executeQuery();
            if (rs.next())
            {
                city_id = rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(pr != null) pr.close();
                if(rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        query = "SELECT * FROM District WHERE city_id = ?";

        ArrayList<String> districts = new ArrayList<>();

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,city_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                districts.add(rs.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(pr != null) pr.close();
                if(rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return districts;
    }

    public static String getCity(int city_id)
    {
        String query = "SELECT name FROM City WHERE id = ?";

        String city = "";

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,city_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                city = rs.getString("name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return city;
    }

    public static int getCityId(String city)
    {
        String query = "SELECT id FROM City WHERE name = ?";

        int city_id = -1;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,city);
            rs = pr.executeQuery();
            if (rs.next())
            {
                city_id = rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return city_id;
    }

    public static String getDistrict(int district_id)
    {
        String query = "SELECT name FROM District WHERE id = ?";

        String district = "";

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,district_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                district = rs.getString("name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return district;
    }

    public static int getDistrictId(String district)
    {
        String query = "SELECT id FROM District WHERE name = ?";

        int district_id = -1;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,district);
            rs = pr.executeQuery();
            if (rs.next())
            {
                district_id = rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return district_id;
    }

    public static int convertToInt(boolean b)
    {
        if (b) return 1;
        else return 0;
    }

    public static boolean convertToBoolean(int i)
    {
        return i != 0;
    }

    public static <T extends JTextComponent> boolean isFieldEmpty (T field)
    {
        return field.getText().trim().isEmpty();
    }

    public static void showMessage(String message, String title)
    {
        optionPageTR();
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean sure(String message)
    {
        return JOptionPane.showConfirmDialog(null,message,"İşlemi Onaylayın",JOptionPane.YES_NO_OPTION) == 0;
    }

    public static boolean checkPhone(String number)
    {
        return number.trim().replaceAll(" +", "").matches("^[\s0-9]{10,15}$");
    }

    public static boolean checkEmail(String email)
    {
        return email.trim().matches("^(.*@.*[.].*)$");
    }

}

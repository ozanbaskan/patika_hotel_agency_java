package com.patikahotel.Model;

import com.patikahotel.Helper.DBConnector;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Reservation {

    private Date enter;
    private Date exit;
    private int id;
    private int hotel_id;
    private int room_id;
    private String contact_name;
    private String contact_phone;
    private String contact_id;
    private int price;

    public Reservation(Date enter, Date exit, int id, int hotel_id, int room_id, String contact_name, String contact_phone, String contact_id, int price) {
        this.enter = enter;
        this.exit = exit;
        this.id = id;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.contact_id = contact_id;
        this.price = price;
    }

    public Reservation(Date enter, Date exit, int id, int hotel_id, int price) {
        this.enter = enter;
        this.exit = exit;
        this.id = id;
        this.hotel_id = hotel_id;
        this.price = price;
    }

    public Date getEnter() {
        return enter;
    }

    public void setEnter(Date enter) {
        this.enter = enter;
    }

    public Date getExit() {
        return exit;
    }

    public void setExit(Date exit) {
        this.exit = exit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static boolean add(Date enter, Date exit, int hotel_id, int room_id, String contact_name, String contact_phone, String contact_id, int price, ArrayList<String> visitor_names, ArrayList<String> visitor_ids, ArrayList<Integer> visitor_pansions)
    {

        String query = "INSERT INTO Reservation (hotel_id, room_id, enter_date, exit_date, price) VALUES (?,?,?,?,?) RETURNING id";

        java.sql.Date sqlEnter = new java.sql.Date(enter.getTime());
        java.sql.Date sqlExit = new java.sql.Date(exit.getTime());

        int reservation_id = -1;

        Connection connection = null;
        PreparedStatement pr = null;
        ResultSet rs = null;

        boolean isSuccess = false;

        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(1, hotel_id);
            pr.setInt(2, room_id);
            pr.setDate(3, sqlEnter);
            pr.setDate(4, sqlExit);
            pr.setInt(5, price);
            rs = pr.executeQuery();
            if (rs.next())
            {
                reservation_id = rs.getInt("id");
            }
            if (reservation_id == -1) return false;

            query = "INSERT INTO contact (reservation_id, hotel_id, room_id, tc, fullname, phone) VALUES (?,?,?,?,?,?)";
            pr = connection.prepareStatement(query);
            pr.setInt(1,reservation_id);
            pr.setInt(2,hotel_id);
            pr.setInt(3,room_id);
            pr.setString(4,contact_id);
            pr.setString(5, contact_name);
            pr.setString(6, contact_phone);
            pr.executeUpdate();

            for (int i = 0; i < visitor_ids.size(); i++)
            {
                query = "INSERT INTO visitor (reservation_id, hotel_id, room_id, tc, fullname, pansion) VALUES (?,?,?,?,?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,reservation_id);
                pr.setInt(2,hotel_id);
                pr.setInt(3,room_id);
                pr.setString(4,visitor_ids.get(i));
                pr.setString(5,visitor_names.get(i));
                pr.setInt(6,visitor_pansions.get(i));
                pr.executeUpdate();
            }
            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return isSuccess;
    }

    public static ArrayList<Reservation> getList(int hotel_id)
    {
        String query = "SELECT * FROM Reservation WHERE hotel_id = ?";
        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                reservation = new Reservation(rs.getDate("enter_date"), rs.getDate("exit_date"), rs.getInt("id"), rs.getInt("hotel_id"), rs.getInt("price"));
                reservations.add(reservation);
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
        return reservations;
    }

    public static ArrayList<Reservation> getList()
    {
        String query = "SELECT * FROM Reservation";
        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;

        Statement st = null;
        ResultSet rs = null;

        try {
            st = DBConnector.getConnection().createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                reservation = new Reservation(rs.getDate("enter_date"), rs.getDate("exit_date"), rs.getInt("id"), rs.getInt("hotel_id"), rs.getInt("price"));
                reservations.add(reservation);
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
        return reservations;
    }

    public static boolean delete(int reservation_id)
    {
        String query = "DELETE FROM visitor WHERE reservation_id = ?";

        Connection connection = null;
        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(1,reservation_id);
            if (pr.executeUpdate() == 0) return false;

            query = "DELETE FROM contact WHERE reservation_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,reservation_id);
            if (pr.executeUpdate() == 0) return false;

            query = "DELETE FROM reservation WHERE id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,reservation_id);
            if (pr.executeUpdate() == 0) return false;

            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return isSuccess;
    }

}

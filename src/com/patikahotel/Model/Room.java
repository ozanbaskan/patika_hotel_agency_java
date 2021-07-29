package com.patikahotel.Model;

import com.patikahotel.Helper.DBConnector;
import com.toedter.calendar.DateUtil;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Room {

    private int id;
    private int hotel_id;
    private int stock;
    private String name;
    private String season;

    private int bed;
    private boolean tv;
    private boolean minibar;
    private boolean console;
    private int sMeter;
    private boolean safe;
    private boolean projection;

    public Room(int id, int stock, String name, int bed, boolean tv, boolean minibar, boolean console, int sMeter, boolean safe, boolean projection) {
        this.id = id;
        this.stock = stock;
        this.name = name;
        this.bed = bed;
        this.tv = tv;
        this.minibar = minibar;
        this.console = console;
        this.sMeter = sMeter;
        this.safe = safe;
        this.projection = projection;
    }

    public Room(String season, int id, int hotel_id, int stock, String name, int bed, boolean tv, boolean minibar, boolean console, int sMeter, boolean safe, boolean projection) {
        this.id = id;
        this.stock = stock;
        this.name = name;
        this.bed = bed;
        this.tv = tv;
        this.minibar = minibar;
        this.console = console;
        this.sMeter = sMeter;
        this.safe = safe;
        this.projection = projection;
        this.hotel_id = hotel_id;
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isMinibar() {
        return minibar;
    }

    public void setMinibar(boolean minibar) {
        this.minibar = minibar;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public int getsMeter() {
        return sMeter;
    }

    public void setsMeter(int sMeter) {
        this.sMeter = sMeter;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public boolean isProjection() {
        return projection;
    }

    public void setProjection(boolean projection) {
        this.projection = projection;
    }

    public static boolean add(int hotel_id, String name, int stock, int bed, int sMeter, boolean tv, boolean minibar, boolean console, boolean safe, boolean projection)
    {
        String query = "INSERT INTO Room (hotel_id, name, stock, area, tv, minibar, game_console, safe, projection, bed) VALUES(?,?,?,?,?,?,?,?,?,?)";
        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, hotel_id);
            pr.setString(2, name);
            pr.setInt(3, stock);
            pr.setInt(4, sMeter);
            pr.setBoolean(5, tv);
            pr.setBoolean(6, minibar);
            pr.setBoolean(7, console);
            pr.setBoolean(8, safe);
            pr.setBoolean(9, projection);
            pr.setInt(10, bed);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static ArrayList<Room> getList(int hotel_id)
    {
        String query = "SELECT * FROM Room WHERE hotel_id = ?";

        ArrayList<Room> rooms = new ArrayList<>();

        Room room = null;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                room = new Room(rs.getInt("id"), rs.getInt("stock"), rs.getString("name"), rs.getInt("bed"),rs.getBoolean("tv"),rs.getBoolean("minibar"),rs.getBoolean("game_console"),rs.getInt("area"),rs.getBoolean("safe"),rs.getBoolean("projection"));
                rooms.add(room);
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
        return rooms;
    }

    public static boolean delete(int room_id)
    {
        String query = "DELETE FROM visitor WHERE room_id = ?";

        boolean isSuccess = false;
        PreparedStatement pr = null;
        Connection connection = null;

        try {
            connection = DBConnector.getConnection();
            pr = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            pr.setInt(1,room_id);
            if (pr.executeUpdate() == 0) return false;

            query = "DELETE FROM contact WHERE room_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,room_id);
            if (pr.executeUpdate() == 0) return false;

            query = "DELETE FROM room WHERE room_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,room_id);
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

    public static Room get(int room_id)
    {
        String query = "SELECT * FROM Room WHERE id = ?";

        Room room = null;
        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,room_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                room = new Room(rs.getInt("id"), rs.getInt("stock"), rs.getString("name"), rs.getInt("bed"),rs.getBoolean("tv"),rs.getBoolean("minibar"),rs.getBoolean("game_console"),rs.getInt("area"),rs.getBoolean("safe"),rs.getBoolean("projection"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if(rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return room;
    }

    public static boolean update(int room_id, String name, int stock, int bed, int sMeter, boolean tv, boolean minibar, boolean console, boolean safe, boolean projection)
    {
        String query = "UPDATE Room SET name = ?, stock = ?, area = ?, tv = ?, minibar = ?, game_console = ?, safe = ?, projection = ?, bed = ? WHERE id = ?";
        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1, name);
            pr.setInt(2, stock);
            pr.setInt(3, sMeter);
            pr.setBoolean(4, tv);
            pr.setBoolean(5, minibar);
            pr.setBoolean(6, console);
            pr.setBoolean(7, safe);
            pr.setBoolean(8, projection);
            pr.setInt(9, bed);
            pr.setInt(10,room_id);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static ArrayList<Room> search(String word, Date enter, Date exit)
    {
        String query = "SELECT id FROM Hotel WHERE name ILIKE ?";
        ArrayList<Integer> hotels = new ArrayList<>();

        ArrayList<Room> rooms = new ArrayList<>();
        Room room = null;

        int enterYear = Integer.parseInt(enter.toInstant().toString().substring(0,4));
        int enterMonth = Integer.parseInt(enter.toInstant().toString().substring(5,7));
        int enterDay = Integer.parseInt(enter.toInstant().toString().substring(8,10));

        int exitYear = Integer.parseInt(exit.toInstant().toString().substring(0,4));
        int exitMonth = Integer.parseInt(exit.toInstant().toString().substring(5,7));
        int exitDay = Integer.parseInt(exit.toInstant().toString().substring(8,10));

        PreparedStatement pr = null;
        ResultSet rs = null;
        PreparedStatement pr_room = null;
        ResultSet rs_room = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1, "%" + word + "%");
            rs = pr.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                if (!hotels.contains(id)) hotels.add(id);
            }
            query = "SELECT Hotel.id FROM Hotel JOIN City ON Hotel.city_id = City.id WHERE City.name ILIKE ?";
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,"%" + word + "%");
            rs = pr.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                if (!hotels.contains(id)) hotels.add(id);
            }
            query = "SELECT Hotel.id FROM Hotel JOIN District ON Hotel.city_id = District.id WHERE District.name ILIKE ?";
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,"%" + word + "%");
            rs = pr.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                if (!hotels.contains(id)) hotels.add(id);
            }
            for (Integer hotel_id: hotels)
            {
                query = "SELECT summer,winter FROM Season WHERE hotel_id = ?";
                pr = DBConnector.getConnection().prepareStatement(query);
                pr.setInt(1,hotel_id);
                rs = pr.executeQuery();
                while (rs.next())
                {
                    String summer = rs.getString("summer");
                    String winter = rs.getString("winter");
                    int summerStartMonth = Integer.parseInt(summer.split("/")[0].split("-")[1]);
                    int summerStartDay = Integer.parseInt(summer.split("/")[0].split("-")[0]);
                    int summerEndMonth = Integer.parseInt(summer.split("/")[1].split("-")[1]);
                    int summerEndDay = Integer.parseInt(summer.split("/")[1].split("-")[0]);

                    int winterStartMonth = Integer.parseInt(winter.split("/")[0].split("-")[1]);
                    int winterStartDay = Integer.parseInt(winter.split("/")[0].split("-")[0]);
                    int winterEndMonth = Integer.parseInt(winter.split("/")[1].split("-")[1]);
                    int winterEndDay = Integer.parseInt(winter.split("/")[1].split("-")[0]);

                    Date dateSummerStart = null;
                    Date dateSummerEnd = null;
                    Date dateWinterStart = null;
                    Date dateWinterEnd = null;

                    if
                    (
                            summerStartMonth <= summerEndMonth
                    )
                    {
                        dateSummerStart = new GregorianCalendar(enterYear, summerStartMonth - 1, summerStartDay + 1).getTime();
                        dateSummerEnd = new GregorianCalendar(enterYear, summerEndMonth - 1, summerEndDay + 1).getTime();
                    }
                    else
                    {
                        dateSummerStart = new GregorianCalendar(enterYear, summerStartMonth - 1, summerStartDay + 1).getTime();
                        dateSummerEnd = new GregorianCalendar(enterYear + 1, summerEndMonth - 1, summerEndDay + 1).getTime();
                    }

                    if
                    (
                            winterStartMonth <= winterEndMonth
                    )
                    {
                        dateWinterStart = new GregorianCalendar(enterYear, winterStartMonth - 1, winterStartDay + 1).getTime();
                        dateWinterEnd = new GregorianCalendar(enterYear, winterEndMonth - 1, winterEndDay + 1).getTime();
                    }
                    else
                    {
                        dateWinterStart = new GregorianCalendar(enterYear, winterStartMonth - 1, winterStartDay + 1).getTime();
                        dateWinterEnd = new GregorianCalendar(enterYear + 1, winterEndMonth - 1, winterEndDay + 1).getTime();
                    }
                    /*System.out.println(enter.toInstant());
                    System.out.println(exit.toInstant());
                    System.out.println(summerStartDay);
                    System.out.println(dateSummerStart.toInstant());
                    System.out.println(dateSummerEnd.toInstant());
                    System.out.println(dateWinterStart.toInstant());
                    System.out.println(dateWinterEnd.toInstant());*/
                    if ((dateSummerStart.before(enter) && dateSummerEnd.after(exit)))
                    {
                        query = "SELECT * FROM Room WHERE hotel_id = ?";
                        pr_room = DBConnector.getConnection().prepareStatement(query);
                        pr_room.setInt(1, hotel_id);
                        rs_room = pr_room.executeQuery();
                        while (rs_room.next())
                        {
                            room = new Room("summer", rs_room.getInt("id"), rs_room.getInt("hotel_id"), rs_room.getInt("stock"), rs_room.getString("name"), rs_room.getInt("bed"), rs_room.getBoolean("tv"), rs_room.getBoolean("minibar"), rs_room.getBoolean("game_console"), rs_room.getInt("area"), rs_room.getBoolean("safe"), rs_room.getBoolean("projection"));
                            rooms.add(room);
                        }
                    }
                    else if (dateWinterStart.before(enter) && dateWinterEnd.after(exit))
                    {
                        query = "SELECT * FROM Room WHERE hotel_id = ?";
                        pr_room = DBConnector.getConnection().prepareStatement(query);
                        pr_room.setInt(1, hotel_id);
                        rs_room = pr_room.executeQuery();
                        while (rs_room.next())
                        {
                            room = new Room("winter", rs_room.getInt("id"), rs_room.getInt("hotel_id"), rs_room.getInt("stock"), rs_room.getString("name"), rs_room.getInt("bed"), rs_room.getBoolean("tv"), rs_room.getBoolean("minibar"), rs_room.getBoolean("game_console"), rs_room.getInt("area"), rs_room.getBoolean("safe"), rs_room.getBoolean("projection"));
                            rooms.add(room);
                        }
                    }
                }
            }
            query = "SELECT * FROM Reservation WHERE room_id = ?";
            for (Room fetchedRoom : rooms)
            {
                pr = DBConnector.getConnection().prepareStatement(query);
                pr.setInt(1,fetchedRoom.getId());
                rs = pr.executeQuery();
                while (rs.next())
                {
                    if
                    (
                            (setTimeToMidnight(rs.getDate("enter_date")).compareTo(setTimeToMidnight(enter)) <= 0 && setTimeToMidnight(rs.getDate("exit_date")).compareTo(setTimeToMidnight(enter)) >= 0)
                            ||
                            (setTimeToMidnight(rs.getDate("enter_date")).compareTo(setTimeToMidnight(exit)) <= 0 && setTimeToMidnight(rs.getDate("exit_date")).compareTo(setTimeToMidnight(exit)) >= 0)
                    )
                    {
                        fetchedRoom.setStock(fetchedRoom.getStock() - 1);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (pr != null) pr.close();
                if (rs != null) rs.close();
                if (pr_room != null) pr_room.close();
                if (rs_room != null) rs_room.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return rooms;
    }


    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}

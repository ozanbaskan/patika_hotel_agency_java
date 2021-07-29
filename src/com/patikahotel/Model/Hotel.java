package com.patikahotel.Model;

import com.patikahotel.Helper.DBConnector;

import java.net.Proxy;
import java.sql.*;
import java.text.Collator;
import java.util.*;

public class Hotel{
    private int id;
    private int city_id;
    private int district_id;
    private String name;
    private String address;
    private String eMail;
    private String phoneNo;
    private int star;


    //separate tables on database
    private ArrayList<String> facilityFeatures;
    private ArrayList<String> pansionTypes;
    // There are 2 seasons for each hotel where their pricings differ


    public Hotel(String name, String address, String eMail, String phoneNo, int star, ArrayList<String> facilityFeatures, ArrayList<String> pansionTypes) {
        this.name = name;

        this.address = address;
        this.eMail = eMail;
        this.phoneNo = phoneNo;
        if (star < 1) this.star = 1;
        else this.star = Math.min(star, 5);
        this.facilityFeatures = facilityFeatures;
        this.pansionTypes = pansionTypes;
    }

    public Hotel(int id, int city_id, int district_id, String name, String address, String eMail, String phoneNo, int star) {
        this.id = id;
        this.name = name;
        this.city_id = city_id;
        this.district_id = district_id;
        this.address = address;
        this.eMail = eMail;
        this.phoneNo = phoneNo;
        if (star < 1) this.star = 1;
        else this.star = Math.min(star, 5);
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public ArrayList<String> getPansionTypes() {
        return pansionTypes;
    }

    public void setPansionTypes(ArrayList<String> pansionTypes) {
        this.pansionTypes = pansionTypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        if (star < 1) this.star = 1;
        else this.star = Math.min(star, 5);
    }

    public ArrayList<String> getFacilityFeatures() {
        return facilityFeatures;
    }

    public void setFacilityFeatures(ArrayList<String> facilityFeatures) {
        this.facilityFeatures = facilityFeatures;
    }

    public ArrayList<String> getPansionType() {
        return pansionTypes;
    }

    public void setPansionType(ArrayList<String> pansionTypes) {
        this.pansionTypes = pansionTypes;
    }

    public static boolean add(String name, int city_id, int district_id, String address, String eMail, String phone, int star, ArrayList<Integer> facilityFeatures, ArrayList<Integer> pansionTypes, String summer, String winter)
    {
        String query = "INSERT INTO Hotel (name, city_id, district_id, address, email, phone, star) VALUES(?,?,?,?,?,?,?) RETURNING id";

        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        boolean isSuccess = false;

        int hotel_id = -1;
        if (star < 1) star = 1;
        else star = Math.min(star, 5);

        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setString(1,name);
            pr.setInt(2,city_id);
            pr.setInt(3,district_id);
            pr.setString(4, address);
            pr.setString(5,eMail);
            pr.setString(6,phone);
            pr.setInt(7,star);
            rs = pr.executeQuery();
            if (rs.next())
            {
                hotel_id = rs.getInt("id");
            }
            if (hotel_id != -1)
            {
                query = "INSERT INTO Type (hotel_id, ueverything, everything, breakfast, fullp, halfp, onlybed, fullnoalcohol) VALUES (?,?,?,?,?,?,?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setInt(2,pansionTypes.get(0));
                pr.setInt(3,pansionTypes.get(1));
                pr.setInt(4,pansionTypes.get(2));
                pr.setInt(5,pansionTypes.get(3));
                pr.setInt(6,pansionTypes.get(4));
                pr.setInt(7,pansionTypes.get(5));
                pr.setInt(8,pansionTypes.get(6));
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO Feature (hotel_id, park, wifi, swimming_pool, fitness, concierge, spa, always_service) VALUES (?,?,?,?,?,?,?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setInt(2,facilityFeatures.get(0));
                pr.setInt(3,facilityFeatures.get(1));
                pr.setInt(4,facilityFeatures.get(2));
                pr.setInt(5,facilityFeatures.get(3));
                pr.setInt(6,facilityFeatures.get(4));
                pr.setInt(7,facilityFeatures.get(5));
                pr.setInt(8,facilityFeatures.get(6));
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO adult_price (hotel_id, season) VALUES (?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setString(2,"winter");
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO adult_price (hotel_id, season) VALUES (?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setString(2,"summer");
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO child_price (hotel_id, season) VALUES (?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setString(2,"winter");
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO child_price (hotel_id, season) VALUES (?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setString(2,"summer");
                if (pr.executeUpdate() == 0) return false;
                query = "INSERT INTO Season (hotel_id, summer, winter) VALUES (?,?,?)";
                pr = connection.prepareStatement(query);
                pr.setInt(1,hotel_id);
                pr.setString(2,summer);
                pr.setString(3,winter);
                if (pr.executeUpdate() == 0) return false;
                connection.commit();
                isSuccess = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                {
                    try {
                        if(pr != null) pr.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }

        return isSuccess;

    }

    public static boolean update(int hotel_id, String name, int city_id, int district_id, String address, String eMail, String phone, int star, ArrayList<Integer> facilityFeatures, ArrayList<Integer> pansionTypes, String summer, String winter)
    {
        String query = "UPDATE Feature SET park = ?, wifi = ?, swimming_pool = ?, fitness = ?, concierge = ?, spa = ?, always_service = ? WHERE hotel_id = ?";

        Connection connection = null;
        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            connection = DBConnector.getConnection();
            pr = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            pr.setInt(8,hotel_id);
            pr.setInt(1,facilityFeatures.get(0));
            pr.setInt(2,facilityFeatures.get(1));
            pr.setInt(3,facilityFeatures.get(2));
            pr.setInt(4,facilityFeatures.get(3));
            pr.setInt(5,facilityFeatures.get(4));
            pr.setInt(6,facilityFeatures.get(5));
            pr.setInt(7,facilityFeatures.get(6));
            if (pr.executeUpdate() == 0) return false;
            query = "UPDATE Type SET ueverything = ?, everything = ?, breakfast = ?, fullp = ?, halfp = ?, onlybed = ?, fullnoalcohol = ? WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(8,hotel_id);
            pr.setInt(1,pansionTypes.get(0));
            pr.setInt(2,pansionTypes.get(1));
            pr.setInt(3,pansionTypes.get(2));
            pr.setInt(4,pansionTypes.get(3));
            pr.setInt(5,pansionTypes.get(4));
            pr.setInt(6,pansionTypes.get(5));
            pr.setInt(7,pansionTypes.get(6));
            if (pr.executeUpdate() == 0) return false;
            query = "UPDATE Season SET summer = ?, winter = ? WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(3,hotel_id);
            pr.setString(1,summer);
            pr.setString(2,winter);
            if (pr.executeUpdate() == 0) return false;
            query = "UPDATE Hotel SET name = ?, city_id = ?, district_id = ?, address = ?, email = ?, phone = ?, star = ? WHERE id = ?";
            pr = connection.prepareStatement(query);
            pr.setString(1,name);
            pr.setInt(2,city_id);
            pr.setInt(3,district_id);
            pr.setString(4, address);
            pr.setString(5,eMail);
            pr.setString(6,phone);
            pr.setInt(7,star);
            pr.setInt(8,hotel_id);
            if (pr.executeUpdate() == 0) return false;
            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return isSuccess;

    }


    public static ArrayList<Hotel> getList()
    {
        String query = "SELECT * FROM Hotel";
        ArrayList<Hotel> hotels = new ArrayList<>();


        Hotel hotel = null;

        Statement st = null;
        ResultSet rs = null;

        try {
            st = DBConnector.getConnection().createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                hotel = new Hotel(rs.getInt("id"), rs.getInt("city_id"), rs.getInt("district_id"), rs.getString("name"), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getInt("star"));
                hotels.add(hotel);
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

        Collections.sort(hotels, new Comparator<Hotel>(){
            public int compare(Hotel h1, Hotel h2) {
                return h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase());
            }
        });

        return hotels;
    }

    public static int[] getTypes(int hotel_id)
    {
        String query = "SELECT * FROM Type WHERE hotel_id = ?";
        int[] values = new int[7];
        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                values[0] = rs.getInt("ueverything");
                values[1] = rs.getInt("everything");
                values[2] = rs.getInt("breakfast");
                values[3] = rs.getInt("fullp");
                values[4] = rs.getInt("halfp");
                values[5] = rs.getInt("onlybed");
                values[6] = rs.getInt("fullnoalcohol");
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
        return values;
    }

    public static int[] getFeatures(int hotel_id)
    {
        String query = "SELECT * FROM Feature WHERE hotel_id = ?";
        int[] values = new int[7];
        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                values[0] = rs.getInt("park");
                values[1] = rs.getInt("wifi");
                values[2] = rs.getInt("swimming_pool");
                values[3] = rs.getInt("fitness");
                values[4] = rs.getInt("concierge");
                values[5] = rs.getInt("spa");
                values[6] = rs.getInt("always_service");
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
        return values;
    }

    public static boolean delete(int hotel_id)
    {
        String query = "DELETE FROM Feature WHERE hotel_id = ?";

        PreparedStatement pr = null;
        Connection connection = null;
        boolean isSuccess = false;


        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM contact WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM visitor WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM Reservation WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM Room WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM adult_price WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM child_price WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM Type WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM Season WHERE hotel_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();

            query = "DELETE FROM Hotel WHERE id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.executeUpdate();
            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if (connection != null) connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return isSuccess;
    }

    public static Hotel get(int hotel_id)
    {
        String query = "SELECT * FROM Hotel WHERE id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;
        Hotel hotel = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                hotel = new Hotel(rs.getInt("id"), rs.getInt("city_id"), rs.getInt("district_id"), rs.getString("name"), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getInt("star"));
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
        return hotel;
    }

    public static boolean setPrices(int hotel_id, ArrayList<Integer> adultPrices, ArrayList<Integer> childPrices, String season)
    {
        String query = "UPDATE adult_price SET ueverything = ?, everything = ?, breakfast = ?, fullp = ?, halfp = ?, onlybed = ?, fullnoalcohol = ? WHERE hotel_id = ? AND season = ?";

        PreparedStatement pr = null;
        Connection connection = null;
        boolean isSuccess = false;


        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(8,hotel_id);
            pr.setString(9,season);

            for (int i = 1; i <= adultPrices.size();i++)
            {

                if (adultPrices.get(i-1) != null) {
                    pr.setInt(i, adultPrices.get(i-1));
                } else {
                    pr.setNull(i,java.sql.Types.NULL);
                }
            }
            if (pr.executeUpdate() == 0) return false;
            query = "UPDATE child_price SET ueverything = ?, everything = ?, breakfast = ?, fullp = ?, halfp = ?, onlybed = ?, fullnoalcohol = ? WHERE hotel_id = ? AND season = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(8,hotel_id);
            pr.setString(9,season);

            for (int i = 1; i <= childPrices.size();i++)
            {
                if (childPrices.get(i-1) != null)
                {
                    pr.setInt(i, childPrices.get(i-1));
                } else {
                    pr.setNull(i,java.sql.Types.NULL);
                }

            }
            if (pr.executeUpdate() == 0) return false;
            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static ArrayList<Integer> getPrices(int hotel_id, String season)
    {
        ArrayList<Integer> prices = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null,null,null,null));
        String query = "SELECT * FROM adult_price WHERE hotel_id = ? AND season = ?";

        Connection connection = null;
        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            connection = DBConnector.getConnection();
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.setString(2,season);
            rs = pr.executeQuery();
            while (rs.next())
            {
                for (int i = 0; i < 7;i++)
                {
                    prices.set(i,rs.getInt(i+3));
                }
            }
            query = "SELECT * FROM child_price WHERE hotel_id = ? AND season = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,hotel_id);
            pr.setString(2,season);
            rs = pr.executeQuery();
            while (rs.next())
            {
                for (int i = 7; i < 14;i++)
                {
                    prices.set(i,rs.getInt(i-4));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally
        {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return prices;
    }
}

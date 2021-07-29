package com.patikahotel.Model;

import com.patikahotel.Helper.DBConnector;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Season {

    private int month;
    private int day;

    public Season(int month, int day) {
        if (month < 1) this.month = 1;
        else this.month = Math.min(month,12);

        if (day < 1)
        {
            this.day = 1;
        }
        else
        {
            switch (month)
            {
                case 1,3,5,7,8,10,12 -> {
                    if (day > 31) this.day = 31;
                }
                case 4,6,9,11 -> {
                    if (day > 30) this.day = 30;
                }
                case 2 -> {
                    if (day > 29) this.day = 29;
                }

            }
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1) this.month = 1;
        else this.month = Math.min(month,12);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day < 1)
        {
            this.day = 1;
        }
        else
        {
            switch (month)
            {
                case 1,3,5,7,8,10,12 -> {
                    if (day > 31) this.day = 31;
                }
                case 4,6,9,11 -> {
                    if (day > 30) this.day = 30;
                }
                case 2 -> {
                    if (day > 29) this.day = 29;
                }

            }
        }
    }

    public static String prepareString(int startMonth, int startDay, int endMonth, int endDay)
    {
        return startDay + "-" + startMonth + "/" + endDay + "-" + endMonth;
    }

    public static ArrayList<Integer> getDaysOfMonth(int month)
    {
        ArrayList<Integer> days = new ArrayList<>();

        switch (month)
        {
            case 1,3,5,7,8,10,12 -> {
                for (int day = 1; day <= 31;day++)
                {
                    days.add(day);
                }
            }
            case 4,6,9,11 -> {
                for (int day = 1; day <= 30;day++)
                {
                    days.add(day);
                }
            }
            case 2 -> {
                for (int day = 1; day <= 29;day++)
                {
                    days.add(day);
                }
            }
        }

        return days;
    }

    public static ArrayList<Integer> getDaysOfMonth(String month)
    {
        ArrayList<Integer> days = new ArrayList<>();

        switch (month)
        {
            case "Ocak","Mart","Mayıs","Temmuz","Ağustos","Ekim","Aralık" -> {
                for (int day = 1; day <= 31;day++)
                {
                    days.add(day);
                }
            }
            case "Nisan","Haziran","Eylül","Kasım" -> {
                for (int day = 1; day <= 30;day++)
                {
                    days.add(day);
                }
            }
            case "Şubat" -> {
                for (int day = 1; day <= 29;day++)
                {
                    days.add(day);
                }
            }
        }

        return days;
    }

    public static int getMonthIndex(String month)
    {
        switch(month)
        {
            case "Ocak":
                return 1;
            case "Şubat":
                return 2;
            case "Mart":
                return 3;
            case "Nisan":
                return 4;
            case "Mayıs":
                return 5;
            case "Haziran":
                return 6;
            case "Temmuz":
                return 7;
            case "Ağustos":
                return 8;
            case "Eylül":
                return 9;
            case "Ekim":
                return 10;
            case "Kasım":
                return 11;
            case "Aralık":
                return 12;
        }

        return 1;
    }

    public static String getMonth(int month)
    {
        switch(month)
        {
            case 1:
                return "Ocak";
            case 2:
                return "Şubat";
            case 3:
                return "Mart";
            case 4:
                return "Nisan";
            case 5:
                return "Mayıs";
            case 6:
                return "Haziran";
            case 7:
                return "Temmuz";
            case 8:
                return "Ağustos";
            case 9:
                return "Eylül";
            case 10:
                return "Ekim";
            case 11:
                return "Kasım";
            case 12:
                return "Aralık";
        }

        return "Ay";
    }

    public static ArrayList<String> getSeason(int hotel_id)
    {
        String query = "SELECT * FROM Season WHERE hotel_id = ?";

        ArrayList<String> summerAndWinter = new ArrayList<>();

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,hotel_id);
            rs = pr.executeQuery();
            if (rs.next())
            {
                summerAndWinter.add(rs.getString("summer"));
                summerAndWinter.add(rs.getString("winter"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally
        {
            try {
                if (pr != null) pr.close();
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return summerAndWinter;
    }
}

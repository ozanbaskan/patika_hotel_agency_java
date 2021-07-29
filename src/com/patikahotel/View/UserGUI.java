package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.Hotel;
import com.patikahotel.Model.Reservation;
import com.patikahotel.Model.Room;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UserGUI extends JFrame {

    JPanel wrapper;
    private JPanel pnl_srch_room;
    private JTextField fld_main_srch;
    private JDateChooser enterDateChooser;
    private JDateChooser exitDateChooser;
    private JButton btn_srch_room;
    private JTextField fld_adult;
    private JTable tbl_room;
    private JTextField fld_child;
    private JButton btn_choose_room;
    private JScrollPane scrl_room;
    private JTabbedPane tabbed_pane;
    private JTable tbl_reservations;
    private JScrollPane scrl_reservations;
    private JComboBox<String> cmb_reservations_hotels;
    private JButton btn_fetch_all_reservations;
    private JButton btn_delete_reservation;
    private JButton btn_exit;
    private DefaultTableModel mdl_room;
    private Object[] row_room;
    private  DefaultTableModel mdl_reservation;
    private Object[] row_reservation;

    private boolean selectedHotel = false;

    public UserGUI()
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(1000,500);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);


        // search hotel panel

        mdl_room = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        Object[] col_room = {"ID", "Otel ID", "Sezon","Otel İsmi", "Otel Konumu","Oda İsmi", "Metrekare", "Yatak Sayısı", "TV", "Oyun Konsolu", "Projeksiyon", "Kasa", "Minibar", "Oda Stoğu"};
        mdl_room.setColumnIdentifiers(col_room);
        row_room = new Object[col_room.length];

        tbl_room.setModel(mdl_room);

        tbl_room.getColumnModel().getColumn(0).setMaxWidth(40);
        tbl_room.getColumnModel().getColumn(1).setMaxWidth(50);
        tbl_room.getColumnModel().getColumn(4).setMinWidth(150);
        tbl_room.getColumnModel().getColumn(5).setMaxWidth(80);
        tbl_room.getColumnModel().getColumn(6).setMaxWidth(80);
        tbl_room.getColumnModel().getColumn(7).setMaxWidth(85);
        tbl_room.getColumnModel().getColumn(8).setMaxWidth(40);
        tbl_room.getColumnModel().getColumn(9).setMaxWidth(80);
        tbl_room.getColumnModel().getColumn(10).setMaxWidth(70);
        tbl_room.getColumnModel().getColumn(11).setMaxWidth(70);



        btn_srch_room.addActionListener(e -> {
            Date enter = enterDateChooser.getDate();
            Date exit = exitDateChooser.getDate();
            if (Helper.isFieldEmpty(fld_main_srch))
            {
                Helper.showMessage("Zorunlu alanları doldurmadınız!", "Arama gerçekleştirilemedi!");
                return;
            }
            if (fld_child.getText().matches(".*[^0-9]+.*") || fld_adult.getText().matches(".*[^0-9]+.*"))
            {
                Helper.showMessage("Uygunsuz değerler girdiniz!", "Arama gerçekleştirilemedi!");
                return;
            }

            if (enter == null || exit == null)
            {
                Helper.showMessage("Tarihler boş bırakılamaz!", "Arama gerçekleştirilemedi!");
                return;
            }
            if (enter.after(exit))
            {
                Helper.showMessage("Uygunsuz tarihler girdiniz!", "Arama gerçekleştirilemedi!");
                return;
            }
            else
            {
                searchRooms(fld_main_srch.getText().trim(), enter, exit);
            }
        });


        btn_choose_room.addActionListener(e -> {
            Date enter = enterDateChooser.getDate();
            Date exit = exitDateChooser.getDate();
            int selected_room = tbl_room.getSelectedRow();
            if (selected_room == -1) return;

            if (Helper.isFieldEmpty(fld_child) && Helper.isFieldEmpty(fld_adult))
            {
                Helper.showMessage("Zorunlu alanları doldurmadınız!", "Arama gerçekleştirilemedi!");
                return;
            }

            if (fld_child.getText().matches(".*[^0-9]+.*") || fld_adult.getText().matches(".*[^0-9]+.*"))
            {
                Helper.showMessage("Uygunsuz değerler girdiniz!", "Arama gerçekleştirilemedi!");
                return;
            }

            int adult = 0;
            int child = 0;
            int hotel_id = 0;
            int room_id = 0;
            String season = "";

            try
            {
                season = (String) tbl_room.getValueAt(tbl_room.getSelectedRow(),2);
                hotel_id = (int) tbl_room.getValueAt(tbl_room.getSelectedRow(),1);
                room_id = (int) tbl_room.getValueAt(tbl_room.getSelectedRow(),0);
                adult = Integer.parseInt(fld_adult.getText());
                child = Integer.parseInt(fld_child.getText());
            } catch (Exception ignore) { };

            if (hotel_id != 0 || room_id != 0 || !season.equals(""))
            {
                long diff = exit.getTime() - enter.getTime();
                int day = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                ReservationGUI reservationGUI = new ReservationGUI(enterDateChooser.getDate(), exitDateChooser.getDate(), adult, child, hotel_id, room_id, season, day);
                reservationGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        searchRooms(fld_main_srch.getText().trim(), enter, exit);
                    }
                });
            }
            else
            {
                Helper.showMessage("Otel seçilmedi ya da bulunamadı.", "Hata!");
            }



        });
        // ### search hotel panel

        // reservation panel



        mdl_reservation = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        Object[] col_reservation = {"Rezervasyon ID", "Otel İsmi", "Otel Konumu", "Giriş Tarihi", "Çıkış Tarihi", "Ücret"};
        mdl_reservation.setColumnIdentifiers(col_reservation);
        row_reservation = new Object[col_room.length];

        tbl_reservations.setModel(mdl_reservation);

        listReservations();

        HashMap<String, Integer> hotels = new HashMap<>();



        for (Hotel hotel : Hotel.getList())
        {
            hotels.put(hotel.getName(),hotel.getId());
            cmb_reservations_hotels.addItem(hotel.getName());
        }

        btn_fetch_all_reservations.addActionListener(e -> {
            listReservations();
            selectedHotel = false;
        });


        cmb_reservations_hotels.addActionListener(e -> {
            listReservations(hotels.get(cmb_reservations_hotels.getSelectedItem().toString()));
            selectedHotel = true;
        });

        btn_delete_reservation.addActionListener(e -> {
            if (tbl_reservations.getSelectedRow() == -1) return;

            if (!Reservation.delete((Integer) tbl_reservations.getValueAt(tbl_reservations.getSelectedRow(),0)))
            {
                Helper.showMessage("Bu kayıt silinemedi!", "Hata!");
            }
            else
            {
                if (selectedHotel)
                {
                    listReservations(hotels.get(cmb_reservations_hotels.getSelectedItem().toString()));
                }
                else
                {
                    listReservations();
                }
            }
        });

        // ### reservation panel


        btn_exit.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });
    }



    void createUIComponents()
    {
        enterDateChooser = new JDateChooser();
        exitDateChooser = new JDateChooser();
    }

    public void searchRooms(String word, Date enter, Date exit)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_room.getModel();
        clearModel.setRowCount(0);

        for(Room room : Room.search(word, enter, exit))
        {
            if (room.getStock() <= 0) continue;
            Hotel hotel = Hotel.get(room.getHotel_id());
            int i = 0;
            row_room[i++] = room.getId();
            row_room[i++] = room.getHotel_id();
            row_room[i++] = room.getSeason().equals("winter") ? "Kış" : "Yaz";
            row_room[i++] = hotel.getName();
            row_room[i++] = Helper.getCity(hotel.getCity_id()) + " - " + Helper.getDistrict(hotel.getDistrict_id());
            row_room[i++] = room.getName();
            row_room[i++] = room.getsMeter();
            row_room[i++] = room.getBed();
            row_room[i++] = room.isTv() ? "Var" : "Yok";
            row_room[i++] = room.isConsole() ? "Var" : "Yok";
            row_room[i++] = room.isProjection() ? "Var" : "Yok";
            row_room[i++] = room.isSafe() ? "Var" : "Yok";
            row_room[i++] = room.isMinibar() ? "Var" : "Yok";
            row_room[i++] = room.getStock();
            mdl_room.addRow(row_room);
        }
    }

    public void listReservations(int hotel_id)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_reservations.getModel();
        clearModel.setRowCount(0);

        Hotel hotel = Hotel.get(hotel_id);

        for (Reservation reservation : Reservation.getList(hotel_id))
        {
            int i = 0;
            row_reservation[i++] = reservation.getId();
            row_reservation[i++] = hotel.getName();
            row_reservation[i++] = Helper.getCity(hotel.getCity_id()) + " - " + Helper.getDistrict(hotel.getDistrict_id());
            row_reservation[i++] = reservation.getEnter();
            row_reservation[i++] = reservation.getExit();
            row_reservation[i++] = reservation.getPrice();
            mdl_reservation.addRow(row_reservation);
        }

    }

    public void listReservations()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_reservations.getModel();
        clearModel.setRowCount(0);

        for (Reservation reservation : Reservation.getList())
        {
            Hotel hotel = Hotel.get(reservation.getHotel_id());
            int i = 0;
            row_reservation[i++] = reservation.getId();
            row_reservation[i++] = hotel.getName();
            row_reservation[i++] = Helper.getCity(hotel.getCity_id()) + " - " + Helper.getDistrict(hotel.getDistrict_id());
            row_reservation[i++] = reservation.getEnter();
            row_reservation[i++] = reservation.getExit();
            row_reservation[i++] = reservation.getPrice();
            mdl_reservation.addRow(row_reservation);
        }

    }


}

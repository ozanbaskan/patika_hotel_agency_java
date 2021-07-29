package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.Hotel;
import com.patikahotel.Model.Room;
import com.patikahotel.Model.Season;
import com.patikahotel.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;

    // hotel values which stored by binary values in database
    private ArrayList<String> pansionTypes = new ArrayList<>(Arrays.asList("Ultra Her Şey Dahil", "Her Şey Dahil", "Oda Kahvaltı", "Tam Pansiyon", "Yarım Pansiyon", "Sadece Yatak", "Alkol Hariç Full"));
    private String[] features = {"Ücretsiz Otopark", "Ücretsiz Wifi", "Yüzme Havusu", "Fitness Center", "Hotel Concierge", "SPA", "7/24 Oda Servisi"};
    // ## hotel values which stored by binary values in database

    // hotel table
    private JTable tbl_hotels;
    private JScrollPane scrl_hotels;
    private DefaultTableModel mdl_hotels;
    private Object[] row_hotels;
    // ### hotel table

    // hotel-related tables
    private JTable tbl_features;
    private DefaultTableModel mdl_features;
    private Object[] row_features;

    private JTable tbl_types;
    private DefaultTableModel mdl_types;
    private Object[] row_types;

    private JScrollPane scrl_features;
    private JScrollPane scrl_types;

    // ### hotel-related tables

    private JButton btn_add_hotel;
    private JButton btn_delete_hotel;
    private JButton btn_edit_hotel;
    private JTextField fld_hotel_srch;
    private JLabel fld_hotel_name;
    private JLabel fld_summer;
    private JLabel fld_winter;
    private JPanel pnl_hotel_info;
    private JLabel lbl_seasons;
    private JLabel lbl_price;

    private DefaultTableModel mdl_pricing_winter;
    private DefaultTableModel mdl_pricing_summer;
    private Object[] row_pricing;

    private JTable tbl_pricing_summer;
    private JTable tbl_pricing_winter;
    private JScrollPane scrl_pricing_summer;
    private JScrollPane scrl_pricing_winter;
    private JButton btn_update_prices;
    private JButton btn_delete_room;
    private JTable tbl_rooms;
    private JScrollPane scrl_rooms;
    private JPanel pnl_hotel;
    private JButton btn_add_room;
    private JButton btn_edit_room;
    private JTabbedPane tabbed_main_panel;

    // USER

    private JPanel pnl_user;
    private JTable tbl_user;
    private JScrollPane scrl_user;
    private JTextField fld_add_username;
    private JTextField fld_add_pw;
    private JComboBox cmb_add_type;
    private JButton btn_add_user;
    private JTextField fld_srch_user;
    private JButton btn_user_delete;
    private JTextField fld_user_delete_id;
    private JTextField fld_edit_username;
    private JTextField fld_edit_password;
    private JComboBox cmb_edit_type;
    private JButton btn_user_edit;
    private JButton btn_exit;

    private DefaultTableModel mdl_users;
    private Object[] row_users;

    // ### USER




    // room table

    private DefaultTableModel mdl_rooms;
    private Object[] row_rooms;

    // ### room table


    // selected hotel row

    private int selected_hotel_id = -1;
    private String selected_hotel_name;

    // ### selected hotel row

    ImageIcon logo;

    public OperatorGUI()
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(1100,800);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        // HOTEL

         // hotel table
        mdl_hotels = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_hotels = {"ID", "İsim", "Şehir", "Bölge", "E-mail", "Telefon", "Yıldız"};
        mdl_hotels.setColumnIdentifiers(col_hotels);
        row_hotels = new Object[col_hotels.length];

        tbl_hotels.setModel(mdl_hotels);

        tbl_hotels.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_hotels.getTableHeader().setReorderingAllowed(false);

        this.updateHotels();
        // ### hotel table


        // hotel-related tables

        mdl_features = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        Object[] col_features = {"Otel Özellikleri"};
        mdl_features.setColumnIdentifiers(col_features);
        row_features = new Object[col_features.length];

        tbl_features.setModel(mdl_features);

        mdl_types = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        Object[] col_types = {"Pansiyon Tipleri"};
        mdl_types.setColumnIdentifiers(col_types);
        row_types = new Object[col_types.length];

        tbl_types.setModel(mdl_types);


        // ### hotel-related tables

        // room table

        mdl_rooms = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        Object[] col_rooms = {"ID", "Oda İsmi", "Metrekare", "Yatak Sayısı", "TV", "Oyun Konsolu", "Projeksiyon", "Kasa", "Minibar", "Oda Stoğu"};
        mdl_rooms.setColumnIdentifiers(col_rooms);
        row_rooms = new Object[col_rooms.length];

        tbl_rooms.setModel(mdl_rooms);

        // ### room table

        // pricing tables

        mdl_pricing_winter = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                if (column == 0) return false;
                return true;
            }
        };

        mdl_pricing_summer = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                if (column == 0) return false;
                return true;
            }
        };




        Object[] col_pricing = {"Pansiyon Tipi", "Yetişkin (TL)", "Çocuk (TL)"};
        mdl_pricing_winter.setColumnIdentifiers(col_pricing);
        mdl_pricing_summer.setColumnIdentifiers(col_pricing);
        row_pricing = new Object[col_pricing.length];

        tbl_pricing_winter.setModel(mdl_pricing_winter);
        tbl_pricing_summer.setModel(mdl_pricing_summer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment( JLabel.RIGHT );

        tbl_pricing_summer.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tbl_pricing_summer.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        tbl_pricing_winter.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tbl_pricing_winter.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        // ### pricing tables


        // update hotel-related tables and fields on hotel selection

        tbl_hotels.getSelectionModel().addListSelectionListener(e ->{
            try
            {
                updateSeasonLabels();
                updateHotelStuff();
                updatePrices();
                updateRooms();
            } catch (Exception ignre) {};
        });

        // ### update hotel-related tables and fields on hotel selection


        // buttons

        btn_add_hotel.addActionListener(e -> {
            int selected_row = tbl_hotels.getSelectedRow();
            AddEditHotelGUI addHotelGUI = new AddEditHotelGUI();
            addHotelGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateHotels();
                    if (selected_row != -1) tbl_hotels.setRowSelectionInterval(selected_row,selected_row);
                }
            });
        });

        btn_delete_hotel.addActionListener(e -> {
            if (!Helper.sure(this.selected_hotel_name + " otelini silmek istediğinize emin misiniz?")) return;
            if (!Hotel.delete(this.selected_hotel_id)) {
                Helper.showMessage("İşlem Başarısız!", "Silme işlemi gerçekleştirilemedi");
            }
            else {
                updateHotels();
                updateRooms();
            }
        });

        btn_edit_hotel.addActionListener(e -> {
            int selected_row = tbl_hotels.getSelectedRow();
            if (selected_row == -1) return;
            AddEditHotelGUI editHotelGUI = new AddEditHotelGUI(this.selected_hotel_id);
            editHotelGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateHotels();
                    tbl_hotels.setRowSelectionInterval(selected_row,selected_row);
                }
            });
        });

        // ### buttons

        // search field
        fld_hotel_srch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ENTER)
                {
                    updateHotels(fld_hotel_srch.getText().trim());
                }
            }
        });
        // ### search field

        // update prices
        btn_update_prices.addActionListener(e -> {

            ArrayList<Integer> adultPrices = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null));
            ArrayList<Integer> childPrices = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null));

            for (int i = 0; i < tbl_pricing_summer.getRowCount();i++)
            {
                if (tbl_pricing_summer.getValueAt(i,1) != null)
                {
                    try
                    {
                        int typeIndexInDataBase = pansionTypes.indexOf(tbl_pricing_summer.getValueAt(i,0).toString());
                        adultPrices.set(typeIndexInDataBase, Integer.parseInt(tbl_pricing_summer.getValueAt(i,1).toString()));
                    } catch (Exception ignore){};

                }

                if (tbl_pricing_summer.getValueAt(i,2) != null)
                {
                    try
                    {
                    int typeIndexInDataBase = pansionTypes.indexOf(tbl_pricing_summer.getValueAt(i,0).toString());
                    childPrices.set(typeIndexInDataBase, Integer.parseInt(tbl_pricing_summer.getValueAt(i,2).toString()));
                    } catch (Exception ignore){};
                }
            }

            if (!Hotel.setPrices(selected_hotel_id,adultPrices,childPrices,"summer")) Helper.showMessage("Fiyatlar güncellenemedi.", "Başarısız!");

            adultPrices = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null));
            childPrices = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null));

            for (int i = 0; i < tbl_pricing_winter.getRowCount();i++)
            {
                if (tbl_pricing_winter.getValueAt(i,1) != null)
                {
                    int typeIndexInDataBase = pansionTypes.indexOf(tbl_pricing_winter.getValueAt(i,0).toString());
                    adultPrices.set(typeIndexInDataBase, Integer.parseInt(tbl_pricing_winter.getValueAt(i,1).toString()));
                }

                if (tbl_pricing_winter.getValueAt(i,2) != null)
                {
                    int typeIndexInDataBase = pansionTypes.indexOf(tbl_pricing_winter.getValueAt(i,0).toString());
                    childPrices.set(typeIndexInDataBase, Integer.parseInt(tbl_pricing_winter.getValueAt(i,2).toString()));
                }
            }

            if (!Hotel.setPrices(selected_hotel_id,adultPrices,childPrices,"winter")) Helper.showMessage("Fiyatlar güncellenemedi.", "Başarısız!");

        });

        mdl_pricing_summer.addTableModelListener(e ->{
            if (e.getType() == TableModelEvent.UPDATE)
            {
                if (tbl_pricing_summer.getValueAt(e.getFirstRow(),e.getColumn()) != null)
                {
                    if (tbl_pricing_summer.getValueAt(e.getFirstRow(),e.getColumn()).toString().matches(".*[^0-9]+.*"))
                    {
                        Helper.showMessage("Sadece sayı girebilirsiniz.", "Uygunsuz giriş!");
                        tbl_pricing_summer.setValueAt(null,e.getFirstRow(),e.getColumn());
                    }
                }

            }
        });

        mdl_pricing_winter.addTableModelListener(e ->{
            if (e.getType() == TableModelEvent.UPDATE)
            {
                if (tbl_pricing_winter.getValueAt(e.getFirstRow(),e.getColumn()) != null)
                {
                    if (tbl_pricing_winter.getValueAt(e.getFirstRow(),e.getColumn()).toString().matches(".*[^0-9]+.*"))
                    {
                        Helper.showMessage("Sadece sayı girebilirsiniz.", "Uygunsuz giriş!");
                        tbl_pricing_winter.setValueAt(null,e.getFirstRow(),e.getColumn());
                    }
                }

            }
        });

        // ### update prices

        // room buttons

        btn_add_room.addActionListener(e -> {
            if (selected_hotel_id == -1) return;
            AddEditRoomGUI addRoomGUI = new AddEditRoomGUI(selected_hotel_id, selected_hotel_name);
            addRoomGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateRooms();
                }
            });
        });

        btn_delete_room.addActionListener(e -> {
            if (!Helper.sure("Seçili odayı silmek istediğinize emin misiniz?")) return;
            int selected_row = tbl_rooms.getSelectedRow();
            if (selected_row == -1) return;
            int selected_room_id = (int) tbl_rooms.getValueAt(tbl_rooms.getSelectedRow(),0);
            if (selected_room_id >= 0)
            {
                if (!Room.delete(selected_room_id))
                {
                    Helper.showMessage("Oda Silinemedi.", "Oda Silme Başarısız.");
                    return;
                }
                updateRooms();
            }
        });
        btn_edit_room.addActionListener(e -> {
            int selected_row = tbl_rooms.getSelectedRow();
            if (selected_row == -1) return;
            int selected_room_id = (int) tbl_rooms.getValueAt(tbl_rooms.getSelectedRow(),0);
            if (selected_room_id >= 0)
            {
                AddEditRoomGUI editRoomGUI = new AddEditRoomGUI(selected_hotel_name,selected_room_id);
                editRoomGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateRooms();
                    }
                });
                updateRooms();
            }
        });
        // ### room buttons

        // ### HOTEL

        // USER

        mdl_users = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Object[] col_users = {"ID", "Kullanıcı Adı", "Şifre", "Kullanıcı Tipi"};
        mdl_users.setColumnIdentifiers(col_users);
        row_users = new Object[col_users.length];


        tbl_user.setModel(mdl_users);

        tbl_user.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_user.getTableHeader().setReorderingAllowed(false);

        updateUsers();

        btn_add_user.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_add_username) || Helper.isFieldEmpty(fld_add_pw))
            {
                Helper.showMessage("Gerekli alanları doldurunuz.", "Ekleme işlemi başarısız!");
                return;
            }
            if (!User.add(fld_add_username.getText(), String.valueOf(fld_add_pw.getText()), cmb_add_type.getSelectedItem().toString()))
            {
                Helper.showMessage("Kullanıcı ekleme işlemi başarısız.", "İşlem başarısız!");
                return;
            }
            updateUsers();
        });

        btn_user_edit.addActionListener(e -> {
            if (tbl_user.getSelectedRow() == -1) return;
            if (!User.update((Integer) tbl_user.getValueAt(tbl_user.getSelectedRow(),0), fld_edit_username.getText().toString(), fld_edit_password.getText().toString(), cmb_edit_type.getSelectedItem().toString()))
            {
                Helper.showMessage("Güncelleme işlemi başarısız.", "İşlem Başarısız!");
                return;
            }
            fld_edit_username.setText(null);
            fld_edit_password.setText(null);
            cmb_edit_type.setSelectedItem("Admin");
            updateUsers();
        });

        tbl_user.getSelectionModel().addListSelectionListener(e -> {
            if (tbl_user.getSelectedRow() == -1) return;
            fld_edit_username.setText((String) tbl_user.getValueAt(tbl_user.getSelectedRow(),1));
            fld_edit_password.setText((String) tbl_user.getValueAt(tbl_user.getSelectedRow(),2));
            cmb_edit_type.setSelectedItem(tbl_user.getValueAt(tbl_user.getSelectedRow(),3));
            fld_user_delete_id.setText(String.valueOf(tbl_user.getValueAt(tbl_user.getSelectedRow(),0)));
        });

        fld_srch_user.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ENTER)
                {
                    updateUsers(fld_srch_user.getText().trim());
                }
            }
        });

        btn_user_delete.addActionListener(e -> {
            try
            {
                if (!User.delete(Integer.parseInt(fld_user_delete_id.getText())))
                {
                    Helper.showMessage("Silme işlemi başarısız.", "İşlem başarısız!");
                    return;
                }
                fld_edit_username.setText(null);
                fld_edit_password.setText(null);
                cmb_edit_type.setSelectedItem("Admin");
                updateUsers();
            } catch (Exception ignore) {}

        });

        // ### USER

        btn_exit.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });
    }

    public void updateHotels()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_hotels.getModel();
        clearModel.setRowCount(0);

        for(Hotel hotel : Hotel.getList())
        {
            int i = 0;
            row_hotels[i++] = hotel.getId();
            row_hotels[i++] = hotel.getName();
            row_hotels[i++] = Helper.getCity(hotel.getCity_id());
            row_hotels[i++] = Helper.getDistrict(hotel.getDistrict_id());
            row_hotels[i++] = hotel.geteMail();
            row_hotels[i++] = hotel.getPhoneNo();
            row_hotels[i++] = hotel.getStar();
            mdl_hotels.addRow(row_hotels);
        }
    }

    public void updateHotels(String search)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_hotels.getModel();
        clearModel.setRowCount(0);

        for(Hotel hotel : Hotel.getList())
        {
            if (!hotel.getName().toLowerCase().contains(search.toLowerCase())) continue;
            int i = 0;
            row_hotels[i++] = hotel.getId();
            row_hotels[i++] = hotel.getName();
            row_hotels[i++] = Helper.getCity(hotel.getCity_id());
            row_hotels[i++] = Helper.getDistrict(hotel.getDistrict_id());
            row_hotels[i++] = hotel.geteMail();
            row_hotels[i++] = hotel.getPhoneNo();
            row_hotels[i++] = hotel.getStar();
            mdl_hotels.addRow(row_hotels);
        }
    }

    public void updateHotelStuff()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_features.getModel();
        clearModel.setRowCount(0);

        clearModel = (DefaultTableModel) tbl_types.getModel();
        clearModel.setRowCount(0);

        clearModel = (DefaultTableModel) tbl_pricing_summer.getModel();
        clearModel.setRowCount(0);

        clearModel = (DefaultTableModel) tbl_pricing_winter.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (int binary : Hotel.getFeatures(this.selected_hotel_id))
        {
            if (binary != 0)
            {
                row_features[0] = this.features[i];
                mdl_features.addRow(row_features);

            }
            i++;
        }

        i = 0;
        for (int binary : Hotel.getTypes(this.selected_hotel_id))
        {
            if (binary != 0)
            {
                row_types[0] = this.pansionTypes.get(i);
                mdl_types.addRow(row_types);
                mdl_pricing_winter.addRow(row_types);
                mdl_pricing_summer.addRow(row_types);
            }
            i++;
        }

    }

    public void updateSeasonLabels()
    {
        try{
            this.selected_hotel_id = Integer.parseInt(tbl_hotels.getValueAt(tbl_hotels.getSelectedRow(),0).toString());
            this.selected_hotel_name = (String) tbl_hotels.getValueAt(tbl_hotels.getSelectedRow(), 1);
            fld_hotel_name.setText(this.selected_hotel_name);
            ArrayList<String> seasons = Season.getSeason(this.selected_hotel_id);
            fld_summer.setText(seasons.get(0).split("/")[0].split("-")[0] + " " + Season.getMonth(Integer.parseInt(seasons.get(0).split("/")[0].split("-")[1])) + " - " +
                    seasons.get(0).split("/")[1].split("-")[0] + " " + Season.getMonth(Integer.parseInt(seasons.get(0).split("/")[1].split("-")[1]))
            );
            fld_winter.setText(seasons.get(1).split("/")[0].split("-")[0] + " " + Season.getMonth(Integer.parseInt(seasons.get(1).split("/")[0].split("-")[1])) + " - " +
                    seasons.get(1).split("/")[1].split("-")[0] + " " + Season.getMonth(Integer.parseInt(seasons.get(1).split("/")[1].split("-")[1]))
            );
        } catch (Exception ignore) { }
    }
    public void updatePrices()
    {
        ArrayList<Integer> winterPrices = Hotel.getPrices(this.selected_hotel_id,"winter");

        for (int i = 0; i < winterPrices.size() / 2;i++)
        {
            if (winterPrices.get(i) != 0)
            {
                int rowCount = tbl_pricing_winter.getRowCount();
                for (int k = 0; k < rowCount;k++)
                {
                    if (pansionTypes.get(i) == tbl_pricing_winter.getValueAt(k,0))
                    {
                        tbl_pricing_winter.setValueAt(winterPrices.get(i),k,1);
                    }
                }
            }
        }

        for (int i = winterPrices.size() / 2; i < winterPrices.size();i++)
        {
            if (winterPrices.get(i) != 0)
            {
                int rowCount = tbl_pricing_winter.getRowCount();
                for (int k = 0; k < rowCount;k++)
                {
                    if (pansionTypes.get(i - 7) == tbl_pricing_winter.getValueAt(k,0))
                    {
                        tbl_pricing_winter.setValueAt(winterPrices.get(i),k,2);
                    }
                }
            }
        }

        ArrayList<Integer> summerPrices = Hotel.getPrices(this.selected_hotel_id,"summer");


        for (int i = 0; i < summerPrices.size() / 2;i++)
        {
            if (summerPrices.get(i) != 0)
            {
                int rowCount = tbl_pricing_summer.getRowCount();
                for (int k = 0; k < rowCount;k++)
                {
                    if (pansionTypes.get(i) == tbl_pricing_summer.getValueAt(k,0))
                    {
                        tbl_pricing_summer.setValueAt(summerPrices.get(i),k,1);
                    }
                }
            }
        }

        for (int i = summerPrices.size() / 2; i < summerPrices.size();i++)
        {
            if (summerPrices.get(i) != 0)
            {
                int rowCount = tbl_pricing_summer.getRowCount();
                for (int k = 0; k < rowCount;k++)
                {
                    if (pansionTypes.get(i - 7) == tbl_pricing_summer.getValueAt(k,0))
                    {
                        tbl_pricing_summer.setValueAt(summerPrices.get(i),k,2);
                    }
                }
            }
        }
    }

    public void updateRooms()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_rooms.getModel();
        clearModel.setRowCount(0);

        for(Room room : Room.getList(this.selected_hotel_id))
        {
            int i = 0;
            row_rooms[i++] = room.getId();
            row_rooms[i++] = room.getName();
            row_rooms[i++] = room.getsMeter();
            row_rooms[i++] = room.getBed();
            row_rooms[i++] = room.isTv() ? "Var" : "Yok";
            row_rooms[i++] = room.isConsole() ? "Var" : "Yok";
            row_rooms[i++] = room.isProjection() ? "Var" : "Yok";
            row_rooms[i++] = room.isSafe() ? "Var" : "Yok";
            row_rooms[i++] = room.isMinibar() ? "Var" : "Yok";
            row_rooms[i++] = room.getStock();
            mdl_rooms.addRow(row_rooms);
        }
    }

    public void updateUsers()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user.getModel();
        clearModel.setRowCount(0);

        for (User user : User.getList())
        {
            int i = 0;
            row_users[i++] = user.getId();
            row_users[i++] = user.getUsername();
            row_users[i++] = user.getPassword();
            row_users[i++] = user.getType();
            mdl_users.addRow(row_users);
        }
    }

    public void updateUsers(String search)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user.getModel();
        clearModel.setRowCount(0);

        for (User user : User.getList())
        {
            if (!user.getUsername().toLowerCase().contains(search.toLowerCase())) continue;
            int i = 0;
            row_users[i++] = user.getId();
            row_users[i++] = user.getUsername();
            row_users[i++] = user.getPassword();
            row_users[i++] = user.getType();
            mdl_users.addRow(row_users);
        }
    }

}

package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.Room;

import javax.swing.*;

public class AddEditRoomGUI extends JFrame {

    JPanel wrapper;
    private JTextField fld_room_name;
    private JTextField fld_room_bed;
    private JTextField fld_room_area;
    private JCheckBox chck_tv;
    private JButton btn_save;
    private JLabel lbl_hotel_name;
    private JCheckBox chck_bar;
    private JCheckBox chck_safe;
    private JTextField fld_room_stock;
    private JCheckBox chck_console;
    private JCheckBox chck_projection;


    // ADD CONSTRUCTOR
    public AddEditRoomGUI(int hotel_id, String hotel_name)
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(650,400);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        lbl_hotel_name.setText(hotel_name);


        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_room_name) || Helper.isFieldEmpty(fld_room_area) || Helper.isFieldEmpty(fld_room_bed) || Helper.isFieldEmpty(fld_room_stock))
            {
                Helper.showMessage("Zorunlu alanları doldurunuz.", "Oda ekleme başarısız.");
                return;
            }
            if (fld_room_area.getText().matches(".*[^0-9]+.*") || fld_room_bed.getText().matches(".*[^0-9]+.*") || fld_room_stock.getText().matches(".*[^0-9]+.*"))
            {
                Helper.showMessage("Uygun olmayan değerler girdiniz", "Uygunsuz değerler!");
                return;
            }

            if (!Room.add(hotel_id, fld_room_name.getText(), Integer.parseInt(fld_room_stock.getText()), Integer.parseInt(fld_room_bed.getText()), Integer.parseInt(fld_room_area.getText()), chck_tv.isSelected(), chck_bar.isSelected(), chck_console.isSelected(), chck_safe.isSelected(), chck_projection.isSelected())) Helper.showMessage("Ekleme başarısız.", "İşlem başarısız!");
            else dispose();
        });
    }

    // EDIT CONSTRUCTOR
    public AddEditRoomGUI(String hotel_name, int room_id)
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(650,400);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        lbl_hotel_name.setText(hotel_name);

        Room room = Room.get(room_id);

        fld_room_name.setText(room.getName());
        fld_room_bed.setText(String.valueOf(room.getBed()));
        fld_room_stock.setText(String.valueOf(room.getStock()));
        fld_room_area.setText(String.valueOf(room.getsMeter()));
        chck_bar.setSelected(room.isMinibar());
        chck_console.setSelected(room.isConsole());
        chck_projection.setSelected(room.isProjection());
        chck_safe.setSelected(room.isSafe());
        chck_tv.setSelected(room.isTv());



        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_room_name) || Helper.isFieldEmpty(fld_room_area) || Helper.isFieldEmpty(fld_room_bed) || Helper.isFieldEmpty(fld_room_stock))
            {
                Helper.showMessage("Zorunlu alanları doldurunuz.", "Oda ekleme başarısız.");
                return;
            }
            if (fld_room_area.getText().matches(".*[^0-9]+.*") || fld_room_bed.getText().matches(".*[^0-9]+.*") || fld_room_stock.getText().matches(".*[^0-9]+.*"))
            {
                Helper.showMessage("Uygun olmayan değerler girdiniz", "Uygunsuz değerler!");
                return;
            }

            if (!Room.update(room_id, fld_room_name.getText(), Integer.parseInt(fld_room_stock.getText()), Integer.parseInt(fld_room_bed.getText()), Integer.parseInt(fld_room_area.getText()), chck_tv.isSelected(), chck_bar.isSelected(), chck_console.isSelected(), chck_safe.isSelected(), chck_projection.isSelected())) Helper.showMessage("Güncelleme başarısız.", "İşlem başarısız!");
            else dispose();
        });
    }



}

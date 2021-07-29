package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.Hotel;
import com.patikahotel.Model.Reservation;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ReservationGUI extends JFrame {

    private JPanel wrapper;
    private JTextField fld_contact_name;
    private JTextField fld_contact_number;
    private JTextField fld_contact_id;

    private JComboBox pansionCombo;
    private ArrayList<String> pansionTypes = new ArrayList<>(Arrays.asList("Ultra Her Şey Dahil", "Her Şey Dahil", "Oda Kahvaltı", "Tam Pansiyon", "Yarım Pansiyon", "Sadece Yatak", "Alkol Hariç Full"));
    private ArrayList<Integer> prices;

    // visitor information table
    private JTable tbl_visitor;
    private JScrollPane scrl_visitor;
    private JButton btn_finish_reservation;
    private JLabel lbl_day;
    private DefaultTableModel mdl_visitor;
    private Object[] row_visitor;
    // ### visitor information table

    public ReservationGUI(Date enter, Date exit, int adult, int child, int hotel_id, int room_id, String season, int day)
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(1000,500);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        lbl_day.setText("Gün sayısı: " + day);

        season = season.equals("Yaz") ? "summer" : "winter";

        prices = Hotel.getPrices(hotel_id, season);
        // visitor table


        mdl_visitor = new DefaultTableModel();

        Object[] col_room = {"Yaş Aralığı", "Ad, Soyad", "T.C. Kimlik", "Pansiyon Tipi", "Toplam Ücret"};
        mdl_visitor.setColumnIdentifiers(col_room);
        row_visitor = new Object[col_room.length];

        tbl_visitor.setModel(mdl_visitor);

        // ### visitor table


        // place visitors on table

        for (int i = 0; i < adult;i++)
        {
            row_visitor[0] = "Yetişkin";
            mdl_visitor.addRow(row_visitor);
        }

        for (int i = 0; i < child;i++)
        {
            row_visitor[0] = "Çocuk";
            mdl_visitor.addRow(row_visitor);
        }

        // ### place visitors on table

        pansionCombo = new JComboBox<>();
        int[] types = Hotel.getTypes(hotel_id);
        for (int i = 0; i < types.length;i++)
        {
            if (types[i] != 0)
            {
                pansionCombo.addItem(pansionTypes.get(i));
            }
        }
        tbl_visitor.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(pansionCombo));
        tbl_visitor.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 3)
                {

                    if (tbl_visitor.getValueAt(e.getFirstRow(),3) == null) return;
                    if (pansionTypes.indexOf(tbl_visitor.getValueAt(e.getFirstRow(),3).toString()) == -1) return;
                    if (tbl_visitor.getValueAt(e.getFirstRow(),0).toString().equals("Yetişkin"))
                    {
                        tbl_visitor.setValueAt(prices.get(pansionTypes.indexOf(tbl_visitor.getValueAt(e.getFirstRow(),3).toString())) * day, e.getFirstRow(),4);
                    }
                    else if (tbl_visitor.getValueAt(e.getFirstRow(),0).toString().equals("Çocuk"))
                    {
                        tbl_visitor.setValueAt(prices.get(pansionTypes.indexOf(tbl_visitor.getValueAt(e.getFirstRow(),3).toString())) * day, e.getFirstRow(),4);
                    }
                }
            }
        });

        btn_finish_reservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Helper.isFieldEmpty(fld_contact_name))
                {
                    Helper.showMessage("İletişime geçilecek kişinin adını giriniz!", "Kayıt tamamlanmadı.");
                }
                if (Helper.isFieldEmpty(fld_contact_id))
                {
                    Helper.showMessage("İletişime geçilecek kişinin T.C numarasını giriniz!", "Kayıt tamamlanmadı.");
                }
                if (fld_contact_id.getText().matches(".*[^0-9]+.*") || fld_contact_id.getText().length() != 11)
                {
                    Helper.showMessage("İletişime geçilecek kişinin T.C numarası hatalı!", "Kayıt tamamlanmadı.");
                }
                if (Helper.isFieldEmpty(fld_contact_number))
                {
                    Helper.showMessage("İletişime geçilecek kişinin telefon numarasını giriniz!", "Kayıt tamamlanmadı.");
                }
                if (fld_contact_number.getText().matches(".*[^0-9]+.*") || fld_contact_number.getText().length() < 10 || fld_contact_number.getText().length() > 15)
                {
                    Helper.showMessage("İletişime geçilecek kişinin telefon numarası hatalı!", "Kayıt tamamlanmadı.");
                }
                for (int i = 0; i < tbl_visitor.getRowCount(); i++)
                {
                    if (tbl_visitor.getValueAt(i,1) == null)
                    {
                        Helper.showMessage("Ad ve soyad kısmını eksiksiz giriniz!", "Kayıt tamamlanmadı.");
                        return;
                    }
                    if (tbl_visitor.getValueAt(i,1).toString().equals(""))
                    {
                        Helper.showMessage("Ad ve soyad kısmını eksiksiz giriniz!", "Kayıt tamamlanmadı.");return;
                    }
                    if (tbl_visitor.getValueAt(i,2) == null)
                    {
                        Helper.showMessage("T.C. kimlik numaralarını eksiksiz giriniz!", "Kayıt tamamlanmadı.");return;
                    }
                    if (tbl_visitor.getValueAt(i,2).toString().length() != 11 || tbl_visitor.getValueAt(i,2).toString().matches(".*[^0-9]+.*"))
                    {
                        Helper.showMessage("T.C. kimlik numaralarını eksiksiz giriniz!", "Kayıt tamamlanmadı.");return;
                    }
                    if (tbl_visitor.getValueAt(i,3) == null)
                    {
                        Helper.showMessage("Pansiyon tiplerini seçiniz!", "Kayıt tamamlanmadı.");return;
                    }
                    if (tbl_visitor.getValueAt(i,3).equals(""))
                    {
                        Helper.showMessage("Pansiyon tiplerini seçiniz!", "Kayıt tamamlanmadı.");return;
                    }
                    if (tbl_visitor.getValueAt(i,4) == null)
                    {
                        Helper.showMessage("Fiyat bilgilerinden bazıları eksiktir.", "Kayıt tamamlanmadı.");return;
                    }
                    try
                    {
                        Integer.parseInt(tbl_visitor.getValueAt(i,4).toString());
                    } catch (Exception ignore)
                    {
                        Helper.showMessage("Fiyat bilgilerinden bazıları hatalıdır.", "Kayıt tamamlanmadı.");
                        return;
                    } finally {
                        if (Integer.parseInt(tbl_visitor.getValueAt(i,4).toString()) == 0)
                        {
                            Helper.showMessage("Fiyat sıfır olamaz.", "Kayıt tamamlanmadı.");
                            return;
                        }
                    }
                }
                ArrayList<String> visitorNames = new ArrayList<>();
                ArrayList<String> visitorIDs = new ArrayList<>();
                ArrayList<Integer> visitorPansions = new ArrayList<>();
                int price = 0;
                for (int i = 0; i < tbl_visitor.getRowCount(); i++)
                {
                    visitorNames.add((String) tbl_visitor.getValueAt(i,1));
                    visitorIDs.add((String) tbl_visitor.getValueAt(i,2));
                    visitorPansions.add(pansionTypes.indexOf(tbl_visitor.getValueAt(i,3).toString()));
                        price += Integer.parseInt(tbl_visitor.getValueAt(i,4) + "");


                }


                if (Reservation.add(enter, exit, hotel_id, room_id, fld_contact_name.getText().trim(), fld_contact_number.getText().trim(),fld_contact_id.getText().trim(), price, visitorNames, visitorIDs, visitorPansions))
                {
                    dispose();
                }
                else
                {
                    Helper.showMessage("Rezervasyon tamamlanamadı.", "Hata!");
                }
            }
        });
    }


    void createUIComponents()
    {
        tbl_visitor = new JTable();
    }



}

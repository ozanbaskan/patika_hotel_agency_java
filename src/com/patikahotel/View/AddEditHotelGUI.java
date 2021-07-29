package com.patikahotel.View;

import com.patikahotel.Helper.Config;
import com.patikahotel.Helper.Helper;
import com.patikahotel.Model.Hotel;
import com.patikahotel.Model.Season;

import javax.swing.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class AddEditHotelGUI extends JFrame {

    private JPanel wrapper;
    private JTextField fld_name;
    private JComboBox<String> cmb_city;
    private JComboBox<String> cmb_district;
    private JTextField fld_address;
    private JTextField fld_phone;
    private JTextField fld_email;
    private JComboBox cmb_star;
    private JCheckBox feature1;
    private JCheckBox feature2;
    private JCheckBox feature3;
    private JCheckBox feature4;
    private JCheckBox feature5;
    private JCheckBox feature6;
    private JCheckBox feature7;
    private JCheckBox type1;
    private JCheckBox type2;
    private JCheckBox type3;
    private JCheckBox type4;
    private JCheckBox type5;
    private JCheckBox type6;
    private JCheckBox type7;
    private JPanel pnl_feature;
    private JPanel pnl_type;
    private JButton btn_save;
    private JComboBox cmb_summer_start_month;
    private JComboBox<Integer> cmb_summer_start_day;
    private JComboBox<Integer> cmb_summer_end_day;
    private JComboBox<String> cmb_summer_end_month;
    private JComboBox<String> cmb_winter_start_month;
    private JComboBox<String> cmb_winter_end_month;
    private JComboBox<Integer> cmb_winter_start_day;
    private JComboBox<Integer> cmb_winter_end_day;

    private ArrayList<Integer> types;
    private ArrayList<Integer> features;

    private String[] months = {"", "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

    // ADD CONSTRUCTOR
    public AddEditHotelGUI()
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(750,650);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        // comboboxes for districts and cities

        cmb_city.addItem("");
        cmb_district.setEnabled(false);

        // get cities and sort for combobox
        ArrayList<String> cities = Helper.getCities();
        Collections.sort(cities,new Comparator<String>(){
            @Override
            public int compare(String s1,String s2){
                Collator collator = Collator.getInstance(new Locale("tr", "TR"));
                return collator.compare(s1, s2);
            }
        });
        // ### get cities and sort for combobox

        for (String city : cities)
        {
            cmb_city.addItem(city);
        }

        cmb_city.addActionListener(e ->{
            cmb_district.setEnabled(true);
            cmb_district.removeAllItems();
            if (cmb_city.getSelectedIndex() != 0)
            {
                for (String district : Helper.getDistricts((String) cmb_city.getSelectedItem()))
                {
                    cmb_district.addItem(district);
                }
            }
            else
            {
                cmb_district.setEnabled(false);
            }

        });

        // ### comboboxes for districts and cities

        // type and feature panels

        pnl_feature.setBorder(BorderFactory.createBevelBorder(0));
        pnl_type.setBorder(BorderFactory.createBevelBorder(0));

        // ### type and feature panels

        // save button
        btn_save.addActionListener(e -> {
            // feature and type selection
            features = new ArrayList<>();
            features.add(Helper.convertToInt(feature1.isSelected()));
            features.add(Helper.convertToInt(feature2.isSelected()));
            features.add(Helper.convertToInt(feature3.isSelected()));
            features.add(Helper.convertToInt(feature4.isSelected()));
            features.add(Helper.convertToInt(feature5.isSelected()));
            features.add(Helper.convertToInt(feature6.isSelected()));
            features.add(Helper.convertToInt(feature7.isSelected()));

            types = new ArrayList<>();
            types.add(Helper.convertToInt(type1.isSelected()));
            types.add(Helper.convertToInt(type2.isSelected()));
            types.add(Helper.convertToInt(type3.isSelected()));
            types.add(Helper.convertToInt(type4.isSelected()));
            types.add(Helper.convertToInt(type5.isSelected()));
            types.add(Helper.convertToInt(type6.isSelected()));
            types.add(Helper.convertToInt(type7.isSelected()));
            // ### feature and type selection


            if (Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_address) || Helper.isFieldEmpty(fld_email)){
                Helper.showMessage("Zorunlu alanları doldurmadınız!", "İşlem gerçekleştirilemedi.");
            }
            else if ( cmb_city.getSelectedIndex() == 0)
            {
                Helper.showMessage("Şehir ve ilçe seçmediniz!", "İşlem gerçekleştirilemedi.");
            }
            else if (!Helper.checkPhone(fld_phone.getText()))
            {
                Helper.showMessage("Uygun olmayan bir telefon numarası girdiniz.", "İşlem Gerçekleştirilemedi");
            }
            else if (!Helper.checkEmail(fld_email.getText()))
            {
                Helper.showMessage("Uygun olmayan bir E-mail adresi girdiniz.", "İşlem Gerçekleştirilemedi");
            }
            else if (cmb_winter_end_day.getSelectedItem() == null)
            {
                Helper.showMessage("Dönem seçimini tamamlayın.", "Dönem seçimi tamamlanmadı!");
            }
            else
            {
                int city_id = Helper.getCityId((String) cmb_city.getSelectedItem());
                int district_id = Helper.getDistrictId((String) cmb_district.getSelectedItem());
                if (city_id == -1 || district_id == -1)
                {
                    Helper.warn("Şehir veya bölge seçilirken bir hata oluştu");
                }
                else
                {
                    if (Hotel.add(fld_name.getText().trim(),city_id,district_id,fld_address.getText().trim(),fld_email.getText().trim(),fld_phone.getText().trim().replaceAll(" +", ""), Integer.parseInt(cmb_star.getSelectedItem().toString().trim()),features,types,
                            Season.prepareString(cmb_summer_start_month.getSelectedIndex(), cmb_summer_start_day.getSelectedIndex() + 1,cmb_summer_end_month.getSelectedIndex(),cmb_summer_end_day.getSelectedIndex() + 1),
                            Season.prepareString(Season.getMonthIndex((String) cmb_winter_start_month.getSelectedItem()), (Integer) cmb_winter_start_day.getSelectedItem(),Season.getMonthIndex((String) cmb_winter_end_month.getSelectedItem()), (Integer) cmb_winter_end_day.getSelectedItem())))
                    {
                        dispose();
                    }
                    else
                    {
                        Helper.showMessage("Ekleme işlemi başarısız!", "İşlem Başarısız");
                        dispose();
                    }
                }

            }
        });
        // ### save button

        // season comboboxes

        cmb_summer_start_day.setEnabled(false);
        cmb_summer_end_month.setEnabled(false);
        cmb_summer_end_day.setEnabled(false);
        cmb_winter_start_month.setEnabled(false);
        cmb_winter_start_day.setEnabled(false);
        cmb_winter_end_month.setEnabled(false);
        cmb_winter_end_day.setEnabled(false);

        cmb_summer_start_month.addActionListener(e -> {
            if (cmb_summer_start_month.getSelectedIndex() <= 0)
            {
                cmb_summer_start_day.setEnabled(false);
                cmb_summer_start_day.removeAllItems();
                return;
            }
            cmb_summer_start_day.setEnabled(true);
            cmb_summer_start_day.removeAllItems();
            for (Integer day : Season.getDaysOfMonth(cmb_summer_start_month.getSelectedIndex()))
            {
                cmb_summer_start_day.addItem(day);
            }
            cmb_summer_start_day.setSelectedItem(1);
        });

        cmb_summer_start_day.addActionListener(e -> {
            if (cmb_summer_start_day.getSelectedIndex() < 0)
            {
                cmb_summer_end_month.setEnabled(false);
                cmb_summer_end_month.removeAllItems();
                return;
            }
            cmb_summer_end_month.setEnabled(true);
            cmb_summer_end_month.removeAllItems();
            for (String month : months)
            {
                cmb_summer_end_month.addItem(month);
            }
        });

        cmb_summer_end_month.addActionListener(e -> {
            if (cmb_summer_end_month.getSelectedIndex() <= 0)
            {
                cmb_summer_end_day.setEnabled(false);
                cmb_summer_end_day.removeAllItems();
                return;
            }
            cmb_summer_end_day.setEnabled(true);
            cmb_summer_end_day.removeAllItems();
            for (Integer day : Season.getDaysOfMonth(cmb_summer_end_month.getSelectedIndex()))
            {
                cmb_summer_end_day.addItem(day);
            }
            cmb_summer_end_day.setSelectedItem(1);
        });

        cmb_summer_end_day.addActionListener(e -> {
            if (cmb_summer_end_day.getSelectedIndex() < 0)
            {
                cmb_winter_start_month.setEnabled(false);
                cmb_winter_start_month.removeAllItems();
                return;
            }
            cmb_winter_start_month.setEnabled(true);
            cmb_winter_start_month.removeAllItems();

            if (cmb_summer_start_month.getSelectedIndex() < cmb_summer_end_month.getSelectedIndex())
            {
                for (int i = cmb_summer_end_month.getSelectedIndex();i <= 12;i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }

                for (int i = 1;i <= cmb_summer_start_month.getSelectedIndex();i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }

            }
            else if (cmb_summer_start_month.getSelectedIndex() == cmb_summer_end_month.getSelectedIndex())
            {
                if (cmb_summer_start_day.getSelectedIndex() <= cmb_summer_end_day.getSelectedIndex())
                {
                    for (int i = cmb_summer_end_month.getSelectedIndex();i <= 12;i++)
                    {
                        cmb_winter_start_month.addItem(months[i]);
                    }

                    for (int i = 1;i < cmb_summer_start_month.getSelectedIndex();i++)
                    {
                        cmb_winter_start_month.addItem(months[i]);
                    }
                }
                else
                {
                    cmb_winter_start_month.addItem((String) cmb_summer_start_month.getSelectedItem());
                }
            }
            else
            {
                for (int i = cmb_summer_end_month.getSelectedIndex(); i <= cmb_summer_start_month.getSelectedIndex();i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }
            }

        });

        cmb_winter_start_month.addActionListener(ev -> {
            if (cmb_winter_start_month.getSelectedIndex() < 0)
            {
                cmb_winter_start_day.setEnabled(false);
                cmb_winter_start_day.removeAllItems();
                return;
            }
            cmb_winter_start_day.setEnabled(true);
            cmb_winter_start_day.removeAllItems();
            if (cmb_winter_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
            {
                if ((Integer)cmb_summer_start_day.getSelectedItem() <= (Integer)cmb_summer_end_day.getSelectedItem() && cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
                {

                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }

                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }

                }
                else if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
                {
                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }
                }
                else
                {
                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }
                }
            }
            else if (cmb_winter_start_month.getSelectedItem() == cmb_summer_start_month.getSelectedItem())
            {
                for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                {
                    if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                    {
                        cmb_winter_start_day.addItem(day);
                    }
                }
            }
            else
            {
                for (Integer day : Season.getDaysOfMonth((String) cmb_summer_end_month.getSelectedItem()))
                {
                    cmb_winter_start_day.addItem(day);
                }
            }
        });

        cmb_winter_start_day.addActionListener(e -> {
            if (cmb_winter_start_day.getSelectedIndex() < 0)
            {
                cmb_winter_end_month.setEnabled(false);
                cmb_winter_end_month.removeAllItems();
                return;
            }
            cmb_winter_end_month.setEnabled(true);
            cmb_winter_end_month.removeAllItems();


            for (int i = cmb_winter_start_month.getSelectedIndex();i < cmb_winter_start_month.getItemCount();i++)
            {
                cmb_winter_end_month.addItem(cmb_winter_start_month.getItemAt(i));
            }
            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && cmb_winter_start_month.getSelectedItem() != cmb_summer_end_month.getSelectedItem())
            {
                cmb_winter_end_month.addItem((String)cmb_summer_start_month.getSelectedItem());
            }
            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && cmb_winter_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
            {
                if ((Integer) cmb_winter_start_day.getSelectedItem() < (Integer) cmb_summer_start_day.getSelectedItem())
                {
                    cmb_winter_end_month.removeAllItems();
                    cmb_winter_end_month.addItem((String) cmb_summer_start_month.getSelectedItem());
                }
            }
            cmb_winter_end_month.setSelectedItem(0);
        });

        cmb_winter_end_month.addActionListener(e -> {
            if (cmb_winter_end_month.getSelectedIndex() < 0)
            {
                cmb_winter_end_day.setEnabled(false);
                cmb_winter_end_day.removeAllItems();
                return;
            }
            cmb_winter_end_day.setEnabled(true);
            cmb_winter_end_day.removeAllItems();

            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && (Integer) cmb_summer_start_day.getSelectedItem() > (Integer) cmb_summer_end_day.getSelectedItem())
            {
                for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                {
                    if (day >= (Integer) cmb_winter_start_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem() && day > (Integer) cmb_summer_end_day.getSelectedItem())
                    {
                        cmb_winter_end_day.addItem(day);
                    }
                }
            }
            else
            {
                    if (cmb_winter_start_month.getSelectedItem() == cmb_winter_end_month.getSelectedItem())
                    {
                        if (cmb_winter_start_month.getSelectedItem() == cmb_summer_start_month.getSelectedItem())
                        {
                            if ((Integer) cmb_winter_start_day.getSelectedItem() < (Integer) cmb_summer_start_day.getSelectedItem())
                            {
                                for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                                {
                                    if (day >= (Integer) cmb_winter_start_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem())
                                        cmb_winter_end_day.addItem(day);
                                }
                            }
                            else
                            {
                                for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                                {
                                    if (day >= (Integer) cmb_winter_start_day.getSelectedItem() || day < (Integer) cmb_summer_start_day.getSelectedItem())
                                        cmb_winter_end_day.addItem(day);
                                }
                            }
                        }
                        else
                        {
                            for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                            {
                                if (day >= (Integer) cmb_winter_start_day.getSelectedItem())
                                    cmb_winter_end_day.addItem(day);
                            }
                        }

                    }
                    else
                    {
                        if (cmb_summer_start_month.getSelectedItem() == cmb_winter_end_month.getSelectedItem())
                        {
                            for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                            {
                                if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                                cmb_winter_end_day.addItem(day);
                            }
                        }
                        else
                        {
                            for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                            {
                                cmb_winter_end_day.addItem(day);
                            }
                        }

                    }
            }
        });

        // ### season comboboxes
    }

    // EDIT CONSTRUCTOR
    public AddEditHotelGUI(int hotel_id)
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
        this.add(wrapper);
        this.setSize(750,650);
        this.setLocation(Helper.centerScreen("x", getSize()), Helper.centerScreen("y", getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(Config.PROJECT_TITLE);
        this.setVisible(true);

        // comboboxes for districts and cities

        cmb_city.addItem("");
        cmb_district.setEnabled(false);

        // get cities and sort for combobox
        ArrayList<String> cities = Helper.getCities();
        Collections.sort(cities,new Comparator<String>(){
            @Override
            public int compare(String s1,String s2){
                Collator collator = Collator.getInstance(new Locale("tr", "TR"));
                return collator.compare(s1, s2);
            }
        });
        // ### get cities and sort for combobox

        for (String city : cities)
        {
            cmb_city.addItem(city);
        }

        cmb_city.addActionListener(e ->{
            cmb_district.setEnabled(true);
            cmb_district.removeAllItems();
            if (cmb_city.getSelectedIndex() != 0)
            {
                for (String district : Helper.getDistricts((String) cmb_city.getSelectedItem()))
                {
                    cmb_district.addItem(district);
                }
            }
            else
            {
                cmb_district.setEnabled(false);
            }

        });

        // ### comboboxes for districts and cities

        // type and feature panels

        pnl_feature.setBorder(BorderFactory.createBevelBorder(0));
        pnl_type.setBorder(BorderFactory.createBevelBorder(0));

        // ### type and feature panels

        // set values to edit

        Hotel hotel = Hotel.get(hotel_id);
        fld_name.setText(hotel.getName());
        fld_address.setText(hotel.getAddress());
        fld_email.setText(hotel.geteMail());
        fld_phone.setText(hotel.getPhoneNo());
        cmb_city.setSelectedItem(Helper.getCity(hotel.getCity_id()));
        cmb_district.setSelectedItem(Helper.getDistrict(hotel.getDistrict_id()));
        cmb_star.setSelectedItem(((Integer)hotel.getStar()).toString());

        int[] hotel_features = Hotel.getFeatures(hotel_id);
        int[] hotel_types = Hotel.getTypes(hotel_id);

        feature1.setSelected(Helper.convertToBoolean(hotel_features[0]));
        feature2.setSelected(Helper.convertToBoolean(hotel_features[1]));
        feature3.setSelected(Helper.convertToBoolean(hotel_features[2]));
        feature4.setSelected(Helper.convertToBoolean(hotel_features[3]));
        feature5.setSelected(Helper.convertToBoolean(hotel_features[4]));
        feature6.setSelected(Helper.convertToBoolean(hotel_features[5]));
        feature7.setSelected(Helper.convertToBoolean(hotel_features[6]));

        type1.setSelected(Helper.convertToBoolean(hotel_types[0]));
        type2.setSelected(Helper.convertToBoolean(hotel_types[1]));
        type3.setSelected(Helper.convertToBoolean(hotel_types[2]));
        type4.setSelected(Helper.convertToBoolean(hotel_types[3]));
        type5.setSelected(Helper.convertToBoolean(hotel_types[4]));
        type6.setSelected(Helper.convertToBoolean(hotel_types[5]));
        type7.setSelected(Helper.convertToBoolean(hotel_types[6]));

        // ## set values to edit

        // save button
        btn_save.addActionListener(e -> {
            // feature and type selection
            features = new ArrayList<>();
            features.add(Helper.convertToInt(feature1.isSelected()));
            features.add(Helper.convertToInt(feature2.isSelected()));
            features.add(Helper.convertToInt(feature3.isSelected()));
            features.add(Helper.convertToInt(feature4.isSelected()));
            features.add(Helper.convertToInt(feature5.isSelected()));
            features.add(Helper.convertToInt(feature6.isSelected()));
            features.add(Helper.convertToInt(feature7.isSelected()));

            types = new ArrayList<>();
            types.add(Helper.convertToInt(type1.isSelected()));
            types.add(Helper.convertToInt(type2.isSelected()));
            types.add(Helper.convertToInt(type3.isSelected()));
            types.add(Helper.convertToInt(type4.isSelected()));
            types.add(Helper.convertToInt(type5.isSelected()));
            types.add(Helper.convertToInt(type6.isSelected()));
            types.add(Helper.convertToInt(type7.isSelected()));
            // ### feature and type selection


            if (Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_address) || Helper.isFieldEmpty(fld_email)){
                Helper.showMessage("Zorunlu alanları doldurmadınız!", "İşlem gerçekleştirilemedi.");
            }
            else if ( cmb_city.getSelectedIndex() == 0)
            {
                Helper.showMessage("Şehir ve ilçe seçmediniz!", "İşlem gerçekleştirilemedi.");
            }
            else if (!Helper.checkPhone(fld_phone.getText()))
            {
                Helper.showMessage("Uygun olmayan bir telefon numarası girdiniz.", "İşlem Gerçekleştirilemedi");
            }
            else if (!Helper.checkEmail(fld_email.getText()))
            {
                Helper.showMessage("Uygun olmayan bir E-mail adresi girdiniz.", "İşlem Gerçekleştirilemedi");
            }
            else if (cmb_winter_end_day.getSelectedItem().toString() == "")
            {
                Helper.showMessage("Dönem seçimini tamamlayın.", "Dönem seçimi tamamlanmadı!");
            }
            else
            {
                int city_id = Helper.getCityId((String) cmb_city.getSelectedItem());
                int district_id = Helper.getDistrictId((String) cmb_district.getSelectedItem());
                if (city_id == -1 || district_id == -1)
                {
                    Helper.warn("Şehir veya bölge seçilirken bir hata oluştu");
                }
                else
                {
                    if (Hotel.update(hotel_id, fld_name.getText().trim(),city_id,district_id,fld_address.getText().trim(),fld_email.getText().trim(),fld_phone.getText().trim().replaceAll(" +", ""), Integer.parseInt(cmb_star.getSelectedItem().toString().trim()),features,types,
                            Season.prepareString(cmb_summer_start_month.getSelectedIndex(), cmb_summer_start_day.getSelectedIndex() + 1,cmb_summer_end_month.getSelectedIndex(),cmb_summer_end_day.getSelectedIndex() + 1),
                            Season.prepareString(Season.getMonthIndex((String) cmb_winter_start_month.getSelectedItem()), (Integer) cmb_winter_start_day.getSelectedItem(),Season.getMonthIndex((String) cmb_winter_end_month.getSelectedItem()), (Integer) cmb_winter_end_day.getSelectedItem())))
                    {
                        dispose();
                    }
                    else
                    {
                        Helper.showMessage("Düzenleme işlemi başarısız!", "İşlem Başarısız");
                        dispose();
                    }
                }

            }
        });
        // ### save button

        // season comboboxes

        cmb_summer_start_day.setEnabled(false);
        cmb_summer_end_month.setEnabled(false);
        cmb_summer_end_day.setEnabled(false);
        cmb_winter_start_month.setEnabled(false);
        cmb_winter_start_day.setEnabled(false);
        cmb_winter_end_month.setEnabled(false);
        cmb_winter_end_day.setEnabled(false);

        cmb_summer_start_month.addActionListener(e -> {
            if (cmb_summer_start_month.getSelectedIndex() <= 0)
            {
                cmb_summer_start_day.setEnabled(false);
                cmb_summer_start_day.removeAllItems();
                return;
            }
            cmb_summer_start_day.setEnabled(true);
            cmb_summer_start_day.removeAllItems();
            for (Integer day : Season.getDaysOfMonth(cmb_summer_start_month.getSelectedIndex()))
            {
                cmb_summer_start_day.addItem(day);
            }
            cmb_summer_start_day.setSelectedItem(1);
        });

        cmb_summer_start_day.addActionListener(e -> {
            if (cmb_summer_start_day.getSelectedIndex() < 0)
            {
                cmb_summer_end_month.setEnabled(false);
                cmb_summer_end_month.removeAllItems();
                return;
            }
            cmb_summer_end_month.setEnabled(true);
            cmb_summer_end_month.removeAllItems();
            for (String month : months)
            {
                cmb_summer_end_month.addItem(month);
            }
        });

        cmb_summer_end_month.addActionListener(e -> {
            if (cmb_summer_end_month.getSelectedIndex() <= 0)
            {
                cmb_summer_end_day.setEnabled(false);
                cmb_summer_end_day.removeAllItems();
                return;
            }
            cmb_summer_end_day.setEnabled(true);
            cmb_summer_end_day.removeAllItems();
            for (Integer day : Season.getDaysOfMonth(cmb_summer_end_month.getSelectedIndex()))
            {
                cmb_summer_end_day.addItem(day);
            }
            cmb_summer_end_day.setSelectedItem(1);
        });

        cmb_summer_end_day.addActionListener(e -> {
            if (cmb_summer_end_day.getSelectedIndex() < 0)
            {
                cmb_winter_start_month.setEnabled(false);
                cmb_winter_start_month.removeAllItems();
                return;
            }
            cmb_winter_start_month.setEnabled(true);
            cmb_winter_start_month.removeAllItems();

            if (cmb_summer_start_month.getSelectedIndex() < cmb_summer_end_month.getSelectedIndex())
            {
                for (int i = cmb_summer_end_month.getSelectedIndex();i <= 12;i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }

                for (int i = 1;i <= cmb_summer_start_month.getSelectedIndex();i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }

            }
            else if (cmb_summer_start_month.getSelectedIndex() == cmb_summer_end_month.getSelectedIndex())
            {
                if (cmb_summer_start_day.getSelectedIndex() <= cmb_summer_end_day.getSelectedIndex())
                {
                    for (int i = cmb_summer_end_month.getSelectedIndex();i <= 12;i++)
                    {
                        cmb_winter_start_month.addItem(months[i]);
                    }

                    for (int i = 1;i < cmb_summer_start_month.getSelectedIndex();i++)
                    {
                        cmb_winter_start_month.addItem(months[i]);
                    }
                }
                else
                {
                    cmb_winter_start_month.addItem((String) cmb_summer_start_month.getSelectedItem());
                }
            }
            else
            {
                for (int i = cmb_summer_end_month.getSelectedIndex(); i <= cmb_summer_start_month.getSelectedIndex();i++)
                {
                    cmb_winter_start_month.addItem(months[i]);
                }
            }

        });

        cmb_winter_start_month.addActionListener(ev -> {
            if (cmb_winter_start_month.getSelectedIndex() < 0)
            {
                cmb_winter_start_day.setEnabled(false);
                cmb_winter_start_day.removeAllItems();
                return;
            }
            cmb_winter_start_day.setEnabled(true);
            cmb_winter_start_day.removeAllItems();
            if (cmb_winter_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
            {
                if ((Integer)cmb_summer_start_day.getSelectedItem() <= (Integer)cmb_summer_end_day.getSelectedItem() && cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
                {

                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }

                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }

                }
                else if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
                {
                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }
                }
                else
                {
                    for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                    {
                        if (day > (Integer) cmb_summer_end_day.getSelectedItem())
                        {
                            cmb_winter_start_day.addItem(day);
                        }
                    }
                }
            }
            else if (cmb_winter_start_month.getSelectedItem() == cmb_summer_start_month.getSelectedItem())
            {
                for (Integer day : Season.getDaysOfMonth((String) cmb_winter_start_month.getSelectedItem()))
                {
                    if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                    {
                        cmb_winter_start_day.addItem(day);
                    }
                }
            }
            else
            {
                for (Integer day : Season.getDaysOfMonth((String) cmb_summer_end_month.getSelectedItem()))
                {
                    cmb_winter_start_day.addItem(day);
                }
            }
        });

        cmb_winter_start_day.addActionListener(e -> {
            if (cmb_winter_start_day.getSelectedIndex() < 0)
            {
                cmb_winter_end_month.setEnabled(false);
                cmb_winter_end_month.removeAllItems();
                return;
            }
            cmb_winter_end_month.setEnabled(true);
            cmb_winter_end_month.removeAllItems();


            for (int i = cmb_winter_start_month.getSelectedIndex();i < cmb_winter_start_month.getItemCount();i++)
            {
                cmb_winter_end_month.addItem(cmb_winter_start_month.getItemAt(i));
            }
            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && cmb_winter_start_month.getSelectedItem() != cmb_summer_end_month.getSelectedItem())
            {
                cmb_winter_end_month.addItem((String)cmb_summer_start_month.getSelectedItem());
            }
            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && cmb_winter_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem())
            {
                if ((Integer) cmb_winter_start_day.getSelectedItem() < (Integer) cmb_summer_start_day.getSelectedItem())
                {
                    cmb_winter_end_month.removeAllItems();
                    cmb_winter_end_month.addItem((String) cmb_summer_start_month.getSelectedItem());
                }
            }
            cmb_winter_end_month.setSelectedItem(0);
        });

        cmb_winter_end_month.addActionListener(e -> {
            if (cmb_winter_end_month.getSelectedIndex() < 0)
            {
                cmb_winter_end_day.setEnabled(false);
                cmb_winter_end_day.removeAllItems();
                return;
            }
            cmb_winter_end_day.setEnabled(true);
            cmb_winter_end_day.removeAllItems();

            if (cmb_summer_start_month.getSelectedItem() == cmb_summer_end_month.getSelectedItem() && (Integer) cmb_summer_start_day.getSelectedItem() > (Integer) cmb_summer_end_day.getSelectedItem())
            {
                for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                {
                    if (day >= (Integer) cmb_winter_start_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem() && day > (Integer) cmb_summer_end_day.getSelectedItem())
                    {
                        cmb_winter_end_day.addItem(day);
                    }
                }
            }
            else
            {
                if (cmb_winter_start_month.getSelectedItem() == cmb_winter_end_month.getSelectedItem())
                {
                    if (cmb_winter_start_month.getSelectedItem() == cmb_summer_start_month.getSelectedItem())
                    {
                        if ((Integer) cmb_winter_start_day.getSelectedItem() < (Integer) cmb_summer_start_day.getSelectedItem())
                        {
                            for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                            {
                                if (day >= (Integer) cmb_winter_start_day.getSelectedItem() && day < (Integer) cmb_summer_start_day.getSelectedItem())
                                    cmb_winter_end_day.addItem(day);
                            }
                        }
                        else
                        {
                            for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                            {
                                if (day >= (Integer) cmb_winter_start_day.getSelectedItem() || day < (Integer) cmb_summer_start_day.getSelectedItem())
                                    cmb_winter_end_day.addItem(day);
                            }
                        }
                    }
                    else
                    {
                        for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                        {
                            if (day >= (Integer) cmb_winter_start_day.getSelectedItem())
                                cmb_winter_end_day.addItem(day);
                        }
                    }

                }
                else
                {
                    if (cmb_summer_start_month.getSelectedItem() == cmb_winter_end_month.getSelectedItem())
                    {
                        for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                        {
                            if (day < (Integer) cmb_summer_start_day.getSelectedItem())
                                cmb_winter_end_day.addItem(day);
                        }
                    }
                    else
                    {
                        for (Integer day : Season.getDaysOfMonth((String)cmb_winter_end_month.getSelectedItem()))
                        {
                            cmb_winter_end_day.addItem(day);
                        }
                    }

                }
            }
        });

        // ### season comboboxes

        // get seasons of the hotel

        ArrayList<String> summerAndWinter = Season.getSeason(hotel_id);
        cmb_summer_start_month.setSelectedIndex(Integer.parseInt(summerAndWinter.get(0).split("-")[1].split("/")[0]));
        cmb_summer_start_day.setSelectedItem(Integer.parseInt(summerAndWinter.get(0).split("-")[0]));
        cmb_summer_end_month.setSelectedIndex(Integer.parseInt(summerAndWinter.get(0).split("/")[1].split("-")[1]));
        cmb_summer_end_day.setSelectedItem(Integer.parseInt(summerAndWinter.get(0).split("/")[1].split("-")[0]));

        cmb_winter_start_month.setSelectedItem(months[Integer.parseInt(summerAndWinter.get(1).split("-")[1].split("/")[0])]);
        cmb_winter_start_day.setSelectedItem(Integer.parseInt(summerAndWinter.get(1).split("-")[0]));
        cmb_winter_end_month.setSelectedItem(months[Integer.parseInt(summerAndWinter.get(1).split("/")[1].split("-")[1])]);
        cmb_winter_end_day.setSelectedItem(Integer.parseInt(summerAndWinter.get(1).split("/")[1].split("-")[0]));

        // ### get seasons of the hotel
    }


}

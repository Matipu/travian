package org.example.gui;

import org.example.Troops;
import org.example.UnitInfo;
import org.example.gui.listner.TownChangeListener;
import org.example.gui.listner.WojskaCheckBoxListener;
import org.example.gui.listner.WojskaListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.example.Calcuations.getAllTownNames;
import static org.example.Utils.loadTroopsFromTownFile;

public class WyborWojskaComponent {
    public JLabel t1Label;
    public JTextField t1Text;
    public JLabel t2Label;
    public JTextField t2Text;
    public JLabel t3Label;
    public JTextField t3Text;
    public JLabel t4Label;
    public JTextField t4Text;
    public JLabel t5Label;
    public JTextField t5Text;
    public JCheckBox sentArmyCheckBox;
    public JComboBox cTowns;

    public WyborWojskaComponent(Container contentPane, UnitInfo unitInfo) throws IOException {

        JLabel townLabel = new JLabel();
        townLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add text to label
        townLabel.setText("wybierz miasto");
        contentPane.add(townLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));

        cTowns = new JComboBox(getAllTownNames());
        cTowns.setMaximumSize(cTowns.getPreferredSize());
        contentPane.add(cTowns);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel informLabel = new JLabel();
        informLabel.setText("Aktualne wojska do farmienia");
        contentPane.add(informLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        t1Label = new JLabel();
        t1Text = new JTextField(30);
        t1Label.setText(unitInfo.t1Name);
        t1Text.setMaximumSize(t1Text.getPreferredSize());
        contentPane.add(t1Label);
        contentPane.add(t1Text);

        t2Label = new JLabel();
        t2Text = new JTextField(30);
        t2Label.setText(unitInfo.t2Name);
        t2Text.setMaximumSize(t2Text.getPreferredSize());
        contentPane.add(t2Label);
        contentPane.add(t2Text);

        t3Label = new JLabel();
        t3Text = new JTextField(30);
        t3Label.setText(unitInfo.t3Name);
        t3Text.setMaximumSize(t3Text.getPreferredSize());
        contentPane.add(t3Label);
        contentPane.add(t3Text);

        t4Label = new JLabel();
        t4Text = new JTextField(30);
        t4Label.setText(unitInfo.t4Name);
        t4Text.setMaximumSize(t4Text.getPreferredSize());
        contentPane.add(t4Label);
        contentPane.add(t4Text);

        t5Label = new JLabel();
        t5Text = new JTextField(30);
        t5Label.setText(unitInfo.t5Name);
        t5Text.setMaximumSize(t5Text.getPreferredSize());
        contentPane.add(t5Label);
        contentPane.add(t5Text);

        t1Text.setText("0");
        t2Text.setText("0");
        t3Text.setText("0");
        t4Text.setText("0");
        t5Text.setText("0");

        sentArmyCheckBox = new JCheckBox("Wysyłać wojska w paczce");
        contentPane.add(sentArmyCheckBox);

        t1Text.getDocument().addDocumentListener(new WojskaListener(this));
        t2Text.getDocument().addDocumentListener(new WojskaListener(this));
        t3Text.getDocument().addDocumentListener(new WojskaListener(this));
        t4Text.getDocument().addDocumentListener(new WojskaListener(this));
        t5Text.getDocument().addDocumentListener(new WojskaListener(this));
        sentArmyCheckBox.addActionListener(new WojskaCheckBoxListener(this));
        cTowns.addActionListener (new TownChangeListener(this));
        loadFromFile();
    }

    public String getT1Text() {
        return t1Text.getText();
    }

    public String getT2Text() {
        return t2Text.getText();
    }

    public String getT3Text() {
        return t3Text.getText();
    }

    public String getT4Text() {
        return t4Text.getText();
    }

    public String getT5Text() {
        return t5Text.getText();
    }

    public Troops getActualTroops() {
        try {
            return new Troops(
                    Integer.valueOf(getT1Text()),
                    Integer.valueOf(getT2Text()),
                    Integer.valueOf(getT3Text()),
                    Integer.valueOf(getT4Text()),
                    Integer.valueOf(getT5Text()),
                    0,
                    sentArmyCheckBox.isSelected());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getTownName() {
        return String.valueOf(cTowns.getSelectedItem());
    }

    public void loadFromFile() throws IOException {
        String townName = getTownName();
        Troops troops = loadTroopsFromTownFile(townName);

        t1Text.setText(String.valueOf(troops.T1));
        t2Text.setText(String.valueOf(troops.T2));
        t3Text.setText(String.valueOf(troops.T3));
        t4Text.setText(String.valueOf(troops.T4));
        t5Text.setText(String.valueOf(troops.T5));
        sentArmyCheckBox.setSelected(troops.sendTroops);
    }
}
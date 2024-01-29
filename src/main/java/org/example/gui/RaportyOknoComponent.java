package org.example.gui;

import org.example.Config;
import org.example.Troops;
import org.example.UnitInfo;
import org.example.gui.listner.TownChangeListener;
import org.example.gui.listner.TownChangeReportsListener;
import org.example.gui.listner.WojskaCheckBoxListener;
import org.example.gui.listner.WojskaListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.Calcuations.getAllTownNames;
import static org.example.Utils.loadTroopsFromTownFile;

public class RaportyOknoComponent {
    public JComboBox cTowns;
    public Config config;
    public List<JLabel> labels = new ArrayList<>();
    public Container contentPanel;

    public RaportyOknoComponent(Container contentPanel, Config config) {
        this.contentPanel = contentPanel;
        this.config = config;
        JLabel townLabel = new JLabel();
        townLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add text to label
        townLabel.setText("wybierz miasto");
        this.contentPanel.add(townLabel);
        this.contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        cTowns = new JComboBox(getAllTownNames());
        cTowns.setMaximumSize(cTowns.getPreferredSize());
        this.contentPanel.add(cTowns);
        this.contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel informLabel = new JLabel();
        informLabel.setText("Aktualne farmy");
        this.contentPanel.add(informLabel);
        this.contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cTowns.addActionListener (new TownChangeReportsListener(this));
    }

    public void addLabel(String text) {
        JLabel label = new JLabel();
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add text to label
        label.setText(text);
        this.contentPanel.add(label);
        this.contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        labels.add(label);

        this.contentPanel.doLayout();
    }

    public String getTownName() {
        return String.valueOf(cTowns.getSelectedItem());
    }

    public void removeAllLabels() {
        for (JLabel label: labels) {
            this.contentPanel.remove(label);
        }
        labels.clear();
    }
}
package org.example.gui.listner;

import org.example.gui.WyborWojskaComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TownChangeListener implements ActionListener {

    public WyborWojskaComponent wyborWojskaComponent;

    public TownChangeListener(WyborWojskaComponent wyborWojskaComponent) {
        this.wyborWojskaComponent = wyborWojskaComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            wyborWojskaComponent.loadFromFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
package org.example.gui.listner;

import org.example.Utils;
import org.example.gui.WyborWojskaComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WojskaCheckBoxListener implements ActionListener {

    public WyborWojskaComponent wyborWojskaComponent;

    public WojskaCheckBoxListener(WyborWojskaComponent wyborWojskaComponent) {
        this.wyborWojskaComponent = wyborWojskaComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Utils.updateTownFile(wyborWojskaComponent);
    }
}
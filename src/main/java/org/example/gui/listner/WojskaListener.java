package org.example.gui.listner;

import org.example.Utils;
import org.example.gui.WyborWojskaComponent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WojskaListener implements DocumentListener {

    public WyborWojskaComponent wyborWojskaComponent;

    public WojskaListener(WyborWojskaComponent wyborWojskaComponent) {
        this.wyborWojskaComponent = wyborWojskaComponent;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        Utils.updateTownFile(wyborWojskaComponent);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        Utils.updateTownFile(wyborWojskaComponent);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Utils.updateTownFile(wyborWojskaComponent);
    }
}
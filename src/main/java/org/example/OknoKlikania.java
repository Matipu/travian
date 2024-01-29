package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static java.awt.event.InputEvent.BUTTON1_MASK;
import static java.awt.event.KeyEvent.*;

class Listener implements HotKeyListener {

    OknoKlikania oknoKlikania;
    Listener(OknoKlikania oknoKlikania) {
        this.oknoKlikania = oknoKlikania;
    }

    @Override
    public void onHotKey(HotKey hotKey) {
        oknoKlikania.stopClicking = true;
    }
}

class Mouse1Listener implements HotKeyListener {

    MouseClickerConfig mouseConfig;
    OknoKlikania oknoKlikania;

    public Mouse1Listener(MouseClickerConfig mouseConfig, OknoKlikania oknoKlikania) {
        this.mouseConfig = mouseConfig;
        this.oknoKlikania = oknoKlikania;
    }

    @Override
    public void onHotKey(HotKey hotKey) {
        PointerInfo a = MouseInfo.getPointerInfo();
        mouseConfig.x1 = (int) a.getLocation().getX();
        mouseConfig.y1 = (int) a.getLocation().getY();
        oknoKlikania.updateMouseText1();
    }
}

class Mouse2Listener implements HotKeyListener {

    MouseClickerConfig mouseConfig;
    OknoKlikania oknoKlikania;

    public Mouse2Listener(MouseClickerConfig mouseConfig, OknoKlikania oknoKlikania) {
        this.mouseConfig = mouseConfig;
        this.oknoKlikania = oknoKlikania;
    }

    @Override
    public void onHotKey(HotKey hotKey) {
        PointerInfo a = MouseInfo.getPointerInfo();
        mouseConfig.x2 = (int) a.getLocation().getX();
        mouseConfig.y2 = (int) a.getLocation().getY();
        oknoKlikania.updateMouseText2();
    }
}

public class OknoKlikania {

    public boolean stopClicking = false;
    MouseClickerConfig mouseConfig;
    Robot robot;
    public JLabel t1Label;
    public JLabel t2Label;
    JTextField t1Text;
    Random rand = new Random();

    public void budujOknoKlikania(Container contentPane) throws AWTException, IOException {
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke("shift Z"), new Listener(this));

        robot = new Robot();
        String unitInfoJson = Utils.loadStringFromFile("config/MouseClickerConfig");
        mouseConfig = (new ObjectMapper()).readValue(unitInfoJson, MouseClickerConfig.class);
        provider.register(KeyStroke.getKeyStroke("shift T"), new Mouse1Listener(mouseConfig, this));
        provider.register(KeyStroke.getKeyStroke("shift Y"), new Mouse2Listener(mouseConfig, this));

        t1Label = new JLabel();
        updateMouseText1();
        contentPane.add(t1Label);

        t2Label = new JLabel();
        updateMouseText2();
        contentPane.add(t2Label);

        t1Text = new JTextField(30);
        t1Text.setMaximumSize(t1Text.getPreferredSize());
        contentPane.add(t1Text);
        t1Text.setText("0");

        JButton button = new JButton();
        button.setText("Klikaj wysylanie wojsk");
        contentPane.add(button);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        button.addActionListener(e -> clickSendArmy());

        JButton button2 = new JButton();
        button2.setText("Klikaj raporty");
        contentPane.add(button2);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        button2.addActionListener(e -> clickReport());
    }

    public void updateMouseText1() {
        t1Label.setText("reguluj pierwsze położenie myszki (shift + t) X: " + mouseConfig.x1 + " Y: " + mouseConfig.y1);
    }


    public void updateMouseText2() {
        t2Label.setText("reguluj drugie położenie myszki (shift + y) X: " + mouseConfig.x2 + " Y: " + mouseConfig.y2);
    }

    private void clickReport() {
        mouseConfig.count = Integer.parseInt(t1Text.getText());
        for (int i = 0; i < mouseConfig.count; i++) {
            robot.mouseMove(869 + rand.nextInt(10), 530 + rand.nextInt(7));
            clickLeftMouse();

            robot.delay(400 + rand.nextInt(50));

            if(stopClicking) {
                stopClicking = false;
                return;
            }
        }

    }

    public void delay(Integer ms) {
        robot.delay(ms + rand.nextInt(100));
    }

    public void clickOnMousePosition(Integer x, Integer y) {
        robot.mouseMove(x + rand.nextInt(7), y + rand.nextInt(7));
        clickLeftMouse();
    }

    public void clickSendArmy() {
        mouseConfig.count = Integer.parseInt(t1Text.getText());
        if (klikajWysylanie()) return;
        ctrl2Click();
        klikajPotwierdzanie();

    }

    private boolean klikajWysylanie() {
        for (int i = 0; i < mouseConfig.count; i++) {
            robot.mouseMove(mouseConfig.x1 + rand.nextInt(50), mouseConfig.y1 + rand.nextInt(7));
            clickLeftMouse();

            robot.delay(10);

            ctrlTab();

            if(stopClicking) {
                stopClicking = false;
                return true;
            }

            robot.delay(100 + rand.nextInt(10));
        }
        return false;
    }

    private void klikajPotwierdzanie() {
        for (int i = 0; i < mouseConfig.count; i++) {

            robot.mouseMove(mouseConfig.x2 + rand.nextInt(40), mouseConfig.y2 + rand.nextInt(7));
            clickLeftMouse();

            robot.delay(20 + rand.nextInt(10));

            if(stopClicking) {
                stopClicking = false;
                return;
            }
            //ctrlTab();
            ctrlW();

            robot.delay(100 + rand.nextInt(10));
        }
    }

    private void ctrlW() {
        robot.keyPress(VK_CONTROL);
        robot.keyPress(VK_W);
        robot.delay(20 + rand.nextInt(10));
        robot.keyRelease(VK_W);
        robot.keyRelease(VK_CONTROL);
    }

    private void ctrlTab() {
        robot.keyPress(VK_CONTROL);
        robot.keyPress(VK_TAB);
        robot.delay(20 + rand.nextInt(10));
        robot.keyRelease(VK_TAB);
        robot.keyRelease(VK_CONTROL);
    }

    private void clickLeftMouse() {
        robot.mousePress(BUTTON1_MASK);
        robot.delay(20 + rand.nextInt(10));
        robot.mouseRelease(BUTTON1_MASK);
    }

    public void ctrl2Click() {
        robot.keyPress(VK_CONTROL);
        robot.keyPress(VK_2);
        robot.delay(20 + rand.nextInt(10));
        robot.keyRelease(VK_2);
        robot.keyRelease(VK_CONTROL);
        robot.delay(100 + rand.nextInt(20));
    }
}

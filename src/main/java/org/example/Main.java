package org.example;

import org.apache.commons.lang3.StringUtils;
import org.example.gui.RaportyOknoComponent;
import org.example.gui.WyborWojskaComponent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.example.Calcuations.getAllTownNames;
import static org.example.Utils.loadTroopsFromTownFile;

public class Main {

    static Config config;

    static OknoKlikania oknoKlikania;

    public static void main(String[] args) throws IOException, AWTException {
        config = new Config();
        JFrame frame = new JFrame("JFrame Example");
        frame.setSize(1920, 1080);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(
                new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS)
        );

        JPanel wysylkaWOjskaPanel = new JPanel();
        wysylkaWOjskaPanel.setLayout(
                new BoxLayout(wysylkaWOjskaPanel, BoxLayout.PAGE_AXIS)
        );
        JPanel opcjePanel = new JPanel();
        opcjePanel.setLayout(
                new BoxLayout(opcjePanel, BoxLayout.PAGE_AXIS)
        );
        JPanel klikaniePanel = new JPanel();
        klikaniePanel.setLayout(
                new BoxLayout(klikaniePanel, BoxLayout.PAGE_AXIS)
        );
        JPanel reportsPanel = new JPanel();
        reportsPanel.setLayout(
                new BoxLayout(reportsPanel, BoxLayout.PAGE_AXIS)
        );
        JTabbedPane tp = new JTabbedPane();
        tp.add("Wysyłka wojska", wysylkaWOjskaPanel);
        tp.add("Opcje", opcjePanel);
        tp.add("Klikanie", klikaniePanel);
        tp.add("Raporty", reportsPanel);
        frame.getContentPane().add(tp);

        oknoKlikania = new OknoKlikania();

        budujOknoWysylkiWojsk(wysylkaWOjskaPanel, config.unitInfo);
        budujOknoRaportow(reportsPanel, config);
        budujOknoOpcji(opcjePanel, config);
        oknoKlikania.budujOknoKlikania(klikaniePanel);

        frame.setVisible(true);
    }

    private static void budujOknoOpcji(Container contentPane, Config config) {
        JLabel informLabel = new JLabel();
        informLabel.setText("Siła wojska do farmienia słabych przeciwników");
        contentPane.add(informLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextField t1Text = new JTextField(30);
        t1Text.setMaximumSize(t1Text.getPreferredSize());
        contentPane.add(t1Text);
        t1Text.setText(config.rajdowanieSila.rajdowanieSilaSlabychWiosek.toString());

        JLabel informLabel2 = new JLabel();
        informLabel2.setText("Siła wojska do farmienia silnych przeciwników");
        contentPane.add(informLabel2);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextField t2Text = new JTextField(30);
        t2Text.setMaximumSize(t2Text.getPreferredSize());
        contentPane.add(t2Text);
        t2Text.setText(config.rajdowanieSila.rajdowanieSilaSilnychWiosek.toString());

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

    }

    private static void budujOknoRaportow(Container contentPane, Config config) throws IOException {
        RaportyOknoComponent raportyOknoComponent = new RaportyOknoComponent(contentPane, config);
    }

    private static void budujOknoWysylkiWojsk(Container contentPane, UnitInfo unitInfo) throws IOException {
        JButton button = new JButton();
        button.setText("Generuj linki");
        contentPane.add(button);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton button2 = new JButton();
        button2.setText("Generuj linki w paczce");
        contentPane.add(button2);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        WyborWojskaComponent wyborWojskaComponent = new WyborWojskaComponent(contentPane, unitInfo);

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea resultText = new JTextArea(20, 20);
        JScrollPane scrollPane = new JScrollPane(resultText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPane.add(scrollPane);
        button.addActionListener(e -> {
            try {
                Calcuations calcuations = new Calcuations(config, wyborWojskaComponent.getActualTroops());
                List<String> result = calcuations.calculate(wyborWojskaComponent.getTownName());
                resultText.setText(StringUtils.join(result));
                for (String x : result) {
                    Desktop.getDesktop().browse(new URL(x).toURI());
                }
                oknoKlikania.t1Text.setText(String.valueOf((result.size())));
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }

        });

        button2.addActionListener(e -> {
            try {
                String[] allTownNames = getAllTownNames();
                for (int i = 0; i < allTownNames.length; i++) {
                    Troops troops = loadTroopsFromTownFile(allTownNames[i]);
                    Calcuations calcuations = new Calcuations(config, troops);
                    List<String> result = calcuations.calculate(wyborWojskaComponent.getTownName());
                    oknoKlikania.t1Text.setText(String.valueOf((result.size())));
                    oknoKlikania.clickOnMousePosition(1950, 650 + i * 34);
                    for (String x : result) {
                        Desktop.getDesktop().browse(new URL(x).toURI());
                    }
                    oknoKlikania.delay(Math.min(result.size() * 80 + 1000, 10000));
                    oknoKlikania.ctrl2Click();
                    oknoKlikania.clickSendArmy();
                }
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

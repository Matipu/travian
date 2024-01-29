package org.example;

import org.example.gui.WyborWojskaComponent;

import java.awt.geom.Point2D;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public static String getActualDate() {
        return formatDateTime(LocalDateTime.now());
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return DATE_FORMAT.format(dateTime);
    }

    public static LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, DATE_FORMAT);
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static String loadStringFromFile(String fileName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
        String result = fileReader.lines().collect(Collectors.joining());
        fileReader.close();
        return result;
    }


    public static int divideRoundUp(Integer x, Integer y) {
        return new BigDecimal(x).divide(new BigDecimal(y), RoundingMode.UP).intValue();
    }

    public static double calculateDistance(String position1, String position2) {
        position1 = position1.substring(1, position1.length() - 1);
        position2 = position2.substring(1, position2.length() - 1);
        int position1X = Integer.parseInt(position1.split("\\|")[0]);
        int position1Y = Integer.parseInt(position1.split("\\|")[1]);

        int position2X = Integer.parseInt(position2.split("\\|")[0]);
        int position2Y = Integer.parseInt(position2.split("\\|")[1]);

        return Point2D.distance(position1X, position1Y, position2X, position2Y);
    }

    public static void updateTownFile(WyborWojskaComponent wyborWojskaComponent) {
        try {
            Troops actualTroops = wyborWojskaComponent.getActualTroops();
            if(actualTroops == null) {
                return;
            }
            String townName = wyborWojskaComponent.getTownName();
            BufferedReader fileReader = new BufferedReader(new FileReader("in/towns/" + townName));
            List<String> fileLines = fileReader.lines().toList();
            String coordinate = fileLines.get(0);
            fileReader.close();

            File lastSendTroopsFile = new File("in/towns/" + townName);
            FileWriter townWriter = new FileWriter(lastSendTroopsFile);
            townWriter.write(coordinate + "\n");
            townWriter.write(actualTroops.toSaveToFileFormat());
            townWriter.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Troops loadTroopsFromTownFile(String townName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("in/towns/" + townName));
        List<String> fileLines = fileReader.lines().toList();
        String armyInfo = fileLines.get(1);
        fileReader.close();
        Troops troops = new Troops();
        troops.fromSaveToFileFormat(armyInfo);
        return troops;
    }

    public static String loadCoordinateFromTownFile(String townName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("in/towns/" + townName));
        return fileReader.lines().toList().get(0);
    }
}

package org.example;

import org.example.Config;
import org.example.HistoricalSendTroops;
import org.example.Utils;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import static org.example.Utils.parseDate;

public class SendingHistory {

    public Integer treshold;

    ArrayList<HistoricalSendTroops> historicalSendTroops = new ArrayList<>();

    public SendingHistory(Integer treshold) {
        this.treshold = treshold;
    }

    public void loadHistoricalSendTroops(File lastSendTroopsFile) throws IOException {

        BufferedReader fileReader = new BufferedReader(new FileReader(lastSendTroopsFile));

        for (String line : fileReader.lines().toList()) {
            String[] splittedLine = line.split(",");

            if (splittedLine.length != 2) {
                continue;
            }
            String farm = splittedLine[0];
            LocalDateTime farmTargetTime = parseDate(splittedLine[1]);
            if (Duration.between(farmTargetTime, LocalDateTime.now()).toMinutes() < treshold) {
                historicalSendTroops.add(new HistoricalSendTroops(farm, farmTargetTime));
            }
        }

        fileReader.close();
    }

    public void saveLastSendTroops() throws IOException {
        File lastSendTroopsFile = new File("lastSendTroops.txt");
        FileWriter lastSendTroopsFileWriter = new FileWriter(lastSendTroopsFile);

        for (HistoricalSendTroops troop : historicalSendTroops) {
            lastSendTroopsFileWriter.write(troop.villageId + "," + Utils.formatDateTime(troop.targetTime) + "\n");
        }
        lastSendTroopsFileWriter.close();
    }

    public void add(HistoricalSendTroops troop) {
        historicalSendTroops.add(troop);
    }

    public boolean isSimilarTargetTime(Map.Entry<String, VillageStatistic> user, LocalDateTime targetTime) {
        return historicalSendTroops.stream().filter(x -> x.villageId.equals(user.getKey())).anyMatch(x -> Math.abs(Duration.between(x.targetTime, targetTime).toMinutes()) < treshold);
    }
}

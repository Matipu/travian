package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.example.Utils.*;

public class Calcuations {
    Troops troopsAmount;
    Config config;
    SendingHistory sendingHistory;

    Calcuations(Config config, Troops troopsAmount) throws IOException {
        this.config = config;
        this.troopsAmount = troopsAmount;
        this.sendingHistory = new SendingHistory(config.treshold);

        File lastSendTroopsFile = new File("lastSendTroops.txt");
        sendingHistory.loadHistoricalSendTroops(lastSendTroopsFile);
        copyFileUsingStream(lastSendTroopsFile, new File("old/old " + getActualDate()));
    }

    public Troops getUnitsWithStrength(Integer strength, Integer tier) {
        UnitInfo unitInfo = config.unitInfo;
        if (tier == 1 && troopsAmount.T1 * unitInfo.t1Strength >= strength) {
            return new Troops(divideRoundUp(strength, unitInfo.t1Strength), 0, 0, 0, 0, 0);
        }
        if (tier == 2 && troopsAmount.T2 * unitInfo.t2Strength >= strength) {
            return new Troops(0, divideRoundUp(strength, unitInfo.t2Strength), 0, 0, 0, 0);
        }
        if (tier == 3 && troopsAmount.T3 * unitInfo.t3Strength >= strength) {
            return new Troops(0, 0, divideRoundUp(strength, unitInfo.t3Strength), 0, 0, 0);
        }
        if (tier == 4 && troopsAmount.T4 * unitInfo.t4Strength >= strength) {
            return new Troops(0, 0, 0, divideRoundUp(strength, unitInfo.t4Strength), 0, 0);
        }
        if (tier == 5 && troopsAmount.T5 * unitInfo.t5Strength >= strength) {
            return new Troops(0, 0, 0, 0, divideRoundUp(strength, unitInfo.t5Strength), 0);
        }
        if (tier == 6 && troopsAmount.T6 * unitInfo.t6Strength >= strength) {
            return new Troops(0, 0, 0, 0, 0, divideRoundUp(strength, unitInfo.t6Strength));
        }

        return null;
    }

    public boolean haveAverageResources(Map.Entry<String, VillageStatistic> user, Integer resources) {
        return !user.getValue().resourcesFound.isEmpty() && user.getValue().resourcesFound.stream().filter(x -> x >= resources).count() >= user.getValue().resourcesFound.size() / 2;
    }

    public LocalDateTime getTargetTime(Troops troopsToSent, double distance) {
        Integer unitSpeed = 9000;
        if (troopsToSent.T1 > 0) {
            unitSpeed = config.unitInfo.t1Speed;
        }
        if (troopsToSent.T2 > 0) {
            unitSpeed = Math.min(unitSpeed, config.unitInfo.t2Speed);
        }
        if (troopsToSent.T3 > 0) {
            unitSpeed = Math.min(unitSpeed, config.unitInfo.t3Speed);
        }
        if (troopsToSent.T4 > 0) {
            unitSpeed = Math.min(unitSpeed, config.unitInfo.t4Speed);
        }
        if (troopsToSent.T5 > 0) {
            unitSpeed = Math.min(unitSpeed, config.unitInfo.t5Speed);
        }
        if (troopsToSent.T6 > 0) {
            unitSpeed = Math.min(unitSpeed, config.unitInfo.t6Speed);
        }

        double hoursToTarget = distance / unitSpeed;
        long secondsToTarget = (long) (hoursToTarget * 3600.0);
        return LocalDateTime.now().plusSeconds(secondsToTarget);
    }

    private Integer calculateStrength(Map.Entry<String, VillageStatistic> user) {
        Integer strenght = config.rajdowanieSila.rajdowanieSilaSlabychWiosek;
        if (haveAverageResources(user, 220)) {
            strenght = config.rajdowanieSila.rajdowanieSilaNajsilniejszychWiosek;
        } else if (config.strongerFarms.contains(user.getKey()) || haveAverageResources(user, 160)) {
            strenght = config.rajdowanieSila.rajdowanieSilaSilnychWiosek;
        }

        if (user.getKey().equals("135754")) { //todo
            strenght = 440;
        }
        return strenght;
    }

    String calculateTroops(Map.Entry<String, VillageStatistic> user) {
        Integer strength = calculateStrength(user);
        for (int i = 1; i <= 6; i++) {
            Troops troopsToSent = getUnitsWithStrength(strength, i);
            if (troopsToSent != null) {
                LocalDateTime targetTime = getTargetTime(troopsToSent, user.getValue().distance);
                if (!sendingHistory.isSimilarTargetTime(user, targetTime)) {
                    troopsAmount.subAllTroops(troopsToSent);
                    sendingHistory.add(new HistoricalSendTroops(user.getKey(), targetTime));
                    return troopsToSent.toUrl();
                }
            }
        }

        return "";
    }

    public static String[] getAllTownNames() {
        File folder = new File("in/towns");
        File[] listOfFiles = Objects.requireNonNull(folder.listFiles());
        ArrayList<String> towns = new ArrayList<>();

        for (File listOfFile : listOfFiles) {
            towns.add(listOfFile.getName());
        }

        String[] arr = new String[towns.size()];
        return towns.toArray(arr);
    }

    public List<String> calculate(String townName) throws IOException {
        VillagesStatistics villagesStatistics = new VillagesStatistics();

        villagesStatistics.setDistanceToFarms(townName, config.ignoreFarms);
        villagesStatistics.loadReports(config.unitInfo);

        List<String> resultList = new ArrayList<>();
//
//        for (Map.Entry<String, VillageStatistic> village : villagesStatistics.sortedStatistics) {
//            if (village.getValue().resourcesFound.isEmpty()) {
//                if (new Random().nextDouble() < 0.05) {
//                    sentTroopsToVillage(village, resultList);
//                }
//            }
//        }

        for (Map.Entry<String, VillageStatistic> village : villagesStatistics.sortedStatistics) {
            sentTroopsToVillage(village, resultList);
        }

        sendingHistory.saveLastSendTroops();
        return resultList;
    }

    private void sentTroopsToVillage(Map.Entry<String, VillageStatistic> village, List<String> resultList) {
        for (int i = 0; i < 6; i++) {
            String calculateTroops = calculateTroops(village);
            if (calculateTroops.length() > 1) {
                resultList.add(config.server + "build.php?id=39&amp;tt=2&amp;c=4&amp;z=" + village.getKey() + "&eventType=4" + calculateTroops + "\n");
            }
        }
    }

}

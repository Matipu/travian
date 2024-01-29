package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.raport.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Map.Entry.*;
import static org.example.Utils.*;

public class VillagesStatistics {
    public Map<String, VillageStatistic> statistics = new HashMap<>();
    public List<Entry<String, VillageStatistic>> sortedStatistics;

    static class EffectivenessComparator implements Comparator<VillageStatistic>
    {
        public int compare(VillageStatistic v1, VillageStatistic v2)
        {
            return v2.averageProfitPerDistance.compareTo(v1.averageProfitPerDistance);
        }
    }

    static class DistanceComparator implements Comparator<VillageStatistic>
    {
        public int compare(VillageStatistic v1, VillageStatistic v2)
        {
            return v1.distance.compareTo(v2.distance);
        }
    }

    public VillagesStatistics() {

    }

    public void loadReports(UnitInfo unitInfo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File folder = new File("in/reports");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            String reportsJson = Utils.loadStringFromFile("in/reports/" + listOfFile.getName());
            Report[] reports = objectMapper.readValue(reportsJson, Report[].class);
            for (Report report : reports) {
                String villageId = report.defenderInfo.village_url.split("=")[1];
                if (statistics.containsKey(villageId)) {
                    updateStatistic(report, villageId, unitInfo);
                }
            }
        }
        sortSortedStatistics(new EffectivenessComparator());
    }

    public void sortSortedStatistics(Comparator<VillageStatistic> comparator) {
        List<Entry<String, VillageStatistic>> list = new ArrayList<>(statistics.entrySet());
        list.sort(comparingByValue(comparator));
        sortedStatistics = list;
    }

    private void updateStatistic(Report report, String villageId, UnitInfo unitInfo) {
        VillageStatistic villageStatistic = statistics.get(villageId);
        Integer resources = parseInt(report.resources.wood) + parseInt(report.resources.clay) + parseInt(report.resources.iron) + parseInt(report.resources.wheat);
        villageStatistic.resourcesFound.add(resources);
        villageStatistic.averageResources = villageStatistic.resourcesFound.stream().mapToInt(Integer::intValue).sum()/villageStatistic.resourcesFound.size();

        Integer loses = parseInt(report.troops.atk_loss.get(0))*unitInfo.t1Cost + parseInt(report.troops.atk_loss.get(4))*unitInfo.t5Cost;
        villageStatistic.loses.add(loses);
        villageStatistic.averageLoses = villageStatistic.loses.stream().mapToInt(Integer::intValue).sum()/villageStatistic.loses.size();
        villageStatistic.averageProfit = villageStatistic.averageResources - villageStatistic.averageLoses;
        villageStatistic.averageProfitPerDistance = villageStatistic.averageProfit / villageStatistic.distance;
    }

    public void setDistanceToFarms(String townName, ArrayList<String> ignoreFarms) throws IOException {
        String townCoordinate = loadCoordinateFromTownFile(townName);
        String[] farms = loadStringFromFile("in/farms/farms").replaceAll("\n", "").split(",");

//sprawdzenie czy są jakieś farmy do dodania
        String[] farmsToAdd = loadStringFromFile("in/farms/farmsToAdd").replaceAll("\n", "").split(",");
        Arrays.stream(farmsToAdd)
                .filter(x-> !(Arrays.asList(farms)).contains(x)).collect(Collectors.joining(","));

        for (int i = 0; i < farms.length; i += 2) {
            if (ignoreFarms.contains(farms[i])) {
                continue;
            }
            double distance = calculateDistance(townCoordinate, farms[i + 1]);
            if (!statistics.containsKey(farms[i])) {
                statistics.put(farms[i], new VillageStatistic(distance));
            }
        }
    }
}

package org.example;

import java.util.ArrayList;

public class VillageStatistic {
    public ArrayList<Integer> resourcesFound = new ArrayList<>();
    public ArrayList<Integer> loses = new ArrayList<>();
    public Integer averageResources = 0;
    public Integer averageLoses = 0;
    public Integer averageProfit = 0;
    public Double distance;
    public Double averageProfitPerDistance = 0.0;

    public VillageStatistic(double distance) {
        this.distance = distance;
    }
}

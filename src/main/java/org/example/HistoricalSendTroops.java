package org.example;

import java.time.LocalDateTime;

public class HistoricalSendTroops {
    String villageId;
    LocalDateTime targetTime;

    public HistoricalSendTroops(String villageId, LocalDateTime targetTime) {
        this.villageId = villageId;
        this.targetTime = targetTime;
    }
}

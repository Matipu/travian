package org.example.gui.listner;

import org.example.VillageStatistic;
import org.example.VillagesStatistics;
import org.example.gui.RaportyOknoComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

public class TownChangeReportsListener implements ActionListener {

    public RaportyOknoComponent raportyOknoComponent;

    public TownChangeReportsListener(RaportyOknoComponent raportyOknoComponent) {
        this.raportyOknoComponent = raportyOknoComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            raportyOknoComponent.removeAllLabels();

            VillagesStatistics villagesStatistics = new VillagesStatistics();
            villagesStatistics.setDistanceToFarms(raportyOknoComponent.getTownName(), raportyOknoComponent.config.ignoreFarms);
            villagesStatistics.loadReports(raportyOknoComponent.config.unitInfo);

            for (Map.Entry<String, VillageStatistic> villagesStatistic: villagesStatistics.sortedStatistics) {
                DecimalFormat twoDecimals = new DecimalFormat("#.##");
                raportyOknoComponent.addLabel("distance: " + twoDecimals.format(villagesStatistic.getValue().distance)  +
                        " | average profit: " + twoDecimals.format(villagesStatistic.getValue().averageProfit));
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.Utils.loadStringFromFile;

public class Config {

    public Nacja nacja;
    public UnitInfo unitInfo;
    public RajdowanieSila rajdowanieSila;
    public String server;
    public Integer treshold;
    public List<String> strongerFarms;
    public ArrayList<String> ignoreFarms;

    ObjectMapper objectMapper = new ObjectMapper();

    public Config() throws IOException {
        loadConfig();
    }

    public void loadConfig() throws IOException {
        nacja = Nacja.getNacja(Utils.loadStringFromFile("config/NacjaConfig"));

        String unitsConfigFileName;
        if (nacja == Nacja.GERMANIE) {
            unitsConfigFileName = "config/GermanieInfo";
        } else if (nacja == Nacja.GALOWIE) {
            unitsConfigFileName = "config/GalowieInfo";
        } else if (nacja == Nacja.RZYMIANIE) {
            unitsConfigFileName = "config/RzymianieInfo";
        } else {
            unitsConfigFileName = "config/GermanieInfo";
        }

        String unitInfoJson = Utils.loadStringFromFile(unitsConfigFileName);
        unitInfo = objectMapper.readValue(unitInfoJson, UnitInfo.class);

        String rajdowanieSilaJson = Utils.loadStringFromFile("config/RajdowanieSilaConfig");
        rajdowanieSila = objectMapper.readValue(rajdowanieSilaJson, RajdowanieSila.class);

        server = Utils.loadStringFromFile("config/ServerConfig");
        treshold = Integer.valueOf(Utils.loadStringFromFile("config/TresholdConfig"));

        strongerFarms = List.of(loadStringFromFile("in/StrongerFarms").split(","));
        ignoreFarms = new ArrayList<>(List.of(loadStringFromFile("in/IgnoreFarms").split(",")));
    }
}

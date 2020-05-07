package com.censusanalyser;

import com.censusanalyser.model.USCensusCSV;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {
    private Map<String, CensusDAO> censusMap;

    @Override
    public Map<String, CensusDAO> loadCensusData(String... csvFilePath) {
        censusMap = super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
        return censusMap;
    }
}

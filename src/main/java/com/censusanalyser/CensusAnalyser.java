package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    List<CensusDAO> censusList = null;
    Map<String, CensusDAO> censusMap = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<CensusDAO>();
        this.censusMap = new HashMap<String, CensusDAO>();
    }

    CensusLoader censusLoader = new CensusLoader();

    public enum Country {
        INDIA, US, FRANCE;
    }

    public int loadCensusData(Country country,String... csvFilePath) {
        censusMap = censusLoader.loadCensusData(country, csvFilePath);
        return censusMap.size();
    }

    public String getSortedCensusData(String sortOn, String... sortBy) {
        censusList = censusMap.values().stream().collect(Collectors.toList());
        Comparator<CensusDAO> censusComparator = null;
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        if (sortOn == "state") {
            censusComparator = Comparator.comparing(census -> census.state);
        } else if (sortOn == "stateCode") {
            censusComparator = Comparator.comparing(census -> census.stateCode);
        } else if (sortOn == "population") {
            censusComparator = Comparator.comparing(census -> census.population);
        } else if (sortOn == "populationDensity") {
            censusComparator = Comparator.comparing(census -> census.populationDensity);
        } else if (sortOn == "area") {
            censusComparator = Comparator.comparing(census -> census.totalArea);
        }
        this.sort(censusComparator);
        if (sortBy.length == 1) {
            Collections.reverse(censusList);
        }
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    private void sort(Comparator<CensusDAO> censusComparator) {
        for (int i = 0; i < censusList.size() - 1; i++) {
            for (int j = 0; j < censusList.size() - i - 1; j++) {
                CensusDAO census1 = censusList.get(j);
                CensusDAO census2 = censusList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j + 1, census1);
                }
            }
        }
    }
}

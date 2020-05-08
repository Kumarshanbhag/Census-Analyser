package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    private final Country country;
    Map<String, CensusDAO> censusMap = null;

    public CensusAnalyser(Country country) {
        this.country = country;
    }

    public enum Country {
        INDIA, US, FRANCE;
    }

    public int loadCensusData(Country country,String... csvFilePath) {
        censusMap = CensusAdapterFactory.loadCensusData(country, csvFilePath);
        return censusMap.size();
    }

    public String getSortedCensusData(String sortOn, String... sortBy) {
        Comparator<CensusDAO> censusComparator = null;
        if (censusMap == null || censusMap.size() == 0) {
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
        ArrayList censusDTO = censusMap.values().stream()
                .sorted(censusComparator)
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(Collectors.toCollection(ArrayList::new));
        if (sortBy.length == 1) {
            Collections.reverse(censusDTO);
        }
        String sortedStateCensus = new Gson().toJson(censusDTO);
        return sortedStateCensus;
    }
}

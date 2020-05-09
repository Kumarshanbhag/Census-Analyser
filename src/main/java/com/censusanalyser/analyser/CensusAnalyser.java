package com.censusanalyser.analyser;

import com.censusanalyser.enums.SortByField;
import com.censusanalyser.adapter.CensusAdapterFactory;
import com.censusanalyser.dao.CensusDAO;
import com.censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    private final Country country;
    Map<String, CensusDAO> censusMap = null;
    Map<SortByField, Comparator<CensusDAO>> sortMap = null;

    public CensusAnalyser(Country country) {
        this.censusMap = new HashMap<>();
        this.country = country;
        this.sortMap = new HashMap<>();
        this.sortMap.put(SortByField.STATE, Comparator.comparing(censusData -> censusData.state));
        this.sortMap.put(SortByField.STATECODE, Comparator.comparing(censusData -> censusData.stateCode));
        this.sortMap.put(SortByField.POPULATION, Comparator.comparing(censusData -> censusData.population));
        this.sortMap.put(SortByField.POPULATIONDENSITY, Comparator.comparing(censusData -> censusData.populationDensity));
        this.sortMap.put(SortByField.AREA, Comparator.comparing(censusData -> censusData.totalArea));    }

    public enum Country {
        INDIA, US, FRANCE;
    }

    public int loadCensusData(Country country,String... csvFilePath) {
        censusMap = CensusAdapterFactory.loadCensusData(country, csvFilePath);
        return censusMap.size();
    }

    public String getSortedCensusData(SortByField sortByField, String... sortBy) {
        Comparator<CensusDAO> censusComparator = null;
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTO = censusMap.values().stream()
                .sorted(this.sortMap.get(sortByField))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(Collectors.toCollection(ArrayList::new));
        if (sortBy.length == 1) {
            Collections.reverse(censusDTO);
        }
        String sortedStateCensus = new Gson().toJson(censusDTO);
        return sortedStateCensus;
    }
}

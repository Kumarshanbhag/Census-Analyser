package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.IndiaStateCodeCSV;
import com.censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<CensusDAO> censusList = null;
    Map<String, CensusDAO> censusMap = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<CensusDAO>();
        this.censusMap = new HashMap<String, CensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVFileIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .forEach(csvCensus -> censusMap.put(csvCensus.state, new CensusDAO(csvCensus)));
            censusList = censusMap.values().stream().collect(Collectors.toList());
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadUSCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusCSV> censusCSVFileIterator = csvBuilder.getCSVFileIterator(reader, USCensusCSV.class);
            Iterable<USCensusCSV> csvIterable = () -> censusCSVFileIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .forEach(csvCensus -> censusMap.put(csvCensus.state, new CensusDAO(csvCensus)));
            censusList = censusMap.values().stream().collect(Collectors.toList());
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndiaStateCode(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCSVFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCSVFileIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> censusMap.get(csvState.stateCode) != null)
                    .forEach(csvState -> censusMap.get(csvState.stateCode).stateCode = csvState.stateCode);
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getSortedCensusData(String sortOn, String... sortBy) {
        Comparator<CensusDAO> censusComparator = null;
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        if (sortOn == "state") {
            censusComparator = Comparator.comparing(census -> census.state);
        } else if (sortOn == "population") {
            censusComparator = Comparator.comparing(census -> census.population);
        } else if (sortOn == "populationDensity") {
            censusComparator = Comparator.comparing(census -> census.populationDensity);
        } else if (sortOn == "area") {
            censusComparator = Comparator.comparing(census -> census.totalArea);
        }
        this.sort(censusComparator);
        if(sortBy.length == 1) {
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

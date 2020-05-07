package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.IndiaStateCodeCSV;
import com.censusanalyser.model.USCensusCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {
    Map<String, CensusDAO> censusMap = new HashMap<>();

    public Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return this.loadCensusData(IndiaCensusCSV.class, csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return this.loadCensusData(USCensusCSV.class, csvFilePath);
        else
            throw new CensusAnalyserException("Incorrect Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }

    private <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusCSVFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvIterable = () -> censusCSVFileIterator;
            if (censusCSVClass.getName().equals("com.censusanalyser.model.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(csvState -> censusMap.put(csvState.state, new CensusDAO(csvState)));
            } else if (censusCSVClass.getName().equals("com.censusanalyser.model.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(csvState -> censusMap.put(csvState.state, new CensusDAO(csvState)));
            }
            if (csvFilePath.length == 1)
                return censusMap;
            this.loadIndianStateCode(censusMap, csvFilePath[1]);
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private int loadIndianStateCode(Map<String, CensusDAO> censusMap, String csvFilePath) {
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
}

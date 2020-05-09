package com.censusanalyser.adapter;

import com.censusanalyser.builder.CSVBuilderFactory;
import com.censusanalyser.dao.CensusDAO;
import com.censusanalyser.builder.ICSVBuilder;
import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.USCensusCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter extends RuntimeException {
    public abstract Map<String, CensusDAO> loadCensusData(String... csvFilePath);

    public <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCsvClass, String csvFilePath) {
        Map<String, CensusDAO> censusMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder openCSVBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusCSVIterator = openCSVBuilder.getCSVFileIterator(reader, censusCsvClass);
            Iterable<E> censusCSVIterable = () -> censusCSVIterator;
            if (censusCsvClass.getName().equals("com.censusanalyser.model.IndiaCensusCSV")) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            } else if (censusCsvClass.getName().equals("com.censusanalyser.model.USCensusCSV")) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}


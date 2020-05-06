package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.IndiaStateCodeCSV;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndiaCensusDAO> censusList = null ;
    Map<String, IndiaCensusDAO> censusMap = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusDAO>();
        this.censusMap = new HashMap<String, IndiaCensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVFileIterator = csvBuilder.getCSVFileIterator(reader,IndiaCensusCSV.class);
            while(censusCSVFileIterator.hasNext()){
                IndiaCensusCSV ind = censusCSVFileIterator.next();
                this.censusMap.put(ind.state, new IndiaCensusDAO(ind));
            }
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
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> censusMap.get(csvState.stateCode) != null)
                    .forEach(csvState -> censusMap.get(csvState.stateCode).stateCode = csvState.stateCode);
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getSortedCensusData(String sortOn) {
        Comparator<IndiaCensusDAO> censusComparator = null;
        if(censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        if(sortOn == "state") {
            censusComparator = Comparator.comparing(census -> census.state);
        }
        if(sortOn == "population") {
            censusComparator = Comparator.comparing(census -> census.population);
        }
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    private void sort(Comparator<IndiaCensusDAO> censusComparator){
        for(int i=0; i<censusList.size() - 1; i++) {
            for (int j=0; j<censusList.size() - i -1; j++ ) {
                IndiaCensusDAO census1 = censusList.get(j);
                IndiaCensusDAO census2 = censusList.get(j+1);
                if(censusComparator.compare(census1, census2)  > 0) {
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
    }
}

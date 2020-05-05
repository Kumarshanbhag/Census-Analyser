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

public class CensusAnalyser {
    List<IndiaCensusDTO> censusList = null ;
    Map<String, IndiaCensusDTO> censusMap = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusDTO>();
        this.censusMap = new HashMap<String, IndiaCensusDTO>();
    }

    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVFileIterator = csvBuilder.getCSVFileIterator(reader,IndiaCensusCSV.class);
            while(censusCSVFileIterator.hasNext()){
                IndiaCensusCSV ind = censusCSVFileIterator.next();
                this.censusMap.put(ind.state, new IndiaCensusDTO(ind));
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
            while(stateCSVFileIterator.hasNext()) {
                IndiaStateCodeCSV indiaStateCode = stateCSVFileIterator.next();
                IndiaCensusDTO indiaCensusDTO = censusMap.get(indiaStateCode.stateCode);
                if(indiaCensusDTO == null)
                    continue;
            }
            return censusMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData() {
        Comparator<IndiaCensusDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    private void sort(Comparator<IndiaCensusDTO> censusComparator) {
        for(int i=0; i<censusList.size() - 1; i++) {
            for (int j=0; j<censusList.size() - i -1; j++ ) {
                IndiaCensusDTO census1 = censusList.get(j);
                IndiaCensusDTO census2 = censusList.get(j+1);
                if(censusComparator.compare(census1, census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
    }
}

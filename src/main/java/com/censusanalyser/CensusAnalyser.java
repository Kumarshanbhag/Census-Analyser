package com.censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class CensusAnalyser {
    List<IndiaCensusCSV> censusCSVList;
    
    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            censusCSVList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndiaStateCode(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<IndiaStateCodeCSV> censusCSVList = csvBuilder.getCSVFileList(reader, IndiaStateCodeCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData() {
        Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(censusCSVList);
        return sortedStateCensus;
    }

    private void sort(Comparator<IndiaCensusCSV> censusComparator) {
        for(int i=0; i<censusCSVList.size() - 1; i++) {
            for (int j=0; j<censusCSVList.size() - i -1; j++ ) {
                IndiaCensusCSV census1 = censusCSVList.get(j);
                IndiaCensusCSV census2 = censusCSVList.get(j+1);
                if(censusComparator.compare(census1, census2) > 0) {
                    censusCSVList.set(j, census2);
                    censusCSVList.set(j+1, census1);
                }
            }
        }
    }
}

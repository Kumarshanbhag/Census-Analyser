package com.censusanalyser;

import org.junit.Assert;
import org.junit.Test;

public class CensusAnalyserTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_TYPE = "./src/test/resources/IndiaStateCensusData.json";
    private static final String WRONG_HEADER_FILE = "./src/test/resources/IndiaStateCode.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        Assert.assertEquals(29, numOfRecords);
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenStateCensusCsvFile_WhenTypeIncorrect_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenStateCensusCsvFile_WhenHeaderIncorrect_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(WRONG_HEADER_FILE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
}


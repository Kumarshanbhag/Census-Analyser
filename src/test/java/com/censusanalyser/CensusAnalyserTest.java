package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CensusAnalyserTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_INDIA_CENSUS_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_INDIA_CENSUS_CSV_FILE_TYPE = "./src/test/resources/IndiaStateCensusData.json";
    private static final String INDIA_STATE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_INDIA_STATE_CSV_FILE_PATH = "./src/main/resources/IndiaStateCode.csv";
    private static final String WRONG_INDIA_STATE_CSV_FILE_TYPE = "./src/main/resources/IndiaStateCode.json";

    CensusAnalyser censusAnalyser;

    @Before
    public void setUp() throws Exception {
        censusAnalyser = new CensusAnalyser();
    }

    //UC1
    @Test
    public void givenIndianCensusCSVFile_ShouldReturnsCorrectRecords() {
        int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        Assert.assertEquals(29, numOfRecords);
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            censusAnalyser.loadIndiaCensusData(WRONG_INDIA_CENSUS_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenStateCensusCsvFile_WhenTypeIncorrect_shouldReturnException() {
        try {
            censusAnalyser.loadIndiaCensusData(WRONG_INDIA_CENSUS_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //UC2
    @Test
    public void givenIndianStateCSV_ShouldReturnExactCount() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        int numOfStateCode = censusAnalyser.loadIndiaStateCode(INDIA_STATE_CSV_FILE_PATH);
        Assert.assertEquals(29, numOfStateCode);
    }

    @Test
    public void givenIndianStateCSV_WithWrongFile_ShouldThrowException() {
        try {
            censusAnalyser.loadIndiaStateCode(WRONG_INDIA_STATE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCSV_WhenTypeIncorrect_shouldReturnException() {
        try {
            censusAnalyser.loadIndiaStateCode(WRONG_INDIA_STATE_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //UC3
    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnStateAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getStateWiseSortedCensusData();
        } catch(CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }
}


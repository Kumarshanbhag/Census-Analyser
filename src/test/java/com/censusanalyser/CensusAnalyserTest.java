package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;
import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.USCensusCSV;
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
    private static final String US_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";
    private static final String WRONG_US_CENSUS_CSV_FILE_PATH = "./src/main/resources/USCensusData.csv";
    private static final String WRONG_US_CENSUS_CSV_FILE_TYPE = "./src/test/resources/USCensusData.json";

    CensusAnalyser censusAnalyser;

    @Before
    public void setUp() throws Exception {
        censusAnalyser = new CensusAnalyser();
    }

    @Test
    public void givenIndianCensusCSVFile_ShouldReturnCorrectRecords() {
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

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("state");
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnStateAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("state", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("population", "descending");
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Uttar Pradesh", censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulationAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("population", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulationDensity_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("populationDensity", "descending");
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Bihar", censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulationDensityAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("populationDensity", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnArea_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("area", "descending");
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Rajasthan", censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnAreaAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("area", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //UC8
    @Test
    public void givenUSCensusCSVFile_ShouldReturnCorrectRecords() {
        int numOfRecords = censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        Assert.assertEquals(51, numOfRecords);

    }

    @Test
    public void givenUSCensusData_WithWrongFile_ShouldThrowException() {
        try {
            censusAnalyser.loadUSCensusData(WRONG_US_CENSUS_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSCensusCsvFile_WhenTypeIncorrect_shouldReturnException() {
        try {
            censusAnalyser.loadUSCensusData(WRONG_US_CENSUS_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //UC9
    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("population", "descending");
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("California", censusCSV[0].state);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("population", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //UC10
    @Test
    public void givenUSCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("state");
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("Alabama", censusCSV[0].state);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnStateAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("state", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnStateCode_ShouldReturnSortedResult() {
        censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("stateCode");
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("Alaska", censusCSV[0].state);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnStateCodeAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("stateCode");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationDensity_ShouldReturnSortedResult() {
        censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("populationDensity", "descending");
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("District of Columbia", censusCSV[0].state);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationDensityAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("populationDensity", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenUSCensusData_WhenSortedOnArea_ShouldReturnSortedResult() {
        censusAnalyser.loadUSCensusData(US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData("area", "descending");
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("Alaska", censusCSV[0].state);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnAreaAndNoDataFound_ShouldReturnException() {
        try {
            censusAnalyser.getSortedCensusData("area", "descending");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }
}


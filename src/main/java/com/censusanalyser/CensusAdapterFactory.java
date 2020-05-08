package com.censusanalyser;

import com.censusanalyser.exception.CensusAnalyserException;

import java.util.Map;

public class CensusAdapterFactory {
    public static Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter().loadCensusData(csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        throw new CensusAnalyserException("Incorrect Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}

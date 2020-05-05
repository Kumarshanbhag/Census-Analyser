package com.censusanalyser.exception;

public class CensusAnalyserException extends RuntimeException{
    public final ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public enum ExceptionType {
        CENSUS_FILE_PROBLEM, NO_CENSUS_DATA;
    }
}

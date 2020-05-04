package com.censusanalyser;

public class CensusAnalyserException extends RuntimeException{
    public final ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public enum ExceptionType {
        CSV_WRONG_HEADER, CENSUS_FILE_PROBLEM

    }
}

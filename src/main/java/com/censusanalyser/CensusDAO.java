package com.censusanalyser;

import com.censusanalyser.model.IndiaCensusCSV;
import com.censusanalyser.model.USCensusCSV;

public class CensusDAO {
    public double totalArea;
    public double populationDensity;
    public int population;
    public String state;
    public String stateCode;

    public CensusDAO(IndiaCensusCSV indiaCensus) {
        state = indiaCensus.state;
        populationDensity = indiaCensus.densityPerSqKm;
        totalArea = indiaCensus.areaInSqKm;
        population = indiaCensus.population;
    }

    public CensusDAO(USCensusCSV usCensus) {
        stateCode = usCensus.stateID;
        state = usCensus.state;
        population = usCensus.population;
        populationDensity = usCensus.populationDensity;
        totalArea = usCensus.totalArea;
    }

    public Object getCensusDTO(CensusAnalyser.Country country){
        if(country.equals(CensusAnalyser.Country.US))
            return new USCensusCSV(state, stateCode, population, populationDensity, totalArea);
        return new IndiaCensusCSV(state, population, populationDensity, totalArea);
    }
}

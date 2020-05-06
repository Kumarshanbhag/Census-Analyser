package com.censusanalyser;

import com.censusanalyser.model.IndiaCensusCSV;

public class IndiaCensusDAO {
    public int population;
    public int areaInSqKm;
    public int densityPerSqKm;
    public String state;
    public String stateCode;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensus) {
        state = indiaCensus.state;
        densityPerSqKm = indiaCensus.densityPerSqKm;
        areaInSqKm = indiaCensus.areaInSqKm;
        population = indiaCensus.population;
    }

}

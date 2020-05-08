package com.censusanalyser.model;

import com.opencsv.bean.CsvBindByName;

public class IndiaCensusCSV {
    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "AreaInSqKm", required = true)
    public double areaInSqKm;

    @CsvBindByName(column = "DensityPerSqKm", required = true)
    public double densityPerSqKm;

    public IndiaCensusCSV(String state, int population, double populationDensity, double totalArea) {
        this.state = state;
        this.population =population;
        this.densityPerSqKm = populationDensity;
        this.areaInSqKm = totalArea;
    }

    public IndiaCensusCSV() {
    }
}

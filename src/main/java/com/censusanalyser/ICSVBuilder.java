package com.censusanalyser;

import com.censusanalyser.model.IndiaCensusCSV;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVBuilder<E> {
    public Iterator<E> getCSVFileIterator(Reader reader, Class<IndiaCensusCSV> indiaCensusCSVClass);
}

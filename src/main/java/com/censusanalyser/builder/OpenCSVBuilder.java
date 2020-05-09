
package com.censusanalyser.builder;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class OpenCSVBuilder implements ICSVBuilder {
    @Override
    public Iterator getCSVFileIterator(Reader reader, Class csvClass) {
        return this.getCSVBean(reader, csvClass).iterator();
    }

    private <E> CsvToBean<E> getCSVBean(Reader reader, Class csvClass) {
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        return csvToBeanBuilder.build();
    }
}

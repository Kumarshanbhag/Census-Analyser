package com.censusanalyser;

import java.io.Reader;
import java.util.List;

public interface ICSVBuilder {
    public List getCSVFileList(Reader reader, Class csvClass);
}

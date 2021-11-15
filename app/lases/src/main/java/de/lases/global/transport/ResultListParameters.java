package de.lases.global.transport;

import java.util.List;

/**
 * Encapsulates all values a repository needs to configure the result lists on queries with a list of results. These parameters include the column to sort by and the offset and length of the desired part of the result.
 */
public class ResultListParameters {

    private List<ResultListFilter> filters;

    private int pageNo;

    // private int resultOffset; = pageNo * resultLength

    private int resultLength; // -> wird aus Konfigdatei eingelesen

    private String sortColumn;

    private SortOrder sortOrder;

    private String globalSearchWord;

    public List<ResultListFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ResultListFilter> filters) {
        this.filters = filters;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getResultLength() {
        return resultLength;
    }

    public void setResultLength(int resultLength) {
        this.resultLength = resultLength;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getGlobalSearchWord() {
        return globalSearchWord;
    }

    public void setGlobalSearchWord(String globalSearchWord) {
        this.globalSearchWord = globalSearchWord;
    }
}

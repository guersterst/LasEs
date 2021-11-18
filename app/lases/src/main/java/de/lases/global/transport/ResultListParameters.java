package de.lases.global.transport;

import java.util.List;

/**
 * Encapsulates all values a repository needs to configure the result lists on
 * queries with a list of results. These parameters include the column to sort
 * by and the offset and length of the desired part of the result.
 */
public class ResultListParameters {

    private List<ResultListFilter> filters;

    private int pageNo;

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

    /**
     * The page the pagination is on and that should subsequently be loaded form
     * the datasource.
     *
     * @param pageNo The page numner.
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    /**
     * Set the name of the column the pagination should be sorted after.
     *
     * @param sortColumn Name of the column to sort after.
     */
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * Set the sort order to ascending or descending.
     *
     * @param sortOrder The sort order.
     */
    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getGlobalSearchWord() {
        return globalSearchWord;
    }

    /**
     * Set a global search word that searches all fields of the table.
     *
     * @param globalSearchWord A global search word.
     */
    public void setGlobalSearchWord(String globalSearchWord) {
        this.globalSearchWord = globalSearchWord;
    }
}

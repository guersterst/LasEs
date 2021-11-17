package de.lases.global.transport;

import java.util.List;

/**
 * A filter that can be applied on a column of a table.
 */
public class ResultListFilter {

    private String columnName;

    private List<ResultListFilterOption> filterOptions;

    public String getColumnName() {
        return columnName;
    }

    /**
     * Set the name of the table column this filter should apply to.
     *
     * @param columnName Name of the column for this filter.
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<ResultListFilterOption> getFilterOptions() {
        return filterOptions;
    }

    /**
     * Set the list of filter options that should be applied to the column
     * specified via {@code setColumnName}.
     *
     * @param filterOptions  List of filter options.
     */
    public void setFilterOptions(List<ResultListFilterOption> filterOptions) {
        this.filterOptions = filterOptions;
    }
}

package de.lases.control.internal;

import java.util.List;

import de.lases.global.transport.*;

/**
 * Abstract class which implements a list pagination.
 * Data is loaded in chunks and can be sorted and filtered.
 *
 * @param <T> element type of the list.
 */
public abstract class Pagination<T> {

    /**
     * Paginated items.
     */
    private List<T> entries;

    /**
     * Meta-information about this pagination.
     * Includes information about the filtering and sorting.
     */
    private final ResultListParameters resultListParameters;

    /**
     * Create a pagination instance. loadData() and calculateNumberPages() needs
     * to be overwritten.
     *
     * @param defaultSortedBy default column identifier to be sorted by.
     */
    public Pagination(String defaultSortedBy) {
        resultListParameters = new ResultListParameters();
        resultListParameters.setSortColumn(defaultSortedBy);
        resultListParameters.setPageNo(1);
        resultListParameters.setSortOrder(SortOrder.ASCENDING);
    }

    /**
     * Needs to be overwritten to load the correct data items for the page.
     * This must do the filtering and sorting, too.
     */
    public abstract void loadData();

    /**
     * Needs to be overwritten to calculate the correct number of pages in that pagination.
     */
    protected abstract Integer calculateNumberPages();

    /**
     * Load data on page 1.
     */
    public void firstPage() {
        resultListParameters.setPageNo(1);
        loadData();
    }

    /**
     * Load data on last page.
     */
    public void lastPage() {
        resultListParameters.setPageNo(calculateNumberPages());
        loadData();
    }

    /**
     * Load data of previous page, unless you are already on the first page.
     */
    public void previousPage() {
        if (resultListParameters.getPageNo() > 1) {
            resultListParameters.setPageNo(resultListParameters.getPageNo() - 1);
        }
        loadData();
    }

    /**
     * Load data of the next page, unless you are already on the last page.
     */
    public void nextPage() {
        if (resultListParameters.getPageNo() < calculateNumberPages()) {
            resultListParameters.setPageNo(resultListParameters.getPageNo() + 1);
        }
        loadData();
    }

    /**
     * Sort the data in the list by a certain column.
     * If the same column is selected again, the sort order is reversed.
     *
     * @param column column identifier to sort by.
     */
    public void sortBy(String column) {
        if (column.equals(resultListParameters.getSortColumn())) {
            switch (resultListParameters.getSortOrder()) {
                case ASCENDING -> resultListParameters.setSortOrder(SortOrder.DESCENDING);
                case DESCENDING -> resultListParameters.setSortOrder(SortOrder.ASCENDING);
            }
            resultListParameters.setPageNo(1);
        } else {
            resultListParameters.setSortColumn(column);
            resultListParameters.setSortOrder(SortOrder.ASCENDING);
            resultListParameters.setPageNo(1);
        }
        loadData();
    }

    /**
     * Get the DTO, which holds all the meta-information of this pagination.
     *
     * @return DTO of the pagination meta-information.
     */
    public ResultListParameters getResultListParameters() {
        return resultListParameters;
    }

    /**
     * Get the paginated list items.
     *
     * @return list items of type T.
     */
    public List<T> getEntries() {
        return entries;
    }
}

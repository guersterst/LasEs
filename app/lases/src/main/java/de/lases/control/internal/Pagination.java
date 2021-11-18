package de.lases.control.internal;

import java.util.List;

import de.lases.global.transport.*;

public abstract class Pagination<T> {

    private List<T> entries;

    private ResultListParameters resultListParameters;

    public Pagination(String sortedBy, Integer numberItemsPage, Integer numberColumns) {
        //resultListParameters.setResultLength(numberItemsPage);
        resultListParameters.setSortColumn(sortedBy);
        resultListParameters.setSortOrder(SortOrder.ASCENDING);
        resultListParameters.setPageNo(1);
    }

    /**
     * Needs to be overwritten to load the correct data items for the page.
     */
    public abstract void loadData();

    protected abstract Integer calculateNumberPages();

    public void firstPage() {
        resultListParameters.setPageNo(1);
        loadData();
    }

    public void lastPage() {
        resultListParameters.setPageNo(calculateNumberPages());
        loadData();
    }

    public void previousPage() {
        if (resultListParameters.getPageNo() > 1) {
            resultListParameters.setPageNo(resultListParameters.getPageNo() - 1);
        }
        loadData();
    }

    public void nextPage() {
        if (resultListParameters.getPageNo() < calculateNumberPages()) {
            resultListParameters.setPageNo(resultListParameters.getPageNo() + 1);
        }
        loadData();
    }

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

    public ResultListParameters getResultListParameters() {
        return resultListParameters;
    }

}

package dtos;

import java.util.List;

public abstract class Pagination<T> {
	
    private List<T> entries;
    Integer currentPage;
    private String[] searchWords;
    boolean ascending;
    String sortedBy;
    Integer numberItemsPage;

    public Pagination(String sortedBy, Integer numberItemsPage, Integer numberColumns) {
        this.numberItemsPage = numberItemsPage;
        this.sortedBy = sortedBy;
        ascending = true;
        currentPage = 1;
        searchWords = new String[numberColumns];
    }

    /**
     * Needs to be overwritten to load the correct data items for the page.
     */
    public abstract void loadData();

    protected abstract Integer calculateNumberPages();

    public void firstPage() {
        currentPage = 1;
        loadData();
    }

    public void lastPage() {
        currentPage = calculateNumberPages();
        loadData();
    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage = getCurrentPage() - 1;
        }
        loadData();
    }

    public void nextPage() {
        if (currentPage < calculateNumberPages()) {
            currentPage = getCurrentPage() + 1;
        }
        loadData();
    }

    public void sortBy(String column) {
        if (column.equals(sortedBy)) {
            ascending = !ascending;
            currentPage = 1;
        } else {
            sortedBy = column;
            ascending = true;
            currentPage = 1;
        }
        loadData();
    }

}

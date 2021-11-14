package de.lases.util;

import java.util.List;

public abstract class Pagination<T> {

    Integer currentPage;

    Integer numberItemsPage;
    String sortedBy;
    boolean ascending;

    private List<T> entries;
    private String[] searchWords;

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

    public Integer getNumberItemsPage() {
        return numberItemsPage;
    }

    public void setNumberItemsPage(Integer numberItemsPage) {
        this.numberItemsPage = numberItemsPage;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String[] getSearchWords() {
        return searchWords;
    }

    public void setSearchWords(String[] searchWords) {
        this.searchWords = searchWords;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }
}

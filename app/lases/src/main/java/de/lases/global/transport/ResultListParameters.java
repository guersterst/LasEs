package de.lases.global.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates all values a repository needs to configure the result lists on
 * queries with a list of results. These parameters include the column to sort
 * by and the offset and length of the desired part of the result.
 */
public class ResultListParameters {

    /**
     * Mapping column names to entered filter words.
     */
    private final Map<String, String> filterColumns = new HashMap<>();

    private DateSelect dateSelect;

    private SubmissionState submissionState;

    private Visibility visibleFilter;

    private boolean recommendationFilter;

    private int pageNo;

    private String sortColumn;

    private SortOrder sortOrder;

    private String globalSearchWord;

    public int getPageNo() {
        return pageNo;
    }

    /**
     * Maps column names to a filter string.
     *
     * @return the  map.
     */
    public Map<String, String> getFilterColumns() {
        return filterColumns;
    }

    public DateSelect getDateSelect() {
        return dateSelect;
    }

    /**
     * Set the filtering for dates.
     *
     * @param dateSelect {@link DateSelect}
     */
    public void setDateSelect(DateSelect dateSelect) {
        this.dateSelect = dateSelect;
    }

    public SubmissionState getSubmissionState() {
        return submissionState;
    }

    /**
     * Set the submission state filtering.
     *
     * @param submissionState {@link SubmissionState}
     */
    public void setSubmissionState(SubmissionState submissionState) {
        this.submissionState = submissionState;
    }

    public Visibility getVisibleFilter() {
        return visibleFilter;
    }

    /**
     * Set filtering for a visible entry (only used by papers and reviews).
     *
     * @param visibleFilter true/false.
     */
    public void setVisibleFilter(Visibility visibleFilter) {
        this.visibleFilter = visibleFilter;
    }

    public boolean isRecommendationFilter() {
        return recommendationFilter;
    }

    /**
     * Set filtering for a recommendation (only used by reviews).
     *
     * @param recommendationFilter true/false.
     */
    public void setRecommendationFilter(boolean recommendationFilter) {
        this.recommendationFilter = recommendationFilter;
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

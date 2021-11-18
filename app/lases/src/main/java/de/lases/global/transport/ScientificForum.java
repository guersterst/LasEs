package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a scientific forum.
 */
public class ScientificForum {

    private int id;

    private String description;

    private String reviewManual;

    private String url;

    private LocalDateTime deadline;

    private String name;

    public int getId() {
        return id;
    }

    /**
     * Set the id of the scientific forum.
     *
     * @param id The id of the scientific forum.
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set a short description of the scientific forum.
     *
     * @param description The description of the scientific forum.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getReviewManual() {
        return reviewManual;
    }

    /**
     * Set the review manual for the reviewers.
     *
     * @param reviewManual The review manual.
     */
    public void setReviewManual(String reviewManual) {
        this.reviewManual = reviewManual;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Set the URL that leads to the webpage of the forum.
     *
     * @param url The URL of the forum.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Set the deadline for adding submissions to the forum.
     *
      * @param deadline Deadline for new submissions.
     */
    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    /**
     * Set the name of the scientific forum.
     *
     * @param name Name of the scientific forum.
     */
    public void setName(String name) {
        this.name = name;
    }
}

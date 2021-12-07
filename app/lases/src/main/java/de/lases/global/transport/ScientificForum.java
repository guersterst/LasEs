package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a scientific forum.
 */
public class ScientificForum implements Cloneable {

    private Integer id;

    private String description;

    private String reviewManual;

    private String url;

    private LocalDateTime deadline;

    private String name;

    public Integer getId() {
        return id;
    }

    /**
     * Set the id of the scientific forum.
     * Integer is used instead of int in order to be able to determine if the property is set.
     *
     * @param id The id of the scientific forum.
     */
    public void setId(Integer id) {
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
    public void setDeadline(LocalDateTime deadline) {
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

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public ScientificForum clone() {
        try {
            return (ScientificForum) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Check equality by comparing ids.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this scientific forum?
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ScientificForum that = (ScientificForum) object;
        return Objects.equals(id, that.id);
    }
}

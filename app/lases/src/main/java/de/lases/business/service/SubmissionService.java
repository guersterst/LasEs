package de.lases.business.service;

import java.io.IOException;
import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import de.lases.persistence.repository.*;
import de.lases.global.transport.*;
import jakarta.inject.Inject;

/**
 * Provides functionality regarding the management and handling of submissions.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class SubmissionService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @return The submission's data.
     */
    public Submission getSubmission(Submission submission) {
        return null;
    }

    /**
     * Deletes a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id
     */
    public void removeSubmission(Submission submission) {
    }

    /**
     * Manipulates a submission
     *
     * @param newSubmission A {@link Submission}-DTO filled with the fields that are desired to be changed.
     *                <p>
     * All fields filled with legal values will be overwritten, the rest are ignored.
     * It should contain an existing id value.
     */
    public void changeSubmission(Submission newSubmission) {
    }

    public void setState(Submission submission, SubmissionState state) {
    }

    public void setEditor() {
    }

    public void addReviewer() {
    }

    public void removeReviewer() {
    }

    public void realeaseReview(Review review, Submission submission) {
    }

    public void addCoAuthor() {
    }

    public void uploadFile(byte[] pdf) {
    }

    public Paper downloadFile() throws IOException {
        return null;
    }

    /*
    Repo?
     */
    public void acceptSubmission() {
    }

    public void rejectSubmission() {
    }

    public boolean canView(Submission sub, User user) {
        return false;
    }

    public List<Submission> getSubmissions(ScientificForum scientificForum, User role, ResultListParameters resultParams) {
        return null;
    }

    public List<Submission> getSubmissions(User role, ResultListParameters resultParams) {
        return null;
    }

    public int countSubmissions() {
        return 0;
    }
}
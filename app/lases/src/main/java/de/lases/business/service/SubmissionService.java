package de.lases.business.service;

import java.io.IOException;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Named;
import de.lases.persistence.repository.*;
import de.lases.global.transport.*;

@ApplicationScoped
@Named
public class SubmissionService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public Submission getSubmission(int submissionId) {
        return null;
    }

    public void removeSubmission(Submission submission) {
    }

    public void changeSubmission(Submission submission, Submission newSubmission) {
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
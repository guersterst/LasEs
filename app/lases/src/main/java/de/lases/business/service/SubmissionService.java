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
     * Creates a new submission.
     * <p></p>
     * The selected editor, reviewers and co-authors are informed about this
     * using the {@code EmailUtil} utility.
     *
     * @param submission The submissions' data.
     * @param reviewers  The desired reviewers as proper {@code User}-DTOs or exclusively containing
     *                   an email-address.
     * @param coauthors  The desired co-athors as proper {@link User}-DTOs or exclusively containing
     *                   an email-address.
     */
    public void createSubmission(Submission submission, List<User> reviewers, List<User> coauthors) {

    }

    /**
     * Deletes a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     */
    public void removeSubmission(Submission submission) {
    }


    /**
     * Manipulates a submission
     *
     * @param newSubmission A {@link Submission}-DTO filled with the fields that are desired to be changed.
     *                      <p>
     *                      All fields filled with legal values will be overwritten, the rest are ignored.
     *                      It should contain an existing id value.
     */
    public void changeSubmission(Submission newSubmission) {
    }

    //todo wieso rot

    /**
     * Sets the state of a submission.
     * <p></p>
     * The submitter and all co-authors are informed about an accept or reject decision
     * by email using the {@link EmailUtil} utility.
     *
     * @param submission The {@link Submission}-DTO with a valid state.
     */
    public void setState(Submission submission) {
    }

    /**
     * Sets the editor of a submission.
     * <p></p>
     * When changed, the new editor is informed
     * by email using the {@link EmailUtil} utility.
     *
     * @param submission The {@link Submission}-DTO with a valid editor.
     */
    public void setEditor(Submission submission) {
    }

    /**
     * Adds a reviewer to a submission.
     * <p></p>
     * If successful the reviewer is informed by email using the
     * {@code EmailUtil} utility.
     *
     * @param submission The submission, that receives a new reviewer.
     * @param reviewer   The reviewer to be added to the submission.
     */
    public void addReviewer(Submission submission, User reviewer) {
    }

    /**
     * Removes a reviewer.
     * <p></p>
     * If successful the reviewer is informed by email using the
     * {@code EmailUtil} utility.
     *
     * @param submission The submission, that loses a reviewer.
     * @param reviewer   The reviewer to be removed from the submission.
     */
    public void removeReviewer(Submission submission, User reviewer) {
    }

    /**
     * Releases a review to be viewed by the submitter.
     *
     * @param review     The review to be released.
     * @param submission The submission containing that review.
     */
    public void realeaseReview(Review review, Submission submission) {
    }

    /**
     * Adds a co-author.
     *
     * @param coAuthor   The co-author to be added. This can be a regular {@link User}-DTO
     *                   or exclusively contain an email address.
     * @param submission The submission, that receives a new co-author.
     */
    public void addCoAuthor(Submission submission, User coAuthor) {
    }

    /**
     * Uploads a file to a submission.
     * <p></p>
     * Whether this is a submission-pdf or a revision-pdf is determined internally.
     *
     * @param file       The file to be added.
     * @param submission The submission, to which the file is being added.
     */
    public void uploadFile(File file, Submission submission) {
    }

    //todo what return? File or paper
    public Paper downloadFile() throws IOException {
        return null;
    }

    /**
     * Determines whether a user has permission to view a submission
     *
     * @param submission The submission to be viewed.
     * @param user       The user whose view access is being determined.
     * @return {@code true} if the view access is allowed, {@code false} otherwise.
     */
    public boolean canView(Submission submission, User user) {
        return false;
    }

    /**
     * Delivers all submissions, that a user either has submitted, is an editor to or reviews
     * in a specified scientific forum.
     *
     * @param scientificForum The {@link ScientificForum} where the wanted submissions are submitted to.
     * @param user            The user, whose editorial, reviewed or own submissions.
     * @param privilege       The role, to which submissions belong, in relation to a user.
     *                        Meaning, the user can request to receive the submissions which he is an editor to,
     *                        reviews or has submitted himself.
     * @param resultParams    Parameters, that control filtering and sorting of the resulting list.
     * @return The resulting list of submissions, that were submitted to a given scientific forum.
     */
    public List<Submission> getSubmissions(ScientificForum scientificForum, User user, Privilege privilege,
                                           ResultListParameters resultParams) {
        return null;
    }

    //todo what about coauthored submissions? one solution expand privilege for co-author.

    /**
     * Delivers all submissions, that a user has submitted, is an editor to or reviews.
     *
     * @param privilege    The role, to which submissions belong, in relation to a user.
     *                     Meaning, the user can request to receive the submissions which he is an editor to,
     *                     reviews or has submitted himself.
     * @param user         The user, whose editorial, reviewed or own submissions.
     * @param resultParams Parameters, that control filtering and sorting of the resulting list.
     * @return The resulting list of submissions, which a user is involved in.
     */
    public List<Submission> getSubmissions(Privilege privilege, User user, ResultListParameters resultParams) {
        return null;
    }


    //todo was macht das wofür?
    public int countSubmissions() {
        return 0;
    }
}
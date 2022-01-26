package de.lases.control.backing;

import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.business.util.EmailUtil;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Backing bean for the toolbar view. This view belongs to the submission page.
 *
 * @author Stefanie Gürster
 */
@ViewScoped
@Named
public class ToolbarBacking implements Serializable {


    @Serial
    private static final long serialVersionUID = -2791102953586117163L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private UserService userService;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    private Submission submission;

    private User reviewerInput;

    private ReviewedBy reviewedByInput;

    private User editorInput;

    private User currentEditor;

    private LinkedHashMap<User, ReviewedBy> reviewer;

    private List<User> editors;

    /**
     * Initialize the dtos.
     * Create the dtos for:
     * <ul>
     *     <li> the submission </li>
     *     <li> the reviewer input </li>
     *     <li> the "reviewed by" input </li>
     *     <li> the editor input </li>
     *     <li> current editor </li>
     *     <li>
     *         the list of reviewers of the submission this toolbar is
     *         about
     *     </li>
     *     <li>
     *         the list of editors of the scientific forum for this
     *         submission
     *     </li>
     * </ul>
     * No data can be loaded form the datasource at this point. (See the
     * {@code onLoad() method}).
     */
    @PostConstruct
    public void init() {
        submission = new Submission();
        reviewerInput = new User();
        reviewedByInput = new ReviewedBy();
        reviewer = new LinkedHashMap<>();
        editors = new ArrayList<>();
        currentEditor = new User();
    }

    /**
     * Load data that was not loaded in the {@code init()} method from the
     * datasource:
     * <ul>
     *     <li> The current editor of the submission. </li>
     *     <li> The list of editors of the submission. </li>
     *     <li>
     *         The list of reviewers for this submission
     *     </li>
     *     <li>
     *         The list of editors of the scientific forum this submission is in
     *     </li>
     * </ul>
     * This method is called after the id of our submission was received via
     * view params by the {@code SubmissionBacking}. This method is <b>not</b>
     * called by a view action but rather by the onLoad method in the
     * {@code SubmissionBacking}.
     */
    public void onLoad(Submission sub) {
        submission = sub;

        loadReviewerList();

        currentEditor.setId(submission.getEditorId());
        currentEditor = userService.get(currentEditor);

        ScientificForum scientificForum = new ScientificForum();
        scientificForum.setId(submission.getScientificForumId());

        editors = userService.getList(scientificForum);
    }

    private void loadReviewerList() {
        reviewer = new LinkedHashMap<>();
        List<User> userList = userService.getList(submission, Privilege.REVIEWER);
        List<ReviewedBy> reviewedByList = submissionService.getList(submission);

        boolean hasNoDeadline = true;
        for (User user : userList) {
            for (ReviewedBy reviewedBy : reviewedByList) {
                if (user.getId().equals(reviewedBy.getReviewerId())) {
                    reviewer.put(user, reviewedBy);
                    hasNoDeadline = false;
                    break;
                }
            }

            if (hasNoDeadline) {
                reviewer.put(user, null);
            }
            hasNoDeadline = true;
        }
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener(ComponentSystemEvent event) {
        if (submission.getId() == null) {
            throw new IllegalUserFlowException("Submission page called without an id.");
        }
    }

    /**
     * The currently entered user will be added as a reviewer.
     */
    public void addReviewer() {
        User newReviewer = new User();
        newReviewer.setEmailAddress(reviewerInput.getEmailAddress());


        ReviewedBy reviewedBy = reviewedByInput.clone();
        reviewedBy.setSubmissionId(submission.getId());
        reviewedBy.setHasAccepted(AcceptanceStatus.NO_DECISION);

        submissionService.manageReviewer(newReviewer, reviewedBy, getReviewer());

        loadReviewerList();
    }

    /**
     * Remove the specified user form the list of reviewers.
     *
     * @param user The user to remove from the list of reviewers.
     */
    public void removeReviewer(User user) {
        submissionService.removeReviewer(submission, user);
        uiMessageEvent.fire(new UIMessage(user.getEmailAddress() + " " + resourceBundle.getString("removeReviewer"), MessageCategory.INFO));
        loadReviewerList();
    }

    /**
     * Add the entered user as managing editor, replacing the old editor.
     */
    public void chooseNewManagingEditor() {
        Submission newSubmission = submission.clone();
        newSubmission.setEditorId(currentEditor.getId());
        submissionService.change(newSubmission);

        for (User user : editors) {
            if (user.getId().equals(newSubmission.getEditorId())) {
                currentEditor = user;
            }
        }

        uiMessageEvent.fire(new UIMessage(resourceBundle.getString("changeData"), MessageCategory.INFO));
    }

    /**
     * Checks whether a user has a title in order to print it.
     *
     * @param user Given user that has to be checked for a title.
     * @return A {@code null} if there is no title. Otherwise, returns the title.
     */
    public String getTitle(User user) {
        if (user == null) {
            return "";
        }
        return Objects.requireNonNullElse(user.getTitle(), "");
    }

    /**
     * State that a revision is required on the submission belonging to this
     * page.
     */
    public void requireRevision() {
        if (!submission.isRevisionRequired()) {
            submission.setRevisionRequired(true);
            submission.setState(SubmissionState.REVISION_REQUIRED);
        }
        submissionService.change(submission);
    }

    /**
     * Accept the submission belonging to this page.
     */
    public void acceptSubmission() {
        Submission newSubmission = submission.clone();
        newSubmission.setState(SubmissionState.ACCEPTED);

        if (submissionService.change(newSubmission)) {
            submission.setState(SubmissionState.ACCEPTED);
        }

    }

    public boolean isAccepted() {
        return submission.getState() == SubmissionState.ACCEPTED;
    }

    /**
     * Reject the submission belonging to this page.
     */
    public void rejectSubmission() {
        Submission newSubmission = submission.clone();
        newSubmission.setState(SubmissionState.REJECTED);

        if (submissionService.change(newSubmission)) {
            submission.setState(SubmissionState.REJECTED);
        }
    }

    public boolean isRejected() {
        return submission.getState() == SubmissionState.REJECTED;
    }

    /**
     * Get the submission belonging to this page.
     *
     * @return The submission belonging to this page.
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Set the submission belonging to this page.
     *
     * @param submission The new submissions.
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * Get the user that is input as a reviewer.
     *
     * @return The new reviewer.
     */
    public User getReviewerInput() {
        return reviewerInput;
    }

    /**
     * Set the user that is input as a reviewer.
     *
     * @param reviewerInput The new reviewer.
     */
    public void setReviewerInput(User reviewerInput) {
        this.reviewerInput = reviewerInput;
    }

    /**
     * Get the user that is input as a reviewer.
     *
     * @return The new reviewer.
     */
    public ReviewedBy getReviewedByInput() {
        return reviewedByInput;
    }

    /**
     * Set meta information about the reviewer that is supposed to be added.
     * This is used to set a deadline for the new reviewer.
     *
     * @param reviewedByInput Meta information about the new reviewer.
     */
    public void setReviewedByInput(ReviewedBy reviewedByInput) {
        this.reviewedByInput = reviewedByInput;
    }

    /**
     * Get the user that is currently entered as the new editor.
     *
     * @return The user that is supposed to become the new editor.
     */
    public User getEditorInput() {
        return editorInput;
    }

    /**
     * Set the user that is currently entered as the new editor.
     *
     * @param editorInput User that is supposed to become the new editor.
     */
    public void setEditorInput(User editorInput) {
        this.editorInput = editorInput;
    }

    /**
     * Get the current editor of the submission.
     *
     * @return The editor of the submission.
     */
    public User getCurrentEditor() {
        return currentEditor;
    }

    /**
     * Get the list of reviewers of the submission.
     *
     * @return The list of reviewers.
     */
    public List<User> getReviewer() {
        return reviewer.keySet().stream().toList();
    }

    public List<ReviewedBy> getReviewerDeadline() {
        return reviewer.values().stream().toList();
    }

    /**
     * Get the list of editors for the scientific forum this submission belongs
     * to.
     *
     * @return The list of editors.
     */
    public List<User> getEditors() {
        return editors;
    }

    /**
     * Get session information.
     *
     * @return The session information.
     */
    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }

    /**
     * Return if the logged-in user is editor of this submission.
     *
     * @return Is the logged-in user editor of this submission?
     */
    public boolean loggedInUserIsEditor() {
        return false;
    }

    /**
     * Return if the logged-in user is reviewer of this submission.
     *
     * @return Is the logged-in user reviewer of this submission?
     */
    public boolean loggedInUserIsReviewer() {
        return false;
    }

    /**
     * Fill form in order to change a reviewedBy DTO.
     *
     * @param user The reviewer to change.
     */
    public void changeReviewing(User user) {
        reviewerInput = user;
        reviewedByInput.setTimestampDeadline(reviewer.get(user).getTimestampDeadline());
    }

    /**
     * Generates a placeholder with the current datetime and local.
     *
     * @return current datetime with a local pattern.
     */
    public String generateDatePlaceholder() {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        String languageTag = locale.toLanguageTag();

        SimpleDateFormat simpleDateFormat;
        LocalDateTime today = LocalDateTime.now();
        if (languageTag.equals("de")) {
            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss", locale);
            return today.format(DateTimeFormatter.ofPattern(simpleDateFormat.toPattern()));
        } else {
            simpleDateFormat = new SimpleDateFormat("MMM d, yyyy, hh:mm:ss", locale);
            String date = today.format(DateTimeFormatter.ofPattern(simpleDateFormat.toPattern()));
            StringBuilder formatDate = new StringBuilder(date);
            formatDate.deleteCharAt(3);
            return formatDate.toString();
        }
    }

    /**
     * Generates a mail to link.
     *
     * @param user the recipient.
     * @return a mail to link.
     */
    public String sendMailTo(User user) {
        String[] email = {user.getEmailAddress()};
        return EmailUtil.generateMailToLink(email, null, null, null);
    }

}

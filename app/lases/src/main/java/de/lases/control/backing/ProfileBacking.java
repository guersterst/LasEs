package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Backing bean for the profile page.
 */
@ViewScoped
@Named
public class ProfileBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -6134346581517123811L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private UserService userService;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private ScienceFieldService scienceFieldService;

    private Part uploadedAvatar;

    private User user;

    private List<ScienceField> usersScienceFields;

    private ScienceField selectedScienceField;

    private List<ScienceField> scienceFields;

    private User adminPasswordInPopup;

    private boolean popupShown;

    /**
     * Initializes the dto objects and gets the global list of science fields
     * from the datasource.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Get the user id from the view param and load their data from the
     * datasource. This method should be called by a view action.
     */
    public void onLoad() { }

    /**
     * Save the changes to the user profile excluding profile picture and
     * science fields. If the user was elevated to admin, a popup menu will
     * ask for confirmation again.
     */
    public void submitChanges() {
    }

    /**
     * Set a new avatar for the user.
     */
    public void uploadAvatar() {
    }

    /**
     * Delete the current avatar for the user.
     */
    public void deleteAvatar() {
    }

    /**
     * Add selected science field to the user's science fields.
     */
    public void addScienceField() {
    }

    /**
     * Remove a science field from the user's science fields.
     *
     * @param field The science field to be removed.
     */
    public void deleteScienceField(ScienceField field) {
    }

    /**
     * Abort the changes made to the user.
     */
    public void abortInPopup() {
    }

    /**
     * Save the changes made to the user.
     */
    public void saveInPopup() {
    }

    /**
     * Delete the of the user.
     *
     * @return Go to the welcome page.
     */
    public String deleteProfile() {
        return null;
    }

    /**
     * Get the number of submission of the logged-in user.
     *
     * @return The number of submissions of the logged-in user.
     */
    public int getNumberOfSubmissions() { return -1; }


    public Part getUploadedAvatar() {
        return uploadedAvatar;
    }

    /**
     * Set the avatar that the user uploaded.
     *
     * @param uploadedAvatar The avatar that the user uploaded.
     */
    public void setUploadedAvatar(Part uploadedAvatar) {
        this.uploadedAvatar = uploadedAvatar;
    }

    public User getUser() {
        return user;
    }

    /**
     * Set the user whose profile this is.
     *
     * @param user The user whose profile this is.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the list of science fields of the user.
     *
     * @return The list of science fields of the user.
     */
    public List<ScienceField> getUsersScienceFields() {
        return usersScienceFields;
    }

    public ScienceField getSelectedScienceField() {
        return selectedScienceField;
    }

    /**
     * Set the science field that is selected to be added.
     *
     * @param selectedScienceField The science field that is selected to be
     *                             added.
     */
    public void setSelectedScienceField(ScienceField selectedScienceField) {
        this.selectedScienceField = selectedScienceField;
    }

    public User getAdminPasswordInPopup() {
        return adminPasswordInPopup;
    }

    /**
     * Set the user that holds the password the admin has to enter on the
     * pop-up.
     *
     * @param adminPasswordInPopup User that holds the admin password.
     */
    public void setAdminPasswordInPopup(User adminPasswordInPopup) {
        this.adminPasswordInPopup = adminPasswordInPopup;
    }

    /**
     * Get the list of science fields in the system.
     *
     * @return The list of science fields in the system.
     */
    public List<ScienceField> getScienceFields() {
        return scienceFields;
    }

    /**
     * Tells if the popup is currently displayed on the screen.
     *
     * @return Is the popup currently displayed on the screen?
     */
    public boolean isPopupShown() {
        return popupShown;
    }

    /**
     * Only the user belonging to the profile and an administrator have edit rights.
     * @return true if they have edit rights.
     */
    public boolean hasViewerEditRights() {
        return sessionInformation.getUser().id == user.getId() || sessionInformation.getUser().isAdmin();
    }
}

package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import de.lases.control.exception.IllegalAccessException;
import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ComponentSystemEvent;
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
    private ScienceFieldService scienceFieldService;

    @Inject
    private ConfigPropagator configPropagator;

    private Part uploadedAvatar;

    private User user;

    private List<ScienceField> usersScienceFields;

    private ScienceField selectedScienceField;

    private List<ScienceField> scienceFields;

    private User adminPasswordInPopup;

    /**
     * Initializes the dto objects and gets the global list of science fields
     * from the datasource.
     * The following objects are initialized by this method:
     * <ul>
     *     <li> The uploaded avatar </li>
     *     <li> The user object of the profile </li>
     *     <li>
     *         the list of science fields of the user this profile is about
     *     </li>
     *     <li>
     *         the list of global science fields
     *     </li>
     *     <li>
     *         the science field that is currently selected to be added
     *     </li>
     *     <li>
     *         the user for the admin password input
     *     </li>
     * </ul>
     * @throws IllegalAccessException If someone tries to access a profile that
     *                                does not belong to him.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Get the user id from the view param and load their data from the
     * datasource. This method should be called by a view action.
     * The following data is loaded from the datasource by this method:
     * <ul>
     *     <li>
     *         The user object of this profile
     *     </li>
     *     <li>
     *         the list of science fields of the user
     *     </li>
     * </ul>
     */
    public void onLoad() { }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @param event The component system event that happens before rendering
     *              the view param.
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener(ComponentSystemEvent event) {}

    /**
     * Save the changes to the user profile excluding profile picture and
     * science fields. If the user changed his email address, a verification
     * email will be sent to that address and a messge will be shown that asks
     * the user to check his inbox.
     * If the user was elevated to admin, the admins' password will be checked
     * and the form will only be submitted if it is correct. Otherwise, an
     * error message will be shown.
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
     * Abort the changes made to the user and show a fresh profile page.
     */
    public void abort() {
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

    /**
     * Get the avatar that the user uploaded.
     *
     * @return The avatar that the user uploaded.
     */
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

    /**
     * Get the user whose profile this is.
     *
     * @return The user whose profile this is.
     */
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

    /**
     * Set the science field that is selected to be added.
     *
     * @return The science field that is selected to be added.
     */
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

    /**
     * Get the user that holds the password the admin has to enter on the
     * pop-up.
     *
     * @return User that holds the admin password.
     */
    public User getAdminPassword() {
        return adminPasswordInPopup;
    }

    /**
     * Set the user that holds the password the admin has to enter on the
     * pop-up.
     *
     * @param adminPasswordInPopup User that holds the admin password.
     */
    public void setAdminPassword(User adminPasswordInPopup) {
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
     * Only the user belonging to the profile and an administrator have edit rights.
     * @return true if they have edit rights.
     */
    public boolean hasViewerEditRights() {
        return sessionInformation.getUser().getId() == user.getId()
                || sessionInformation.getUser().isAdmin();
    }
}

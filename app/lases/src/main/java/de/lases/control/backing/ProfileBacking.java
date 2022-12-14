package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import de.lases.control.exception.IllegalAccessException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Event;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;

/**
 * Backing bean for the profile page.
 *
 * @author Sebastian Vogt
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
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle messageBundle;

    private Part uploadedAvatar;

    private User user;

    private User userForAdminSettings;

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
     */
    @PostConstruct
    public void init() {
        user = new User();
        scienceFields = new ArrayList<>();
        usersScienceFields = new ArrayList<>();
        selectedScienceField = new ScienceField();
        adminPasswordInPopup = new User();

        scienceFields = scienceFieldService.getList(new ResultListParameters());
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
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     * @throws IllegalAccessException If someone who is not an editor or admin tries to access a profile that
     *                                does not belong to him.
     */
    public void onLoad() {
        if (user.getId() == null) {
            throw new IllegalUserFlowException("Profile page called without an id");
        }
        User accessingUser = sessionInformation.getUser();
        if (!(accessingUser.isAdmin() || accessingUser.isEditor()
                || Objects.equals(accessingUser.getId(), user.getId()))) {
            throw new IllegalAccessException("Illegal access to profile.");
        }
        user = userService.get(user);
        userForAdminSettings = user.clone();
        usersScienceFields = scienceFieldService.getList(user, new ResultListParameters());
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener() {
        if (user.getId() == null) {
            throw new IllegalUserFlowException("Profile page called without an id.");
        }
    }

    /**
     * Save the changes to the user profile excluding profile picture,
     * science fields and admin rights. If the user changed his email address,
     * a verification email will be sent to that address and a message will be
     * shown that asks the user to check his inbox.
     */
    public void submitChanges() {
        userService.change(user, sessionInformation.getUser());
    }

    /**
     * Save the changes to the user's admin status. This can only be done by
     * an admin who is editing someone's profile. He must enter his password
     * to complete this action. If he did enter his valid password, the admin
     * status will be changed. If the password is wrong, an error message will
     * be displayed.
     */
    public void submitAdminChanges() {
        userService.change(userForAdminSettings, sessionInformation.getUser());
    }

    /**
     * Set a new avatar for the user.
     */
    public void uploadAvatar() {
        FileDTO avatar = new FileDTO();
        try {
            avatar.setFile(uploadedAvatar.getInputStream().readAllBytes());
            userService.setAvatar(avatar, user);
        } catch (IOException e) {
            uiMessageEvent.fire(new UIMessage(messageBundle.getString("failedUpload"), MessageCategory.ERROR));
        }
    }

    /**
     * Delete the current avatar for the user.
     */
    public void deleteAvatar() {
        userService.setAvatar(null, user);
    }

    /**
     * Add selected science field to the user's science fields.
     */
    public void addScienceField() {
        if (!usersScienceFields.contains(selectedScienceField)) {
            ScienceField selectedClone = selectedScienceField.clone();
            usersScienceFields.add(selectedClone);
            userService.addScienceField(user, selectedClone);
        }
    }

    /**
     * Remove a science field from the user's science fields.
     *
     * @param field The science field to be removed.
     */
    public void deleteScienceField(ScienceField field) {
        usersScienceFields.remove(field);
        userService.removeScienceField(user, field);
    }

    /**
     * Delete the of the user.
     *
     * @return Go to the welcome page.
     */
    public String deleteProfile() {
        if( sessionInformation.getUser().equals(user)) {
            userService.remove(user);
            sessionInformation.setUser(null);
            return "/views/anonymous/welcome.xhtml?faces-redirect=true";
        } else {
            userService.remove(user);
            return "/views/authenticated/homepage.xhtml";
        }
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
        return Objects.equals(sessionInformation.getUser(), user)
                || sessionInformation.getUser().isAdmin();
    }

    /**
     * Get the user object that should be used to write the admin changes.
     *
     * @return The user.
     */
    public User getUserForAdminSettings() {
        return userForAdminSettings;
    }

    /**
     * Set the user object that should be used to write the admin changes.
     *
     * @param userForAdminSettings The user.
     */
    public void setUserForAdminSettings(User userForAdminSettings) {
        this.userForAdminSettings = userForAdminSettings;
    }
}

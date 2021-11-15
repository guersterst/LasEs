package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.util.List;

@RequestScoped
@Named
public class ProfileBacking {

    private SessionInformation sessionInformation;

    private Part uploadedAvatar;

    private UserService userService;

    private SubmissionService submissionService;

    private ScienceFieldService scienceFieldService;

    private User user;

    private User newUser;

    private List<ScienceField> selectedScienceFields;

    private List<ScienceField> scienceFields;

    private ScienceField selectedScienceField;

    private int numberOfSubmissions;

    private String passwordInput;

    private boolean popupShown;

    @PostConstruct
    public void init() {
    }

    public void submitChanges() {
    }

    public void uploadAvatar() {
    }

    public void deleteAvatar() {
    }

    public void addScienceField() {
    }

    public void deleteScienceField(int id) {
    }

    public void abortInPopup() {
    }

    public void saveInPopup() {
    }

    public String deleteProfile() {
        return null;
    }

}

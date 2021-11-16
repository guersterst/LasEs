package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serializable;
import java.util.List;

@RequestScoped
@Named
public class NewSubmissionBacking implements Serializable {


    // Services and Session.

    private SessionInformation sessionInformation;

    private SubmissionService submissionService;

    private ScientificForumService scientificForumService;

    private UserService userService;

    // Inputs from view.

    private List<User> coAuthors;

    private List<User> editors;

    private String nameOfPaperInput;

    private String forumNameInput;

    private User editorSelectionInput;

    private Part uploadedPDF;

    private String titleInput;

    private String coAuthorTitleInput;

    private String coAuthorFirstNameInput;

    private String coAuthorLastNameInput;

    private String coAuthorEmailInput;

    @PostConstruct
    public void init() {
    }

    // Action methods.

    public void submitCoAuthor() {
    }

    public void deleteCoAuthor(int id) {
    }

    public void submit() {
    }

}

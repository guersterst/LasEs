package de.lases.control.backing;

import de.lases.business.service.LoginService;
import de.lases.global.transport.*;
import de.lases.control.internal.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;


@RequestScoped
@Named
public class NavigationBacking {

    private String searchString; // y

    private LoginService loginService;

    private SessionInformation sessionInformation;

    private List<User> userSearchResults;

    private List<ScientificForum> forumSearchResults;

    private List<Submission> submissionSearchResults;

    public List<User> getUserSearchResults() {
        return null;
    }

    public List<ScientificForum> getScientificForums() {
        return null;
    }

    public List<Submission> getSubmissionSearchResults() {
        return null;
    }


    @PostConstruct
    public void init() {
    }

    public String logout() {
        return null;
    } // y

    public String search() {
        return null;
    } // y

}

package de.lases.control.backing;

import de.lases.business.service.LoginService;
import de.lases.global.transport.*;
import de.lases.control.internal.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

/**
 * Backing bean for the navigation bar.
 */
@RequestScoped
@Named
public class NavigationBacking {


    //TODO:Bild des logos braucht man für Navigationsleiste.

    private String searchString; // y


    @Inject
    private LoginService loginService;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private ResultListBacking resultListBacking;

    private ResultListParameters resultListParameters;

    /**
     * Log the user out of the system and got to the welcome page.
     *
     * @return Show the welcome page.
     */
    public String logout() {
        return null;
    }

    /**
     * Submit the entered search word and go to the result list page.
     *
     * @return Show the result list page.
     */
    public String search() {
        return null;
    }

    public ResultListParameters getResultListParameters() {
        return resultListParameters;
    }

    /**
     * Get the parameters for the result lists, which specifically contains a
     * global search word.
     *
     * @param resultListParameters The parameters for the result lists.
     */
    public void setResultListParameters(
            ResultListParameters resultListParameters) {
        this.resultListParameters = resultListParameters;
    }
}

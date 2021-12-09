package de.lases.control.backing;

import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.ResultListParameters;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.logging.Logger;

/**
 * Backing bean for the navigation bar.
 */
@RequestScoped
@Named
public class NavigationBacking {

    private static final Logger logger = Logger.getLogger(NavigationBacking.class.getName());

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private ResultListBacking resultListBacking;

    private ResultListParameters resultListParameters;

    /**
     * Create the {@link ResultListParameters}-DTO required for the search.
     */
    @PostConstruct
    public void init() {
       resultListParameters = new ResultListParameters();
    }
    /**
     * Log the user out of the system and got to the welcome page.
     *
     * @return Show the welcome page.
     */
    public String logout() {
        logger.finest("User: " + sessionInformation.getUser().getId() + "logged out.");
        sessionInformation.setUser(null);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "welcome.xhtml?faces-redirect=true";
    }

    /**
     * Submit the entered search word and go to the result list page.
     *
     * @return Show the result list page.
     */
    public String search() {
        String searchWord = resultListParameters.getGlobalSearchWord();
        if (searchWord == null) {
            searchWord = "";
        }
        resultListBacking.setSearchWord(searchWord);
        return "/views/authenticated/resultList.xhtml?faces-redirect=true";
    }

    /**
     * Set the parameters for the result lists, which specifically contains a
     * global search word.
     *
     * @return The parameters for the result list.
     */
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

    /**
     * Get session information.
     *
     * @return The session information.
     */
    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }
}

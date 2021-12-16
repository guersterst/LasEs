package de.lases.control.internal;

import de.lases.control.exception.IllegalAccessException;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.User;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

import java.io.Serial;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Listens for the restore view phase and checks the user's rights to access
 * the requested page after the phase.
 */
public class TrespassListener implements PhaseListener {

    @Serial
    private static final long serialVersionUID = -1137139795334466811L;

    private PropertyResourceBundle propertyResourceBundle =
            (PropertyResourceBundle) ResourceBundle.getBundle("resource_bundles/message");

    private final Logger logger = Logger.getLogger(TrespassListener.class.getName());

    /**
     * Gets the is of the phase this listener is interested in.
     *
     * @return The id of the restore view phase.
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    /**
     * Before the restore view phase, this listener does nothing.
     *
     * @param event The event that is about to happen.
     */
    @Override
    public void beforePhase(PhaseEvent event) {
        // No op
    }

    /**
     * After the restore view phase, we check if the user has the rights to
     * visit the site he has requested. If not, he will be redirected to a 404
     * error page.
     *
     * @param event The event that just happened.
     * @throws IllegalAccessException If there is an attempt to access a page to which the user does not have
     *                                access with his current roles.
     */
    @Override
    public void afterPhase(PhaseEvent event) {

        FacesContext fctx = event.getFacesContext();
        SessionInformation sessionInformation = CDI.current().select(SessionInformation.class).get();
        User user = sessionInformation.getUser();

        UIViewRoot viewRoot = fctx.getViewRoot();

        if (viewRoot == null) {
            logger.severe("The view root is null.");
            throw new IllegalAccessException();
        }

        // Load localised messages to avoid CDI.
        propertyResourceBundle = (PropertyResourceBundle)
                ResourceBundle.getBundle("resource_bundles/message", viewRoot.getLocale());

        String viewId = viewRoot.getViewId();
        if (viewId.contains("/anonymous/")) {

            // Do nothing and let the unregistered user remain on this site.
        } else if (user == null || (!user.isRegistered() && !viewId.contains("/anonymous/"))) {

            // Illegal access to a site which is visible to registered users only.
            logger.warning("An unregistered user tried illegally access "
                    + "a page using the url: " + viewId);
            navigateToLogin(fctx);
        } else if (!user.isVerified()) {

            // Illegal access by user without verified email address.
            logger.warning("An unverified user tried illegally access a page using the url: " + viewId);
            throw new IllegalAccessException(propertyResourceBundle.getString("unverified"));
        } else if (!user.getPrivileges().contains(Privilege.EDITOR) && !user.isAdmin() && viewId.contains("/editor/")) {

            // Illegal access to a site which is visible to editors and admins only.
            logger.warning("The non-editor or non-admin user with the id: " + user.getId() + " tried to illegally "
                    + "access a page using the url: " + viewId);
            throw new IllegalAccessException(propertyResourceBundle.getString("illegalAccess"));
        } else if (!user.isAdmin() && viewId.contains("/admin/")) {

            // Illegal access to a site which is visible to admins only.
            logger.warning("The non-admin user with the id: " + user.getId() + " tried to illegally "
                    + "access a page using the url: " + viewId);
            throw new IllegalAccessException(propertyResourceBundle.getString("illegalAccess"));
        }
    }

    /**
     * Redirect a user to the login page and hint to him to attempt to login before
     * accessing restricted sites using a {@code FacesMessage}.
     *
     * @param fctx The current instance of the faces-context.
     */
    private void navigateToLogin(FacesContext fctx) {
        setErrorMessage(fctx, propertyResourceBundle.getString("unauthenticatedAccess"));

        NavigationHandler nav = fctx.getApplication().getNavigationHandler();
        nav.handleNavigation(fctx, null, "/views/anonymous/welcome.xhtml?faces-redirect=true");
        fctx.responseComplete();
    }

    /**
     * Display a {@code FacesMessage} to the user.
     *
     * @param fctx The current instance of the faces-context.
     * @param message The message to be displayed.
     */
    private void setErrorMessage(FacesContext fctx, String message) {
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
        fctx.addMessage(null, fmsg);

        // Let the faces messages of fctx also live in the next request. The
        // flash scope lives exactly for two subsequent requests.
        ExternalContext ctx = fctx.getExternalContext();
        ctx.getFlash().setKeepMessages(true);
    }

}

package de.lases.control.internal;

import de.lases.control.exception.IllegalAccessException;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;

import java.io.Serial;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Listens for the restore view phase and checks the user's rights to access
 * the requested page after the phase.
 */
public class TrespassListener implements PhaseListener {

    @Serial
    private static final long serialVersionUID = -1137139795334466811L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private PropertyResourceBundle propertyResourceBundle;

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
        // no op
    }

    /**
     * After the restore view phase, we check if the user has the rights to
     * visit the site he has requested. If not, he will be redirected to a 404
     * error page.
     *
     * @param event The event that just happened.
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fctxt = event.getFacesContext();
        ExternalContext ectxt = fctxt.getExternalContext();

        SessionInformation sessionInformation
                = CDI.current().select(SessionInformation.class).get();

        UIViewRoot viewRoot = fctxt.getViewRoot();
        String viewId = null;
        if (viewRoot != null) {
            viewId = viewRoot.getViewId();

            //TODO what if viewRoot null?
            // -> just throw Error
            //TODO is case sensitive?
        }

        //TODO what if user null
        User user = sessionInformation.getUser();

        boolean isRegistered = user.isRegistered();
        boolean isAdmin = user.isAdmin();
        boolean isEditor = user.getPrivileges().contains(Privilege.EDITOR);

        //TODO throw control exceptions and let unchecked exc handler handle navigation?

        if (!isRegistered && !viewId.contains("/anonymous/")) {

            // Illegal access to a site which is visible to registered users only.
            navigateToLogin(fctxt);
            logger.warning("The unregistered user with the id: " + user.getId() + " tried illegally access "
                    + "a page using the url: " + viewId);
        } else if (!isEditor && !isAdmin && viewId.contains("/editor/")) {

            // Illegal access to a site which is visible to editors and admins only.
            logger.warning("The non-editor or non-admin user with the id: " + user.getId() + " tried to illegally "
                    + "access a page using the url: " + viewId);
            throw new IllegalAccessException(propertyResourceBundle.getString("illegalAccess"));
        } else if (!isAdmin && viewId.contains("/admin/")) {

            // Illegal access to a site which is visible to admins only.
            logger.warning("The non-admin user with the id: " + user.getId() + " tried to illegally "
                    + "access a page using the url: " + viewId);
            throw new IllegalAccessException(propertyResourceBundle.getString("illegalAccess"));
        }
    }

    private void navigateToLogin(FacesContext facesContext) {
        uiMessageEvent.fire(new UIMessage(
                propertyResourceBundle.getString("unauthenticatedAccess"), MessageCategory.ERROR));

        NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
        nav.handleNavigation(facesContext, null, "welcome.xhtml?faces-redirect=true");
        facesContext.responseComplete();
    }
}

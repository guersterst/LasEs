package de.lases.control.internal;

import de.lases.global.transport.Privilege;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;

import java.io.Serial;

/**
 * Listens for the restore view phase and checks the user's rights to access
 * the requested page after the phase.
 */
public class TrespassListener implements PhaseListener {

    @Serial
    private static final long serialVersionUID = -1137139795334466811L;

    /**
     * Gets the is of the phase this listener is interested in.
     *
     * @return The id of the restore view phase.
     */
    @Override
    public PhaseId getPhaseId() {
        return null;
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

        boolean isRegistered = sessionInformation.getUser().isRegistered();
        boolean isAdmin = sessionInformation.getUser().isAdmin();
        boolean isEditor = sessionInformation.getUser().getPrivileges().contains(Privilege.EDITOR);

        //TODO throw control exceptions and let unchecked exc handler handle navigation?

        if (!isRegistered && !viewId.contains("/anonymous/")) {
          //not allowed
        }
        if (!isEditor && !isAdmin && viewId.contains("/admin/")) {
            // tschö
        }





        else if ((viewId.contains("/authenticated/") || viewId.contains("/reviewer/"))) {

        }


        // NOT AUTH
        // /authenticated /reviewer
        // NOT EDTIOR
        // /editor
        // NOT ADMIN
        // /admin
    }

    private static void navigateToLogin() {

    }
}

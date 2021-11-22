package de.lases.control.internal;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

public class TrespassListener implements PhaseListener {

    private final SessionInformation sessionInformation
            = CDI.current().select(SessionInformation.class).get();

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
    }


}

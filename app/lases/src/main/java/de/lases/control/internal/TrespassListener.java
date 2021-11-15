package de.lases.control.internal;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

public class TrespassListener implements PhaseListener {

    // @Inject
    //private final SessionInformation sessionInformation
    //        = CDI.current().select(SessionInformation.class).get();

    @Override
    public PhaseId getPhaseId() {
        return null;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }


}

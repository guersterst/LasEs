package control.internal;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import business.internal.CDI;

public class TrespassListener {

    // @Inject
    private final SessionInformation sessionInformation
            = CDI.current().select(SessionInformation.class).get();

    @Override
    public PhaseId getPhaseId() { }

    @Override
    public void beforePhase(PhaseEvent event) { }

    @Override
    public void afterPhase(PhaseEvent event) { }


}

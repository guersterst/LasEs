package de.lases.business.internal;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named
public class PeriodicWorker implements Runnable {

    @Override
    public void run() {
    }

    public void shutdown() {
    }

}

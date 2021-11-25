package de.lases.business.internal;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Cleans up the email verifications in the database every 12 hours.
 */
@ApplicationScoped
public class PeriodicWorker implements Runnable {

    /**
     * Cleans up the email verifications in the database every 12 hours.
     */
    @Override
    public void run() {
    }

}

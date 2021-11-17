package de.lases.business.internal;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Executes periodic cleanup jobs from the database.
 */
@ApplicationScoped
public class PeriodicWorker implements Runnable {

    /**
     * Performs cleanup of data, that is no longer in needed.
     */
    @Override
    public void run() {
    }

    /**
     * Performs a clean shut down of {@code this}.
     */
    public void shutdown() {
    }

}

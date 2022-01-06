package de.lases.business.internal;

import de.lases.persistence.util.DatasourceUtil;
import org.jboss.logging.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Cleans up the email verifications in the database every 12 hours.
 */
public class PeriodicWorker {

    private static final int TWELVE_HOURS_MS = 1000 * 60 * 60 * 12;
    private static final Logger logger = Logger.getLogger(PeriodicWorker.class);

    private static final Timer timer = new Timer("PeriodicWorker");
    private static final TimerTask cleanUpTask = new TimerTask() {
        @Override
        public void run() {
            DatasourceUtil.cleanUpVerifications();
        }
    };

    /**
     * Scheduling the tasks to be run.
     */
    public static void init() {
        logger.info("Initialising Periodic Worker with Clean Up Task.");
        timer.schedule(cleanUpTask, TWELVE_HOURS_MS, TWELVE_HOURS_MS);
    }

    /**
     * Stopping the worker, after any running tasks (if any) are completed.
     */
    public static void stop() {
        timer.cancel();
    }

}

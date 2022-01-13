package de.lases.persistence.util;

import java.util.List;

/**
 * Checks if a psql error code hints at a transient error that can be solved by retrying without changing anything.
 */
public class TransientSQLExceptionChecker {

    private static final List<String> transientCodes = List.of(
            // insufficient_resources
            "53000",
            // out_of_memory
            "53200",
            // too_many_connections
            "53300",
            // configuration_limit_exceeded
            "53400",
            // operator_intervention
            "57000",
            // query_canceled
            "57014",
            // admin_shutdown
            "57P01",
            // crash_shutdown
            "57P02",
            // system_error
            "58000",
            // lock_not_available
            "55P03",
            // object_in_use
            "55006"
    );

    /**
     * Checks if a psql error code hints at a transient error that can be solved by retrying without changing anything.
     *
     * @param psqlErrorCode The postgres error code.
     */
    public static boolean isTransient(String psqlErrorCode) {
        return transientCodes.contains(psqlErrorCode);
    }

}

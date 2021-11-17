package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Bundles information about the verification process is somebody is newly
 * registered or has just changed their email.
 */
public class Verification {

    private User user;

    private int id;


    private String validationRandom;

    private boolean verified;

    private LocalDateTime timestampValidationStarted;

    private String nonValidatedEmailAddress;

}

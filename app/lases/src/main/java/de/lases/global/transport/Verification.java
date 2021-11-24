package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Bundles information about the verification process is somebody is newly
 * registered or has just changed their email.
 */
public class Verification implements Cloneable {

    private int userId;

    private int id;

    private String validationRandom;

    private boolean verified;

    private LocalDateTime timestampValidationStarted;

    private String nonVerifiedEmailAddress;

    public int getUserId() {
        return userId;
    }

    /**
     * Set the id of the user this verification is about.
     *
     * @param userId Id of the corresponding user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    /**
     * Set id of this verification.
     *
     * @param id Id of this verification.
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getValidationRandom() {
        return validationRandom;
    }

    /**
     * Set the random string that is used to check if a validation link is
     * valid.
     *
     * @param validationRandom The validation random.
     */
    public void setValidationRandom(String validationRandom) {
        this.validationRandom = validationRandom;
    }

    public boolean isVerified() {
        return verified;
    }

    /**
     * Set the user to verified or unverified.
     *
     * @param verified Is the user verified.
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getTimestampValidationStarted() {
        return timestampValidationStarted;
    }

    /**
     * Set the timestamp that states when the validation was started.
     *
     * @param timestampValidationStarted When was the validation started.
     */
    public void setTimestampValidationStarted(
            LocalDateTime timestampValidationStarted) {
        this.timestampValidationStarted = timestampValidationStarted;
    }

    public String getNonVerifiedEmailAddress() {
        return nonVerifiedEmailAddress;
    }

    /**
     * Set an email address that will be verified by this verification.
     *
     * @param nonVerifiedEmailAddress Unverified email address.
     */
    public void setNonVerifiedEmailAddress(String nonVerifiedEmailAddress) {
        this.nonVerifiedEmailAddress = nonVerifiedEmailAddress;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public Verification clone() {
        try {
            Verification clone = (Verification) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

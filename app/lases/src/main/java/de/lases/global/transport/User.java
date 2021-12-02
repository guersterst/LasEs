package de.lases.global.transport;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a user.
 */
public class User implements Cloneable {

    private int verificationId;

    private Integer id;

    private List<Privilege> privileges;

    private String title;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private LocalDate dateOfBirth;

    private String employer;

    private String passwordNotHashed;

    private String passwordHashed;

    private String passwordSalt;

    private boolean isRegistered;

    private int numberOfSubmissions;

    /**
     * Return if the user is an admin.
     *
     * @return Is the user an admin.
     */
    public boolean isAdmin() { return false; }

    /**
     * Add or remove admin privileges to the user.
     *
     * @param isAdmin Should teh use be an admin.
     */
    public void setAdmin(boolean isAdmin) { }

    public int getVerificationId() {
        return verificationId;
    }

    /**
     * Set the id of the verification belonging to this user.
     *
     * @param verificationId The id of the verification.
     */
    public void setVerificationId(int verificationId) {
        this.verificationId = verificationId;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Set the id of this user.
     * Integer is used instead of int in order to be able to determine if the property is set.
     *
     * @param id The user id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Set the academic title of this user.
     *
     * @param title The title of the user.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of this user.
     *
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of this user.
     *
     * @param lastName The last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address of this user.
     *
     * @param emailAddress The email addrss.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set the date of birth for this user.
     *
     * @param dateOfBirth The date of birth.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmployer() {
        return employer;
    }

    /**
     * Set the free text containing th employer of this user.
     *
     * @param employer The employer.
     */
    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getPasswordNotHashed() {
        return passwordNotHashed;
    }

    /**
     * Set the unhashed password for this user.
     *
     * @param passwordNotHashed The user's unhashed password.
     */
    public void setPasswordNotHashed(String passwordNotHashed) {
        this.passwordNotHashed = passwordNotHashed;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    /**
     * Set the hashed password for this user.
     *
     * @param passwordHashed The user's hashed password.
     */
    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Set the salt used to hash this user's password.
     *
     * @param passwordSalt The password salt.
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    /**
     * Decide if the user is registered or not.
     *
     * @param registered Is the user not registered?
     */
    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * Set the list of privileges. Each privilege has a dedicated getter/setter
     * for convenience purposes.
     *
     * @param privileges The list of privileges.
     */
    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * Check equality by comparing ids.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this user?
     */
    @Override
    public boolean equals(Object object) {
        return false;
    }

    /**
     * Hash the id of the user.
     *
     * @return The hashed id of this user.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    public int getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    /**
     * Set the number of the user's own submissions.
     *
     * @param numberOfSubmissions How many submissions did the user submit.
     */
    public void setNumberOfSubmissions(int numberOfSubmissions) {
        this.numberOfSubmissions = numberOfSubmissions;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public User clone() {
        try {
            User clone = (User) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

package de.lases.global.transport;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a user.
 */
public class User {

    //private Verification verification;
    private int verificationId;

    //private List<Review> reviews;

    //private List<ScienceField> scienceFields;

    //private List<ScientificForum> edits;

    //private List<Submission> edited;

    //private List<Submission> coAuthored;

    //private List<Submission> authored;

    //private List<Submission> reviewed;

    private int id;

    private List<Privilege> priveleges;

    private String title;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private LocalDate dateOfBirth;

    private String employer;

    private String passwordNotHashed;

    private String passwordHashed;

    private String passwordSalt;

    private boolean isNotRegistered;

    public boolean isEditor() { return false; }

    public void setEditor(boolean isEditor) { }

    public boolean isReviewer() { return false; }

    public void setReviewer(boolean reviewer) { }

    public boolean isAdmin() { return false; }

    public void setAdmin() { }

    public int getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(int verificationId) {
        this.verificationId = verificationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getPasswordNotHashed() {
        return passwordNotHashed;
    }

    public void setPasswordNotHashed(String passwordNotHashed) {
        this.passwordNotHashed = passwordNotHashed;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public boolean isNotRegistered() {
        return isNotRegistered;
    }

    public void setNotRegistered(boolean notRegistered) {
        isNotRegistered = notRegistered;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        User user = (User) object;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}

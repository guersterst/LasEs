package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

public class UserRepository {

    // get, add, change, remove by id
    public static User get(Submission submission, Transaction transaction) throws InvalidFieldsException {
        return null;
    }

    public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException {
    }

    public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    // get list for
    public static List<User> getList() {
        return null;
    }

    // specific adders for n to m
    public static void addScienceField() {
    }

    public static void removeScienceField() {
    }

    public static boolean emailExists(String emailAddress, Transaction transaction) {
        return false;
    }

    public static boolean isVerified() {
        return false;
    }

    public static File getAvatar() throws IllegalArgumentException {
        return null;
    }

    public static void setAvatar() throws IllegalArgumentException {
    }

    public static File getAvatarThumbnail() throws IllegalArgumentException {
        return null;
    }

    public static void setAvatarThumbnail() throws IllegalArgumentException {
    }

}

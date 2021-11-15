package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

public class ReviewRepository {

    // get, add, change, remove
    public static Review get(Review review, Transaction transaction) throws InvalidFieldsException {
        return null;
    }

    public static void add() {
    }

    public static void change() {
    }

    public static void remove() {
    }

    // ueberladen fuer user und paper
    public static List<Review> getList() {
        return null;
    }

    public static File getPDF() throws IllegalArgumentException {
        return null;
    }

    public static void setPDF() throws IllegalArgumentException {
    }

}

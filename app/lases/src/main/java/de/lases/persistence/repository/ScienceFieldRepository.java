package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

public class ScienceFieldRepository {

    // get, add, change, remove by id
    public static ScienceField get(Submission submission, Transaction transaction) throws InvalidFieldsException {
        return null;
    }

    public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException {
    }

    public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }


    public static List<ScienceField> getList(Transaction transaction) {
        return null;
    }

    public static boolean isScienceField(String name, Transaction transaction) {
        return false;
    }

}

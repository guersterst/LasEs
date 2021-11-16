package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

public class ScientificForumRepository {

    // get, add, change, remove by id
    public static ScientificForum get(Submission submission, Transaction transaction) throws InvalidFieldsException {
        return null;
    }

    public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException {
    }

    public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    // get lists
    public static List<ScientificForum> getList(ResultListParameters resultParams, Transaction transaction) throws InvalidQueryParamsException {
        return null;
    }

    // specific adders for n to m
    public static void addEditor() {
    }

    public static void addScienceField() {
    }

    public static void removeEditor() {
    }

    public static void removeScienceField() {
    }


}

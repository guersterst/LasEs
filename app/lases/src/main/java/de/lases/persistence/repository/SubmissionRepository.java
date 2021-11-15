package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

public class SubmissionRepository {

    // get, add, change, remove by id
    public static Submission get(Submission submission, Transaction transaction) throws InvalidFieldsException {
        return null;
    }

    public static void add(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    public static void change(Submission old, Submission edited, Transaction transaction) throws InvalidFieldsException {
    }

    public static void remove(Submission submission, Transaction transaction) throws InvalidFieldsException {
    }

    // ueberladen mit (Nutzer, Nutzerrolle) und/oder ScientificForum
    public static List<Submission> getList() {
        return null;
    }

    // specific adders for n to m
    public static void addCoAuthor() {
    }

    public static void addReviewer() {
    }

    public static void removeCoAuthor() {
    }

    public static void removeReviewer() {
    }

    public static int countSubmissions() {
        return 0;
    }

}

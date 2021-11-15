package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
public class ScientificForumService {

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public ScientificForum getForum() {
        return null;
    }

    public void updateForum(ScientificForum forum, ScientificForum newForum) {
    }

    public void addForum(ScientificForum forum) {
    }

    public void removeForum() {
    }

    public void addSubmission(Submission submission) {
    }

    public void removeSubmission(Submission submission) {
    }

    public void getSubmissions() {
    }

    public void addEditor(User user) {
    }

    public void removeEditor(User user) {
    }

    public void addScienceField() {
    }

    public List<User> getEditors() {
        return null;
    }

    public List<ScientificForum> getForums(ResultListParameters resultListParams) {
        return null;
    }
}

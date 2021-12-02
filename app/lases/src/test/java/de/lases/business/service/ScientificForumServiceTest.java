package de.lases.business.service;

import de.lases.global.transport.ScientificForum;
import de.lases.global.transport.User;
import de.lases.persistence.repository.ScientificForumRepository;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScientificForumServiceTest {

    static MockedStatic<ScientificForumRepository> forumRepoMock;
    static MockedStatic<UserRepository> userRepoMock;

    private static final Integer EXAMPLE_FORUM_ID = 1;
    private static final String EXAMPLE_FORUM_NAME = "Advances in Neural Information Processing Systems";
    private static final String EXAMPLE_FORUM_NAME_ALTERNATIVE =
            "Proceedings of the IEEE International Conference on Computer Vision";
    private static final String EXAMPLE_FORUM_DESCRIPTION = "Lorem ipsum dolor sit";

    private static final Integer EXAMPLE_USER_ID_EDITOR = 17;
    private static final Integer EXAMPLE_USER_ID_EDITOR_ALTERNATIVE = 18;
    private static final Integer EXAMPLE_USER_ID_NOT_AN_EDITOR = 16;

    @BeforeAll
    static void mockRepositories() {

        // Mock repository
        forumRepoMock = mockStatic(ScientificForumRepository.class);
        userRepoMock = mockStatic(UserRepository.class);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        forumRepoMock.close();
        userRepoMock.close();
    }

    @Test
    void testGet() {

        // Create the forum to be returned from a get request.
        ScientificForum forum = new ScientificForum();
        forum.setId(EXAMPLE_FORUM_ID);
        forum.setName(EXAMPLE_FORUM_NAME);

        // Mock get to return a forum with a name if the id is correct.
        forumRepoMock.when(() -> ScientificForumRepository
                .get(eq(forum), any(Transaction.class))).thenReturn(forum);

        // Create a forum with the correct id
        ScientificForum idForum = new ScientificForum();
        idForum.setId(EXAMPLE_FORUM_ID);

        // Request the forum with name from the forum with just an id.
        ScientificForumService forumService = new ScientificForumService();
        ScientificForum result = forumService.get(idForum);
        assertAll(
                () -> assertEquals(EXAMPLE_FORUM_ID, result.getId()),
                () -> assertEquals(EXAMPLE_FORUM_NAME, result.getName())
        );
    }

    @Test
    void testChange() {

        // A forum containing an old name.
        ScientificForum forum = new ScientificForum();
        forum.setId(EXAMPLE_FORUM_ID);
        forum.setName(EXAMPLE_FORUM_NAME);
        forum.setDescription(EXAMPLE_FORUM_DESCRIPTION);

        // Create a forum with a new name but the same id.
        ScientificForum newForum = new ScientificForum();
        newForum.setId(EXAMPLE_FORUM_ID);
        newForum.setName(EXAMPLE_FORUM_NAME_ALTERNATIVE);
        newForum.setDescription(EXAMPLE_FORUM_DESCRIPTION);

        // Change the forum to contain the new name.
        ScientificForumService forumService = new ScientificForumService();
        forumService.change(newForum);

        // Mock the get request to return the new forum.
        forumRepoMock.when(() -> ScientificForumRepository
                .get(eq(forum), any(Transaction.class))).thenReturn(newForum);

        // Request the changed forum.
        ScientificForum result = forumService.get(forum);

        assertAll(
                () -> assertEquals(EXAMPLE_FORUM_ID, result.getId()),
                () -> assertEquals(EXAMPLE_FORUM_NAME_ALTERNATIVE, result.getName()),
                () -> assertEquals(EXAMPLE_FORUM_DESCRIPTION, result.getDescription())
        );
    }

    @Test
    void testGetEditors() {

        // Create two editors of a forum and one editor of a different forum and the respective forums.
        User editor = new User();
        editor.setId(EXAMPLE_USER_ID_EDITOR);
        User anotherEditor = new User();
        anotherEditor.setId(EXAMPLE_USER_ID_EDITOR_ALTERNATIVE);
        User notAnEditor = new User();
        notAnEditor.setId(EXAMPLE_USER_ID_NOT_AN_EDITOR);

        ScientificForum forum = new ScientificForum();
        forum.setId(EXAMPLE_FORUM_ID);

        ScientificForum anotherForum = new ScientificForum();
        anotherForum.setId(EXAMPLE_FORUM_ID + 1);

        // Add the editors to their forums.
        ScientificForumService forumService = new ScientificForumService();
        forumService.addEditor(editor, forum);
        forumService.addEditor(anotherEditor, forum);
        forumService.addEditor(notAnEditor, anotherForum);

        // Mock the underlying repository function.
        List<User> tempEditorsList = Arrays.asList(anotherEditor, editor);
        userRepoMock.when(() -> UserRepository.getList(any(Transaction.class), eq(forum))).thenReturn(tempEditorsList);


        List<User> editors = forumService.getEditors(forum);
        assertAll(
                // Assert that the user editor is contained somewhere within
                // the list of editors
                () -> assertTrue(() -> {
                    for (User user : editors) {
                        if (user.getId().equals(editor.getId())) {
                            return true;
                        }
                    }
                    return false;
                }),
                // Assert that the user anotherEditor is contained somewhere within
                // the list of editors
                () -> assertTrue(() -> {
                    for (User user : editors) {
                        if (user.getId().equals(anotherEditor.getId())) {
                            return true;
                        }
                    }
                    return false;
                }),

                // Assert that the user notAnEditor is not contained somewhere within
                // the list of editors
                () -> assertTrue(() -> {
                    for (User user : editors) {
                        if (user.getId().equals(notAnEditor.getId())) {
                            return false;
                        }
                    }
                    return true;
                })
        );
    }
}

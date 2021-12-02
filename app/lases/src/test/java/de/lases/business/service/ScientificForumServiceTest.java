package de.lases.business.service;

import de.lases.global.transport.ScientificForum;
import de.lases.persistence.repository.ScientificForumRepository;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScientificForumServiceTest {

    static MockedStatic<ScientificForumRepository> forumRepoMock;

    private static final Integer EXAMPLE_FORUM_ID = 1;
    private static final String EXAMPLE_FORUM_NAME = "Advances in Neural Information Processing Systems";
    private static final String EXAMPLE_FORUM_NAME_ALTERNATIVE =
            "Proceedings of the IEEE International Conference on Computer Vision";
    private static final String EXAMPLE_FORUM_DESCRIPTION = "Lorem ipsum dolor sit";

    @BeforeAll
    static void mockRepositories() {

        // Mock repository
        forumRepoMock = mockStatic(ScientificForumRepository.class);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        forumRepoMock.close();
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


        // Add the forum for a complete procedure. The result of this will not be verified.
        ScientificForumService forumService = new ScientificForumService();
        forumService.add(forum, null, null);

        // Create a forum with a new name but the same id.
        ScientificForum newForum = new ScientificForum();
        newForum.setId(EXAMPLE_FORUM_ID);
        newForum.setName(EXAMPLE_FORUM_NAME_ALTERNATIVE);
        newForum.setDescription(EXAMPLE_FORUM_DESCRIPTION);

        // Change the forum to contain the new name.
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
    void testGetAllForums() {



        ScientificForumService forumService = new ScientificForumService();
        assertAll(
                () -> assertEquals()
        );
    }
}

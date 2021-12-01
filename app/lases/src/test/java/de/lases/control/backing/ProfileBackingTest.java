package de.lases.control.backing;

import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileBackingTest {

    @Mock
    SessionInformation currentSession;

    @InjectMocks
    ProfileBacking profileBackingOwner = new ProfileBacking();


    @Test
    void testHasOwnerEditRights() {
        User profileOwner = new User();
        profileOwner.setId(123456);
        profileBackingOwner.setUser(profileOwner);

        when(currentSession.getUser()).thenReturn(profileOwner);

        assertTrue(profileBackingOwner.hasViewerEditRights());
    }

    @Test
    void testHasAdminEditRights() {
        User profileOwner = new User();
        profileOwner.setId(123456);
        profileBackingOwner.setUser(profileOwner);

        User admin = new User();
        admin.setId(1);
        admin.setAdmin(true);

        when(currentSession.getUser()).thenReturn(admin);

        assertTrue(profileBackingOwner.hasViewerEditRights());
    }

    @Test
    void testHasEditorEditRights() {
        User profileOwner = new User();
        profileOwner.setId(123456);
        profileBackingOwner.setUser(profileOwner);

        User editor = new User();
        editor.setId(2);

        when(currentSession.getUser()).thenReturn(editor);

        assertFalse(profileBackingOwner.hasViewerEditRights());
    }
}

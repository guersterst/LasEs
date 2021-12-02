package de.lases.business.service;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.ScienceField;
import de.lases.global.transport.User;
import de.lases.persistence.repository.ScienceFieldRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ScienceFieldServiceTest {

    private static final String NAME_MATH_SF = "Math";

    static MockedStatic<ScienceFieldRepository> mockedStatic;

    @Test
    void testAddScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_MATH_SF);
        scienceFieldService.add(mathField);

        assertEquals(mathField, scienceFieldService.get(NAME_MATH_SF));
    }

    @Test
    void testRemoveScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_MATH_SF);
        scienceFieldService.add(mathField);

        assertEquals(mathField, scienceFieldService.get(NAME_MATH_SF));

        scienceFieldService.remove(mathField);

        assertNull(scienceFieldService.get(NAME_MATH_SF));
    }

    @Test
    void testGetScienceFieldsOfUser() {
        User user = new User();
        user.setId(1);

        ScienceField mathField = new ScienceField();
        mathField.setName("Math");
        ScienceField csField = new ScienceField();
        csField.setName("Computer Science");

        ResultListParameters params = new ResultListParameters();

        List<ScienceField> userScienceFields = Arrays.asList(mathField, csField);

        mockedStatic.when(() -> ScienceFieldRepository.getList(eq(user), any(), eq(params))).thenReturn(userScienceFields);

        assertEquals(userScienceFields, ScienceFieldService.getList(user, params));
    }
}

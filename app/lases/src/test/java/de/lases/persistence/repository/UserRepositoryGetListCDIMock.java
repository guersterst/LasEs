package de.lases.persistence.repository;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.SortOrder;
import de.lases.global.transport.User;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryGetListCDIMock {

    private static Transaction transaction;

    @BeforeAll
    static void init() {
        ConnectionPool.init();
        transaction = new Transaction();
    }

    @AfterAll
    static void destruct() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    @Test
    void testGetUserListBasicParams() throws DataNotCompleteException {
        ResultListParameters params = new ResultListParameters();
        params.setPageNo(1);
        params.setSortColumn("firstname");
        params.setSortOrder(SortOrder.ASCENDING);

        CDI cdiMock = mock(CDI.class);
        ConfigReader mockConfig = mock(ConfigReader.class);
        Instance mockInstanceConfig = mock(Instance.class);

        // Return default pagination size
        when(mockConfig.getProperty("MAX_PAGINATION_LENGTH")).thenReturn("25");

        // Mock CDI
        MockedStatic<CDI> cdiMockedStatic = mockStatic(CDI.class);
        cdiMockedStatic.when(CDI::current).thenReturn(cdiMock);

        when(cdiMock.select(ConfigReader.class)).thenReturn(mockInstanceConfig);
        when(mockInstanceConfig.get()).thenReturn(mockConfig);

        List<User> userList = UserRepository.getList(transaction, params);

        assertTrue(userList.size() > 4);
    }
}

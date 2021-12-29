package de.lases.control.backing;

import de.lases.control.internal.Pagination;
import de.lases.global.transport.User;

public interface UserPaginationBacking {

    Pagination<User> getUserPagination();
}

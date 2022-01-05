package de.lases.control.backing;

import de.lases.business.util.EmailUtil;
import de.lases.control.internal.Pagination;
import de.lases.global.transport.User;

public interface UserPaginationBacking {

    Pagination<User> getUserPagination();

    default String[] getPrivileges() {
        return new String[]{"none", "editor", "admin", "reviewer"};
    }

    default String generateMailTo(String recipient) {
        return EmailUtil.generateMailToLink(new String[]{recipient}, null, null, null);
    }

}

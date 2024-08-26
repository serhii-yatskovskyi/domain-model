package org.bayaweaver.domainmodel.model;

import org.bayaweaver.domainmodel.domain.model.AuthorizationException;

public class AuthExample {

    public void example() throws AuthorizationException {
        User u = new User();
        Account a = new Account(u);
        u.take(a).modify();
        new Admin().take(a).close();
    }
}

package org.bayaweaver.domainmodel.model;

import org.bayaweaver.domainmodel.domain.model.Actor;
import org.bayaweaver.domainmodel.domain.model.AuthorizationException;
import org.bayaweaver.domainmodel.domain.model.IllegalRoleException;
import org.bayaweaver.domainmodel.domain.model.Role;

public class User implements Actor {

    public AccountUserPermissions take(Account a) throws AuthorizationException {
        if (a.creator().equals(this)) {
            return a;
        }
        throw new AuthorizationException();
    }

    @Override
    public <T extends Role> T inRoleOf(Class<T> role) throws IllegalRoleException {
        return null;
    }
}

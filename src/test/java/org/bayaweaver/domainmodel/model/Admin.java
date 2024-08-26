package org.bayaweaver.domainmodel.model;

import org.bayaweaver.domainmodel.domain.model.Actor;
import org.bayaweaver.domainmodel.domain.model.IllegalRoleException;
import org.bayaweaver.domainmodel.domain.model.Role;

public class Admin implements Actor {

    AccountAdminPermissions take(Account a) {
        return a;
    }

    @Override
    public <T extends Role> T inRoleOf(Class<T> role) throws IllegalRoleException {
        return null;
    }
}

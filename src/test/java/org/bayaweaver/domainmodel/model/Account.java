package org.bayaweaver.domainmodel.model;

import org.bayaweaver.domainmodel.domain.event.Events;
import org.bayaweaver.domainmodel.domain.model.DefinedIdentifier;
import org.bayaweaver.domainmodel.domain.model.Entity;

public class Account extends Entity<Account.Id> implements AccountUserPermissions, AccountAdminPermissions {
    private User creator;
    public Account(User creator) {
        super(null, null);
        this.creator = creator;
    }

    User creator() {
        return creator;
    }

    @Override
    public Events close() {
        return null;
    }

    @Override
    public Events modify() {
        return null;
    }

    class State extends Entity.State {}

    class Id extends DefinedIdentifier {
        public Id(String value) {
            super(value);
        }
    }
}

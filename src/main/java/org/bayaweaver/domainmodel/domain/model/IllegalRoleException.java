package org.bayaweaver.domainmodel.domain.model;

public class IllegalRoleException extends DomainException {

    public IllegalRoleException(Class<?> role) {
        super("Actor can not take role of '" + role.getSimpleName() + "'.");
    }
}

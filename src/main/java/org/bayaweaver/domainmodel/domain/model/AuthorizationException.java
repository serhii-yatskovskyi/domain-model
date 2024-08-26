package org.bayaweaver.domainmodel.domain.model;

public class AuthorizationException extends DomainException {

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException() {
        this("Access denied.");
    }
}

package org.bayaweaver.domainmodel.domain.model;

public abstract class DomainException extends Exception {

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(String message) {
        this(message, null);
    }
}

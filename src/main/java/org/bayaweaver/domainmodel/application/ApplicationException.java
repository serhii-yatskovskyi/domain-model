package org.bayaweaver.domainmodel.application;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException() {
        super();
    }
}

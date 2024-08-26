package org.bayaweaver.domainmodel.domain.event;

import org.bayaweaver.domainmodel.domain.model.AuthorizationException;

public class MismatchedEntityEventException extends AuthorizationException {

    public MismatchedEntityEventException() {
        super("The event aimed to another entity can not be processed by a current one.");
    }
}

package org.bayaweaver.domainmodel.application;

import org.bayaweaver.domainmodel.domain.event.DomainEvent;

public abstract class DomainEventSerializer<T extends DomainEvent> {
    private final Class<T> domainEventType;

    public DomainEventSerializer(Class<T> domainEventType) {
        this.domainEventType = domainEventType;
    }

    public abstract Object serialize(T domainEvent);

    public Class<T> domainEventType() {
        return domainEventType;
    }
}

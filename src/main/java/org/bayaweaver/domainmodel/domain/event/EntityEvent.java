package org.bayaweaver.domainmodel.domain.event;

import org.bayaweaver.domainmodel.domain.model.Identifier;

public abstract class EntityEvent<T extends Identifier> implements DomainEvent {
    private final T source;

    public EntityEvent(T source) {
        this.source = source;
    }

    public T source() {
        return source;
    }
}

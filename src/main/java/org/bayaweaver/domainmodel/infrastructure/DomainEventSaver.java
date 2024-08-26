package org.bayaweaver.domainmodel.infrastructure;

import org.bayaweaver.domainmodel.domain.event.DomainEvent;

public interface DomainEventSaver {

    void save(DomainEvent domainEvent);
}

package org.bayaweaver.domainmodel.infrastructure;

import org.bayaweaver.domainmodel.domain.event.DomainEvent;

import java.util.stream.Stream;

public interface DomainEventSaverRegistry {

    void register(DomainEventSaver saver);
    Stream<DomainEventSaver> forEvent(DomainEvent event);
}

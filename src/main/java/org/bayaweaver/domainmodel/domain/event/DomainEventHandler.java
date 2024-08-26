package org.bayaweaver.domainmodel.domain.event;

@FunctionalInterface
public interface DomainEventHandler {

    void handle(DomainEvent e) throws Exception;
}

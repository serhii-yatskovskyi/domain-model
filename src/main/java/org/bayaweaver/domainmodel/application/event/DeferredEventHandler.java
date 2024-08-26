package org.bayaweaver.domainmodel.application.event;

import org.bayaweaver.domainmodel.domain.event.DomainEvent;
import org.bayaweaver.domainmodel.domain.event.DomainEventHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class DeferredEventHandler implements DomainEventHandler {
    private final List<DomainEvent> eventQueue;

    public DeferredEventHandler() {
        this.eventQueue = new ArrayList<>(5);
    }

    protected abstract void doHandle(DomainEvent event);

    @Override
    public final void handle(DomainEvent event) {
        eventQueue.add(event);
        onEventDeferred(event);
    }

    public synchronized final void handleDeferredEvents() {
        try {
            for (DomainEvent event : eventQueue) {
                doHandle(event);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            eventQueue.clear();
        }
    }

    protected void onEventDeferred(DomainEvent event) {}
}

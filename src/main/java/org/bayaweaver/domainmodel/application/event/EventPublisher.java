package org.bayaweaver.domainmodel.application.event;

import org.bayaweaver.domainmodel.domain.event.DomainEvent;
import org.bayaweaver.domainmodel.domain.event.DomainEventHandler;
import org.bayaweaver.domainmodel.domain.event.Events;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventPublisher {
    private final Collection<DomainEventHandler> subscribers;
    private final Queue<DomainEvent> eventQueue;
    private boolean idle;

    public EventPublisher() {
        this.subscribers = Collections.newSetFromMap(new IdentityHashMap<>());
        this.eventQueue = new ConcurrentLinkedQueue<>();
        this.idle = true;
    }

    public void publish(Events events) {
        for (DomainEvent event : events.close()) {
            eventQueue.offer(event);
        }
        synchronized (this) {
            if (!idle) {
                return;
            }
            idle = false;
        }
        try {
            while (!eventQueue.isEmpty()) {
                DomainEvent event = eventQueue.poll();
                for (DomainEventHandler handler : subscribers) {
                    handler.handle(event);
                }
            }
        } catch (Throwable e) {
            eventQueue.clear();
            throw new RuntimeException(e);
        } finally {
            idle = true;
        }
    }

    public EventPublisher subscribe(DomainEventHandler handler) {
        if (!idle) {
            throw new IllegalStateException("An Event Handler cannot be added during the ongoing publication process.");
        }
        subscribers.add(handler);
        return this;
    }

    public EventPublisher unsubscribe(DomainEventHandler handler) {
        if (!idle) {
            throw new IllegalStateException("An Event Handler cannot be removed during the ongoing publication process.");
        }
        subscribers.remove(handler);
        return this;
    }
}

package org.bayaweaver.domainmodel.event;

import org.bayaweaver.domainmodel.application.event.EventPublisher;
import org.bayaweaver.domainmodel.domain.event.DomainEventHandler;
import org.bayaweaver.domainmodel.domain.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventSubscriptionTest {
    private final EventPublisher eventPublisher = new EventPublisher();
    private List<String> handlerResponses;
    private final DomainEventHandler H1 = (event) -> handlerResponses.add("H1." + event.getClass().getSimpleName());
    private final DomainEventHandler H2 = (event) -> handlerResponses.add("H2." + event.getClass().getSimpleName());

    @BeforeEach
    public void init() {
        handlerResponses = new ArrayList<>();
    }

    @Test
    public void singleEventHandler() {
        eventPublisher.subscribe(H1);
        eventPublisher.publish(Events.with(new EventA()));
        assertEquals(Arrays.asList("H1.EventA"), handlerResponses);
    }

    @Test
    public void allEventHandler() {
        eventPublisher.subscribe(H2);
        eventPublisher.publish(Events
                .with(new EventA())
                .add(new EventB())
                .add(new EventC()));
        assertEquals(Arrays.asList("H2.EventA", "H2.EventB", "H2.EventC"), handlerResponses);
    }

    @Test
    public void twoEventHandlers() {
        eventPublisher
                .subscribe(H1)
                .subscribe(H2);
        eventPublisher.publish(Events.with(new EventA()));
        handlerResponses.sort(Comparator.naturalOrder());
        assertEquals(Arrays.asList("H1.EventA", "H2.EventA"), handlerResponses);
    }

    @Test
    public void duplicatedSubscription() {
        DomainEventHandler handler1 = (event) -> handlerResponses.add("h1");
        DomainEventHandler handler2 = (event) -> handlerResponses.add("h2");
        eventPublisher
                .subscribe(handler1)
                .subscribe(handler1)
                .subscribe(handler2);
        eventPublisher.publish(Events.with(new EventA()));
        handlerResponses.sort(Comparator.naturalOrder());
        assertEquals(Arrays.asList("h1", "h2"), handlerResponses);
    }
}

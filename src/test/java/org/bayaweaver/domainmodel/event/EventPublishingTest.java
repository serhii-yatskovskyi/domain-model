package org.bayaweaver.domainmodel.event;

import org.bayaweaver.domainmodel.application.event.EventPublisher;
import org.bayaweaver.domainmodel.domain.event.Events;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventPublishingTest {
    private final EventPublisher eventPublisher = new EventPublisher();

    @Test
    public void rightOrder() {
        List<Class<?>> handledEvents = new ArrayList<>();
        eventPublisher
                .subscribe(event -> {
                    handledEvents.add(event.getClass());
                    if (event.getClass() == EventA.class) {
                        eventPublisher.publish(Events.with(new EventB()));
                    }
                })
                .subscribe(event -> {
                    handledEvents.add(event.getClass());
                });
        eventPublisher.publish(Events.with(new EventA()));
        assertEquals(Arrays.asList(EventA.class, EventA.class, EventB.class, EventB.class), handledEvents);
    }

    @Test
    public void terminatesPublicationOnException() {
        List<Class<?>> handledEvents = new ArrayList<>();
        eventPublisher
                .subscribe(event -> {
                    handledEvents.add(event.getClass());
                    if (event.getClass() == EventB.class) {
                        throw new Exception();
                    }
                });
        assertThrows(RuntimeException.class, () -> {
            eventPublisher.publish(Events
                    .with(new EventA())
                    .add(new EventB())
                    .add(new EventC()));
        });
        assertEquals(Arrays.asList(EventA.class, EventB.class), handledEvents);
    }
}

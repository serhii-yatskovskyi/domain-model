package org.bayaweaver.domainmodel.event;

import org.bayaweaver.domainmodel.application.event.DeferredEventHandler;
import org.bayaweaver.domainmodel.application.event.EventPublisher;
import org.bayaweaver.domainmodel.domain.event.DomainEvent;
import org.bayaweaver.domainmodel.domain.event.Events;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeferredEventHandlerTest {

    @Test
    public void simpleTest() {
        List<String> handledEvents = new ArrayList<>();
        DeferredEventHandler handler = new DeferredEventHandler() {
            @Override
            protected void doHandle(DomainEvent event) {
                handledEvents.add(event.getClass().getSimpleName());
            }
        };
        EventPublisher eventPublisher = new EventPublisher();
        eventPublisher.subscribe(handler);
        eventPublisher.publish(Events.with(new EventA()));
        assertTrue(handledEvents.isEmpty());
        assertEquals(Collections.emptyList(), handledEvents);
        handler.handleDeferredEvents();
        assertEquals(Collections.singletonList(EventA.class.getSimpleName()), handledEvents);
    }
}

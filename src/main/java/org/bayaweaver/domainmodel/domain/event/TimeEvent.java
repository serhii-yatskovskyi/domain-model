package org.bayaweaver.domainmodel.domain.event;

import java.time.Instant;

public abstract class TimeEvent implements DomainEvent {
    private final Instant time;

    public TimeEvent(Instant time) {
        this.time = time;
    }

    public final Instant time() {
        return time;
    }
}

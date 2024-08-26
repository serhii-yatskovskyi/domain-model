package org.bayaweaver.domainmodel.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Events {
    private final List<DomainEvent> events;
    private boolean closed;

    private Events() {
        this.events = new ArrayList<>();
    }

    public static Events with(DomainEvent... events) {
        Events stream = new Events();
        for (DomainEvent event : events) {
            stream.events.add(event);
        }
        return stream;
    }

    public static Events none() {
        return with();
    }

    public Events add(DomainEvent event) {
        verifyNotClosed();
        this.events.add(event);
        return this;
    }

    public void mergeInto(Events mainStream) {
        Iterable<DomainEvent> events = close();
        for (DomainEvent event : events) {
            mainStream.events.add(event);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> boolean replace(Class<T> eventClass, Function<T, ? extends DomainEvent> replacer) {
        ListIterator<DomainEvent> it = this.events.listIterator();
        boolean replaced = false;
        while (it.hasNext()) {
            DomainEvent event = it.next();
            if (event.getClass() == eventClass) {
                it.set(replacer.apply((T) event));
                replaced = true;
            }
        }
        return replaced;
    }
/*
    public boolean override(
            Predicate<DomainEvent> eventCriteria,
            Function<DomainEvent, DomainEvent> replacer) {

        ListIterator<DomainEvent> it = this.events.listIterator();
        boolean overriden = false;
        while (it.hasNext()) {
            DomainEvent event = it.next();
            if (eventCriteria.test(event)) {
                it.set(replacer.apply(event));
                overriden = true;
            }
        }
        return overriden;
    }
*/
    public Iterable<DomainEvent> close() {
        verifyNotClosed();
        closed = true;
        return events;
    }

    public Stream<DomainEvent> stream() {
        return events.stream();
    }

    private void verifyNotClosed() {
        if (closed) {
            throw new IllegalStateException("The Event Stream has already been closed.");
        }
    }
}

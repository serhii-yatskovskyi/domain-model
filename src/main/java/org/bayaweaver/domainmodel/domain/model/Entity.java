package org.bayaweaver.domainmodel.domain.model;

import org.bayaweaver.domainmodel.domain.event.Events;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public abstract class Entity<T extends DefinedIdentifier> {
    private final T id;
    private final State state;
    private int invocations;

    public Entity(T id, State state) {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
        this.id = id;
        this.state = state;
    }

    public Entity(T id) {
        this(id, null);
    }

    public T id() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Entity<?> other = (Entity<?>) o;
        return id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    public static abstract class State {
        final Stack<Queue<Runnable>> changes;

        public State() {
            this.changes = new Stack<>();
        }

        protected final void queueChange(Runnable change) {
            changes.peek().add(change);
        }
    }

    @FunctionalInterface
    public interface Action {

        Events perform(State state) throws DomainException, AuthorizationException;
    }
} 

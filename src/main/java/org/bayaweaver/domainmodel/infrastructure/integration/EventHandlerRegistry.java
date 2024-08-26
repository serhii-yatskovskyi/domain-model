package org.bayaweaver.domainmodel.infrastructure.integration;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class EventHandlerRegistry {
    private final EventHandler defaultEventHandler;
    private final Map<Class<?>, EventHandler> handlers;

    public EventHandlerRegistry() {
        this(event -> {});
    }

    public EventHandlerRegistry(EventHandler defaultEventHandler) {
        this.handlers = new HashMap<>();
        this.defaultEventHandler = defaultEventHandler;
    }

    public <T extends Serializable> void register(
            Class<T> eventClass,
            EventHandlerAction<T> handlerAction,
            Consumer<T> onSuccess,
            BiConsumer<T, Exception> onFail) {

        register(eventClass, handlerAction, onSuccess, Exception.class, onFail);
    }

    public <T extends Serializable, E extends Throwable> void register(
            Class<T> eventClass,
            EventHandlerAction<T> handlerAction,
            Consumer<T> onSuccess,
            Class<E> exceptionClass,
            BiConsumer<T, E> onFail) {

        for (Class<?> existingMessageClass : handlers.keySet()) {
            if (existingMessageClass.isAssignableFrom(eventClass)) {
                throw new IllegalArgumentException("The handler for the " + eventClass + " has already been registered.");
            }
        }
        Map<Class<E>, BiConsumer<T, E>> onFailActions = Collections.singletonMap(exceptionClass, onFail);
        handlers.put(eventClass, new SimpleMessageHandler<>(eventClass, handlerAction, onSuccess, onFailActions));
    }

    protected EventHandler forMessage(Class<? extends Serializable> eventClass) {
        if (eventClass == null) {
            return defaultEventHandler;
        }
        for (Map.Entry<Class<?>, EventHandler> entry : handlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                return entry.getValue();
            }
        }
        return defaultEventHandler;
    }

    public interface EventHandlerAction<T> {

        void perform(T event) throws Exception;
    }

    private static class SimpleMessageHandler<T, E extends Throwable> implements EventHandler {
        private final Class<T> eventClass;
        private final EventHandlerAction<T> handlerAction;
        private final Consumer<T> onSuccess;
        private final Map<Class<E>, BiConsumer<T, E>> onFail;

        SimpleMessageHandler(
                Class<T> eventClass,
                EventHandlerAction<T> handlerAction,
                Consumer<T> onSuccess,
                Map<Class<E>, BiConsumer<T, E>> onFail) {

            this.eventClass = eventClass;
            this.handlerAction = handlerAction;
            this.onSuccess = onSuccess;
            this.onFail = onFail;
        }

        @Override
        public void handleEvent(Serializable event) {
            if (eventClass.isInstance(event)) {
                T castMessage = (T) event;
                try {
                    handlerAction.perform(castMessage);
                    onSuccess.accept(castMessage);
                } catch (Throwable e) {
                    BiConsumer<T, E> action = onFail.get(e.getClass());
                    if (action == null) {
                        throw new RuntimeException(e);
                    }
                    action.accept(castMessage, (E) e);
                }
            }
        }
    }
}

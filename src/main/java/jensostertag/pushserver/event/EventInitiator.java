package jensostertag.pushserver.event;

import jensostertag.pushserver.util.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventInitiator {
    private static final HashMap<Class<?>, List<Object[]>> LISTENERS = new HashMap<>();

    public static int registerListener(Listener listener) {
        new Logger("EventInitiator").debug("Setting up listeners for methods in class " + listener.getClass().getName());

        int registeredEvents = 0;
        for(Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }

            if(method.getParameterCount() != 1) {
                continue;
            }

            Class<?> eventClass = method.getParameterTypes()[0];

            List<Object[]> listeners = new ArrayList<>();
            if(EventInitiator.LISTENERS.containsKey(eventClass)) {
                listeners = EventInitiator.LISTENERS.get(eventClass);
            }

            listeners.add(new Object[]{listener, method});
            EventInitiator.LISTENERS.put(eventClass, listeners);

            registeredEvents++;

            new Logger("EventInitiator").debug("Set up " + eventClass.getName() + " listener with method " + method.getName() + " in class " + listener.getClass().getName());
        }

        new Logger("EventInitiator").debug("Listener setup for class " + listener.getClass().getName() + " completed");
        return registeredEvents;
    }

    public static void trigger(Event event) {
        if(!EventInitiator.LISTENERS.containsKey(event.getClass())) {
            return;
        }

        for(Object[] listener : EventInitiator.LISTENERS.get(event.getClass())) {
            Listener listenerObject = (Listener) listener[0];
            Method method = (Method) listener[1];

            try {
                method.invoke(listenerObject, event);
            } catch(Exception e) {
                new Logger("EventInitiator").error("Failed to trigger event " + event.getClass().getName() + " for method " + method.getName() + " in class " + method.getDeclaringClass().getName() + ": " + e.getMessage() + " (" + e.getClass().getName() + ")");
            }
        }
    }
}

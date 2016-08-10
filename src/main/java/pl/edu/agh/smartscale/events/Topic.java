package pl.edu.agh.smartscale.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {

    private List<MetricsListener> listeners = new CopyOnWriteArrayList<>();

    public void sendEvent(Event event) {
        for (MetricsListener listener : listeners) {
            listener.omgEvent(event);
        }
    }

    public void registerListener(MetricsListener listener) {
        listeners.add(listener);
    }
}

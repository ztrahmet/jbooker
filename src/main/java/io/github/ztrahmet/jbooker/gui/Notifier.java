package io.github.ztrahmet.jbooker.gui;

import java.util.ArrayList;
import java.util.List;

public class Notifier {
    private final List<PanelListener> listeners = new ArrayList<>();

    public void addListener(PanelListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (PanelListener listener : listeners) {
            listener.refreshData();
        }
    }
}

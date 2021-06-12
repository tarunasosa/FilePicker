package com.example.filepicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListenerList<L> {
    private List<L> listenerList = new ArrayList<L>();

    public interface FireHandler<L> {
        /**
         * @param listener
         * @throws IOException
         */
        void fireEvent(L listener) throws IOException;
    }

    public void add(L listener) {
        listenerList.add(listener);
    }

    public void fireEvent(FireHandler<L> fireHandler) throws IOException {
        List<L> copy = new ArrayList<>(listenerList);
        for (L l : copy) fireHandler.fireEvent(l);
    }

    public void remove(L listener) {
        listenerList.remove(listener);
    }

    public List<L> getListenerList() {
        return listenerList;
    }
}


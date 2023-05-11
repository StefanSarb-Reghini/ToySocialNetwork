package com.example.toysocialnetwork.observer;

import com.example.toysocialnetwork.utils.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}

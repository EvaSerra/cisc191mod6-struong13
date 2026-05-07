package edu.sdccd.cisc191;

import java.util.LinkedList;

public class GenericMatchQueue<T> {

    private final LinkedList<T> items = new LinkedList<>();

    public void enqueue(T item) {
        items.addLast(item); //add to back
    }

    public T dequeue() {
        if (items.isEmpty()) {
            throw new IllegalStateException("List can't be empty");
        }
        return items.removeFirst(); // remove from front
    }

    public T peek() {
        if (items.isEmpty()) {
            throw new IllegalStateException("List can't be empty");
        }
        return items.getFirst(); // look at front
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {

        return items.size();
    }
}

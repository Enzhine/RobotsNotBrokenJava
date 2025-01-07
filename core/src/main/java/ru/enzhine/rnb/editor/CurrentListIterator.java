package ru.enzhine.rnb.editor;

import java.util.ListIterator;

public class CurrentListIterator<T> {
    private final ListIterator<T> inner;
    private int virtualIndex;
    private T value;

    public CurrentListIterator(ListIterator<T> inner) {
        this.inner = inner;
        this.virtualIndex = 0;

        if (!inner.hasNext()) {
            throw new RuntimeException("List must not be empty");
        }
        value = inner.next();
        inner.previous();
    }

    public boolean hasPrevious() {
        return inner.hasPrevious();
    }

    public boolean hasNext() {
        inner.next();
        var hasNext = inner.hasNext();
        inner.previous();
        return hasNext;
    }

    public void previous() {
        value = inner.previous();
        virtualIndex -= 1;
    }

    public void next() {
        inner.next();
        value = inner.next();
        inner.previous();
        virtualIndex += 1;
    }

    public void add(T value) {
        inner.next();
        inner.add(value);
        this.value = value;
        inner.previous();
        virtualIndex += 1;
    }

    public void remove() {
        if (hasPrevious()) {
            inner.remove();
            value = inner.previous();
            virtualIndex -= 1;
        } else if (hasNext()) {
            inner.remove();
            value = inner.next();
            inner.previous();
        } else {
            throw new RuntimeException("List us about to become empty");
        }
    }

    public int index() {
        return virtualIndex;
    }

    public T get() {
        return value;
    }
}

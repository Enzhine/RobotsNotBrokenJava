package ru.enzhine.rnb.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class CurrentListIteratorTest {

    @Test
    void index__correct() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        var myIter = new CurrentListIterator<>(list.listIterator());

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());
        Assertions.assertFalse(myIter.hasPrevious());
        Assertions.assertTrue(myIter.hasNext());

        myIter.next();

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(2, myIter.get());
        Assertions.assertTrue(myIter.hasPrevious());
        Assertions.assertTrue(myIter.hasNext());

        myIter.next();

        Assertions.assertEquals(2, myIter.index());
        Assertions.assertEquals(3, myIter.get());
        Assertions.assertTrue(myIter.hasPrevious());
        Assertions.assertFalse(myIter.hasNext());

        myIter.previous();

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(2, myIter.get());
        Assertions.assertTrue(myIter.hasPrevious());
        Assertions.assertTrue(myIter.hasNext());

        myIter.previous();

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());
        Assertions.assertFalse(myIter.hasPrevious());
        Assertions.assertTrue(myIter.hasNext());
    }

    @Test
    void remove_0_correct() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        var myIter = new CurrentListIterator<>(list.listIterator());

        myIter.next();

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(2, myIter.get());

        myIter.remove();

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());

        myIter.next();

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(3, myIter.get());

        myIter.remove();

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());
    }

    @Test
    void remove_1_correct() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);

        var myIter = new CurrentListIterator<>(list.listIterator());

        myIter.remove();

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(2, myIter.get());
    }

    @Test
    void add__correct() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);

        var myIter = new CurrentListIterator<>(list.listIterator());

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());

        myIter.add(5);

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(5, myIter.get());

        myIter.next();

        Assertions.assertEquals(2, myIter.index());
        Assertions.assertEquals(2, myIter.get());

        myIter.previous();

        Assertions.assertEquals(1, myIter.index());
        Assertions.assertEquals(5, myIter.get());

        myIter.previous();

        Assertions.assertEquals(0, myIter.index());
        Assertions.assertEquals(1, myIter.get());
    }
}

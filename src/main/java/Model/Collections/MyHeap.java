package Model.Collections;

import Exceptions.InterpreterException;
import Model.Value.IValue;

import java.util.HashMap;
import java.util.Set;

public class MyHeap implements IHeap {
    HashMap<Integer, IValue> heap;
    Integer freeLocationValue;

    public int newValue() {
        freeLocationValue += 1;
        while (freeLocationValue == 0 || heap.containsKey(freeLocationValue))
            freeLocationValue += 1;
        return freeLocationValue;
    }

    public MyHeap() {
        this.heap = new HashMap<>();
        freeLocationValue = 1;
    }

    @Override
    public int getFreeValue() {
        return freeLocationValue;
    }

    @Override
    public HashMap<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(HashMap<Integer, IValue> newMap) {
        this.heap = newMap;
    }

    @Override
    public int add(IValue value) {
        heap.put(freeLocationValue, value);
        Integer toReturn = freeLocationValue;
        freeLocationValue = newValue();
        return toReturn;
    }

    @Override
    public void update(Integer position, IValue value) throws InterpreterException {
        if (!heap.containsKey(position))
            throw new InterpreterException(String.format("%d is not present in the heap", position));
        heap.put(position, value);
    }

    @Override
    public IValue get(Integer position) throws InterpreterException {
        if (!heap.containsKey(position))
            throw new InterpreterException(String.format("%d is not present in the heap", position));
        return heap.get(position);
    }

    @Override
    public boolean containsKey(Integer position) {
        return this.heap.containsKey(position);
    }

    @Override
    public void remove(Integer key) throws InterpreterException {
        if (!containsKey(key))
            throw new InterpreterException(key + " is not defined.");
        freeLocationValue = key;
        this.heap.remove(key);
    }

    @Override
    public Set<Integer> keySet() {
        return heap.keySet();
    }
    @Override
    public String toString() {
        return this.heap.toString();
    }

}
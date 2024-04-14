package Model.Collections;


import Exceptions.InterpreterException;
import Model.Value.IValue;

import java.util.HashMap;
import java.util.Set;

public interface IHeap {
    int getFreeValue();
    HashMap<Integer, IValue> getContent();
    void setContent(HashMap<Integer, IValue> newMap);
    int add(IValue value);
    void update(Integer position, IValue value) throws InterpreterException;
    IValue get(Integer position) throws InterpreterException;
    boolean containsKey(Integer position);
    void remove(Integer key) throws InterpreterException;
    Set<Integer> keySet();
}

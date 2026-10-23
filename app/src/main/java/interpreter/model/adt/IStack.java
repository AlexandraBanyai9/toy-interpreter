package interpreter.model.adt;

import interpreter.exceptions.ADTException;

public interface IStack<T> {
    public void push(T v);
    public T pop() throws ADTException;
    public boolean isEmpty();
}

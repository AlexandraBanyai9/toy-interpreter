package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.List;

public interface IStack<T> {
    public void push(T v);
    public T pop() throws ADTException;
    public boolean isEmpty();

    List<T> toList();
}

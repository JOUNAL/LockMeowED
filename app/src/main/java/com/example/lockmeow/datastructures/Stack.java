package com.example.lockmeow.datastructures;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * Implementación de una Pila (Stack) - Estructura de datos lineal LIFO
 */
public class Stack<T> {
    private ArrayList<T> elements;
    
    public Stack() {
        elements = new ArrayList<>();
    }
    
    /**
     * Añade un elemento al tope de la pila
     */
    public void push(T item) {
        elements.add(item);
    }
    
    /**
     * Remueve y retorna el elemento del tope de la pila
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }
    
    /**
     * Retorna el elemento del tope sin removerlo
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.get(elements.size() - 1);
    }
    
    /**
     * Verifica si la pila está vacía
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    /**
     * Retorna el tamaño de la pila
     */
    public int size() {
        return elements.size();
    }
    
    /**
     * Limpia todos los elementos de la pila
     */
    public void clear() {
        elements.clear();
    }
    
    @Override
    public String toString() {
        return "Stack: " + elements.toString();
    }
}

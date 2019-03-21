package com.arman.queuetube.util;

public class Tuple<T, E> {

    private T left;
    private E right;

    public Tuple(T left, E right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public E getRight() {
        return right;
    }

}

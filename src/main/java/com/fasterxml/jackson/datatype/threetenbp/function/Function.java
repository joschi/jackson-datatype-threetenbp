package com.fasterxml.jackson.datatype.threetenbp.function;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * Very simple and stupid interface for representing functions f(T) &rarr; R.
 */
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);
}
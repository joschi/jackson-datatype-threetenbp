package com.fasterxml.jackson.datatype.threetenbp.function;

/**
 * Represents a function that accepts one argument and produces an {@code int} result.
 *
 * Very simple and stupid interface for representing functions f(T) &rarr; {@code int}.
 */
public interface ToIntFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    int applyAsInt(T value);
}
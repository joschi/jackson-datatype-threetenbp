package com.fasterxml.jackson.datatype.threetenbp.function;

/**
 * Represents a function that accepts one argument and produces a long result.
 * <p/>
 * Very simple and stupid interface for representing functions f(R) -> {@link Long}.
 *
 * @param <T> the type of the input to the function
 */
public interface ToLongFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    long applyAsLong(T value);
}
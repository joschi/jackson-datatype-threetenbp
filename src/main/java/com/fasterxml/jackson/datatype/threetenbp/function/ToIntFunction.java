package com.fasterxml.jackson.datatype.threetenbp.function;

/**
 * Represents a function that accepts one argument and produces an {@link Integer} result.
 * <p/>
 * Very simple and stupid interface for representing functions f(R) -> {@link Integer}.
 *
 * @param <T> the type of the input to the function
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
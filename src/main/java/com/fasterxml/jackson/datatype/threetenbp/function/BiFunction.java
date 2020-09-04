package com.fasterxml.jackson.datatype.threetenbp.function;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the two-arity specialization of {@link Function} for representing
 * functions f(T, U) &rarr; R.
 *
 * @see Function
 */
public interface BiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u);
}

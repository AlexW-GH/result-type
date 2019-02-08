package tech.wendt.resulttype;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A container object which contains a value or an error.
 *
 * @param <T> The type of the value
 * @param <E> The type of the error
 */
public interface Result<T, E> {

    /**
     *
     * Returns an {@code OkResult} instance.
     *
     * @param value The value to be contained in the {@code Result}
     * @param <T> The type of the value
     * @param <E> The type of the error
     * @return a {@code Result} with the value present
     * @throws NullPointerException if {@code value} is {@code null}
     */
    static <T, E> Result<T, E> of(T value){
        return new OkResult<>(value);
    }

    /**
     *
     * Returns an error {@code ErrResult} instance.
     *
     * @param error The error to be contained in the {@code Result}
     * @param <T> The type of the value
     * @param <E> The type of the error
     * @return a {@code Result} with the error present
     * @throws NullPointerException if {@code error} is {@code null}
     */
    static <T, E> Result<T, E> error(E error){
        return new ErrResult<>(error);
    }

    /**
     *
     * Returns an {@code OkResult} instance, if the provided value is not {@code null}.
     * Otherwise it returns an {@code ErrResult} instance.
     *
     * @param value The value to be contained in the {@code Result}
     * @param error The error to be contained in the {@code Result}
     * @param <T> The type of the value
     * @param <E> The type of the error
     * @return A {@code Result} with the value or error present
     * @throws NullPointerException if {@code error} is {@code null}
     */
    static <T, E> Result<T, E> ofNullable(T value, E error){
        if(value != null){
            return Result.of(value);
        } else {
            return Result.error(error);
        }
    }

    /**
     *
     * @return An {@code Optional} with the value present, if the {@code Result} is an {@code OkResult}
     */
    Optional<T> getOptional();

    /**
     *
     * @return An {@code Optional} with the error present, if the {@code Result} is an {@code ErrResult}
     */
    Optional<E> getErrorOptional();

    /**
     *
     * Returns the value, if the {@code Result} is an {@code OkResult}, otherwise throws {@code NoSuchElementException}.
     *
     * @return The value of the {@code Result}
     * @throws NoSuchElementException if the {@code Result} is an error
     */
    T get();

    /**
     *
     * Returns the error, if the {@code Result} is an {@code ErrResult}, otherwise throws {@code NoSuchElementException}.
     *
     * @return The error of the {@code Result}
     * @throws NoSuchElementException if the {@code Result} is an error
     */
    E getError();


    /**
     *
     * If the {@code Result} is an {@code OkResult} returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is present, otherwise {@code false}
     */
    boolean isOk();

    /**
     *
     * If the {@code Result} is an {@code ErrResult}, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if an error is present, otherwise {@code false}
     */
    boolean isError();

    /**
     * If the {@code Result} is an {@code OkResult}, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param action the action to be performed, if a value is present
     * @throws NullPointerException if value is present and the given action is
     *         {@code null}
     */
    void ifPresent(Consumer<? super T> action);

    /**
     * If the {@code Result} is an {@code OkResult}, performs the given action with the value,
     * otherwise performs the given error-based action.
     *
     * @param action the action to be performed, if a value is present
     * @param errorAction the error-based action to be performed, if an error is
     *        present
     * @throws NullPointerException if a value is present and the given action
     *         is {@code null}, or an error is present and the given error-based
     *         action is {@code null}.
     */
    void ifPresentOrElse(Consumer<? super T> action, Runnable errorAction);

    /**
     * If the {@code Result} is an {@code OkResult}, and the value matches the given predicate,
     * returns an {@code Optional} describing the value, otherwise returns an
     * empty {@code Optional}.
     *
     * @param predicate the predicate to apply to a value, if present
     * @return an {@code Optional} describing the value of this
     *         {@code Optional}, if a value is present and the value matches the
     *         given predicate, otherwise an empty {@code Optional}
     * @throws NullPointerException if the predicate is {@code null}

     */
    Optional<T> filter(Predicate<? super T> predicate);


    /**
     * If  the {@code Result} is an {@code OkResult}, returns a {@code Result} describing
     * the result of applying the given mapping function to
     * the value, otherwise returns an {@code ErrResult}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a {@code Result} describing the result of applying a mapping
     *         function to the value of this {@code Result}, if a value is
     *         present, otherwise the {@code Result} without mapping applied.
     * @throws NullPointerException if the mapping function is {@code null}
     * @throws NullPointerException if the mapping function was applied and returns {@code null}
     */
    <U> Result<U, E> map(Function<T, U> mapper);

    /**
     * If  the {@code Result} is an {@code ErrResult}, returns a {@code Result} describing
     * the result of applying the given mapping function to
     * the error, otherwise returns the {@code Result} without mapping applied.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return a {@code Result} describing the result of applying a mapping
     *         function to the value of this {@code Result}, if a value is
     *         present, otherwise an {@code ErrResult}
     * @throws NullPointerException if the mapping function is {@code null}
     * @throws NullPointerException if the mapping function was applied and returns {@code null}
     */
    <U> Result<T, U> mapError(Function<? super E, ? extends U> mapper);

    /**
     * If the {@code Result} is an {@code OkResult}, returns the result of applying the given
     * {@code Result}-bearing mapping function to the value, otherwise
     * returns the {@code Result} without mapping applied.
     *
     * @param <U> The type of value of the {@code Result} returned by the
     *            mapping function
     * @param mapper the mapping function to apply to a value, if present
     * @return the result of applying a {@code Result}-bearing mapping
     *         function to the value of this {@code Result}, if a value is
     *         present, otherwise an {@code ErrResult}
     * @throws NullPointerException if the mapping function is {@code null} or
     *         returns a {@code null} result
     */
    <U> Result<U, E> flatMap(Function<? super T, ? extends Result<? extends U, ? extends E>> mapper);

    /**
     * If the {@code Result} is an {@code ErrResult}, returns the result of applying the given
     * {@code Result}-bearing mapping function to the error, otherwise returns
     * returns the {@code Result} without mapping applied.
     *
     * @param <U> The type of error of the {@code Result} returned by the
     *            mapping function
     * @param mapper the mapping function to apply to an error, if present
     * @return the result of applying a {@code Result}-bearing mapping
     *         function to the value of this {@code Result}, if a value is
     *         present, otherwise an {@code ErrResult}
     * @throws NullPointerException if the mapping function is {@code null} or
     *         returns a {@code null} result
     */
    <U> Result<T, U> flatMapError(Function<? super E, ? extends Result<? extends T, ? extends U>> mapper);

    /**
     * If the {@code Result} is an {@code OkResult}, returns a {@code Result} describing the value,
     * otherwise returns a {@code Result} produced by the supplying function.
     *
     * @param supplier the supplying function that produces a {@code Result}
     *        to be returned
     * @return returns a {@code Result} describing the value of this
     *         {@code Result}, if the {@code Result} is an {@code OkResult}, otherwise a
     *         {@code Result} produced by the supplying function.
     * @throws NullPointerException if the supplying function is {@code null} or
     *         produces a {@code null} result
     */
    Result<T, E> or(Supplier<? extends Result<? extends T, ? extends E>> supplier);

    /**
     * If the {@code Result} is an {@code OkResult}, returns a sequential {@link Stream} containing
     * only the contained value, otherwise returns an empty {@code Stream}.
     *
     * @return the value as a {@code Stream}
     */
    Stream<T> stream();
    /**
     * If the {@code Result} is an {@code ErrResult}, returns a sequential {@link Stream} containing
     * only the contained error, otherwise returns an empty {@code Stream}.
     *
     * @return the error as a {@code Stream}
     */
    Stream<E> streamError();


    /**
     * If the {@code Result} is an {@code OkResult}, returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if the {@code Result} is an {@code ErrResult}.
     *        May be {@code null}.
     * @return the value, if the {@code Result} is an {@code OkResult}, otherwise {@code other}
     */
    T orElse(T other);

    /**
     * If the {@code Result} is an {@code OkResult}, returns the value, otherwise returns the result
     * produced by the supplying function.
     *
     * @param supplier the supplying function that produces a value to be returned
     * @return the value, if the {@code Result} is an {@code OkResult}, otherwise the result produced by the
     *         supplying function
     * @throws NullPointerException if the {@code Result} is an {@code ErrResult} and the supplying
     *         function is {@code null}
     */
    T orElseGet(Supplier<? extends T> supplier);

    /**
     * If the {@code Result} is an {@code OkResult}, returns the value, otherwise throws an exception
     * produced by the exception supplying function.
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier the supplying function that produces an
     *        exception to be thrown
     * @return the value, if the {@code Result} is an {@code OkResult}
     * @throws X if the {@code Result} is an {@code ErrResult}
     * @throws NullPointerException if the {@code Result} is ERROR and the exception
     *          supplying function is {@code null}
     */
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

}
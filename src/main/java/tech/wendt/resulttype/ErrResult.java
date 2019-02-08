package tech.wendt.resulttype;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A Result that represents a Result containing an error.
 *
 * @param <T> The type of the value
 * @param <E> The type of the error
 */
class ErrResult<T, E> implements Result<T, E> {

    private E error;

    ErrResult(E error) {
        this.error = Objects.requireNonNull(error);
    }

    @Override
    public Optional<T> getOptional() {
        return Optional.empty();
    }

    @Override
    public Optional<E> getErrorOptional() {
        return Optional.of(error);
    }

    @Override
    public T get() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public E getError() {
        return error;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public void ifPresent(Consumer<? super T> action) {
        Objects.requireNonNull(action);
    }

    @Override
    public void ifPresentOrElse(Consumer<? super T> action, Runnable errorAction) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(errorAction);

        errorAction.run();
    }

    @Override
    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);

        return Optional.empty();
    }

    @Override
    public <U> Result<U, E> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper);

        return Result.error(this.error);
    }

    @Override
    public <U> Result<T, U> mapError(Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        return Result.error(Objects.requireNonNull(mapper.apply(error)));
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
        Objects.requireNonNull(mapper);

        return Result.error(this.error);
    }

    @Override
    public <U> Result<T, U> flatMapError(Function<? super E, ? extends Result<? extends T, ? extends U>> mapper) {
        Objects.requireNonNull(mapper);

        @SuppressWarnings("unchecked")
        Result<T, U> result = (Result<T, U>) Objects.requireNonNull(mapper.apply(error));
        return result;
    }

    @Override
    public Result<T, E> or(Supplier<? extends Result<? extends T, ? extends E>> supplier) {
        Objects.requireNonNull(supplier);

        @SuppressWarnings("unchecked")
        Result<T, E> result = (Result<T, E>) supplier.get();
        return result;
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public Stream<E> streamError() {
        return Stream.of(error);
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return supplier.get();
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);

        throw exceptionSupplier.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Result)) {
            return false;
        }
        Result<?, ?> other = (Result<?, ?>) obj;
        if(other.isOk()){
            return false;
        }
        return Objects.equals(error, other.getError());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(error);
    }

    @Override
    public String toString() {
        return String.format("Result[Err[%s]]", error);
    }
}

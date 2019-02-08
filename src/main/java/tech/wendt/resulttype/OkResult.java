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
 * A Result that represents a Result containing a value.
 *
 * @param <T> The type of the value
 * @param <E> The type of the error
 */
class OkResult<T, E> implements Result<T, E> {

    private T value;

    OkResult(T value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public E getError() {
        throw new NoSuchElementException("No error present");
    }

    @Override
    public Optional<T> getOptional() {
        return Optional.of(value);
    }

    @Override
    public Optional<E> getErrorOptional() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public void ifPresent(Consumer<? super T> action) {
        Objects.requireNonNull(action);

        action.accept(value);
    }

    @Override
    public void ifPresentOrElse(Consumer<? super T> action, Runnable errorAction) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(errorAction);

        action.accept(value);
    }

    @Override
    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);

        return predicate.test(value) ? Optional.of(value) : Optional.empty();
    }

    @Override
    public <U> Result<U, E> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper);

        return Result.of(Objects.requireNonNull(mapper.apply(value)));
    }

    @Override
    public <U> Result<T, U> mapError(Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        return Result.of(this.value);
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
        Objects.requireNonNull(mapper);

        @SuppressWarnings("unchecked")
        Result<U, E> result = (Result<U, E>) Objects.requireNonNull(mapper.apply(value));
        return result;
    }

    @Override
    public <U> Result<T, U> flatMapError(Function<? super E, ? extends Result<? extends T, ? extends U>> mapper) {
        Objects.requireNonNull(mapper);

        return Result.of(this.value);
    }

    @Override
    public Result<T, E> or(Supplier<? extends Result<? extends T, ? extends E>> supplier) {
        Objects.requireNonNull(supplier);

        return this;
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(value);
    }

    @Override
    public Stream<E> streamError() {
        return Stream.empty();
    }

    @Override
    public T orElse(T other) {
        Objects.requireNonNull(other);

        return value;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return value;
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);

        return value;
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
        if(other.isError()){
            return false;
        }
        return Objects.equals(value, other.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Result[Ok[%s]]", value);
    }
}

package tech.wendt.resulttype;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrResultTest {


    @Test
    public void getOptional() {
        Result<Object, Integer> result = Result.error(1);
        assertThat(result.getOptional()).isNotPresent();
    }

    @Test
    public void getErrorOptional() {
        Result<Object, Integer> result = Result.error(1);
        assertThat(result.getErrorOptional()).isPresent();
        assertThat(result.getErrorOptional().get()).isEqualTo(1);
    }

    @Test(expected = NoSuchElementException.class)
    public void get() {
        Result<Object, Integer> result = Result.error(1);
        result.get();
    }


    public void getError() {
        Result<Object, Integer> result = Result.error(1);
        assertThat(result.getError()).isEqualTo(1);
    }

    @Test
    public void isOk() {
        Result<Object, Integer> result = Result.error(1);
        assertThat(result.isOk()).isFalse();
    }

    @Test
    public void isError() {
        Result<Object, Integer> result = Result.error(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void ifPresent() {
        List<Integer> result = new ArrayList<>();
        Result.<Integer, Integer>error(1).ifPresent(result::add);
        assertThat(result).isEmpty();
    }

    @Test
    public void ifPresentOrElse() {
        List<Integer> result = new ArrayList<>();
        Result.<Integer, Integer>error(1).ifPresentOrElse(result::add, () -> result.add(0));
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).contains(0);
    }

    @Test
    public void filter() {
        Optional<Integer> result = Result.<Integer, Integer>error(1).filter(value -> value.equals(1));
        assertThat(result).isEmpty();
    }

    @Test
    public void map() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).map(value -> value + value);
        assertThat(result.isError()).isTrue();
        assertThat(result.getError()).isEqualTo(1);
    }

    @Test
    public void mapError() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).mapError(value -> value + value);
        assertThat(result.isError()).isTrue();
        assertThat(result.getError()).isEqualTo(2);
    }

    @Test
    public void flatMap() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).flatMap(value -> Result.of(2));
        assertThat(result.isError()).isTrue();
        assertThat(result.getError()).isEqualTo(1);
    }

    @Test
    public void flatMapError() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).flatMapError(value -> Result.of(value +  value));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(2);
    }

    @Test
    public void or_withOk() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).or(() -> Result.of(2));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(2);
    }

    @Test
    public void or_withErr() {
        Result<Integer, Integer> result = Result.<Integer, Integer>error(1).or(() -> Result.error(2));
        assertThat(result.isError()).isTrue();
        assertThat(result.getError()).isEqualTo(2);
    }

    @Test
    public void stream() {
        List<Integer> result = Result.<Integer, Integer>error(1).stream().collect(Collectors.toList());
        assertThat(result).isEmpty();
    }

    @Test
    public void streamError() {
        List<Integer> result = Result.<Integer, Integer>error(1).streamError().collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(1);
    }

    @Test
    public void orElse() {
        Integer result = Result.<Integer, Integer>error(1).orElse(2);
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void orElseGet() {
        Integer result = Result.<Integer, Integer>error(1).orElseGet(() -> 2);
        assertThat(result).isEqualTo(2);
    }

    @Test(expected = NoSuchElementException.class)
    public void orElseThrow() {
        Result.error(1).orElseThrow(NoSuchElementException::new);
    }
}

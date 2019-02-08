package tech.wendt.resulttype;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OkResultTest {

    @Test
    public void getOptional() {
        Result<Integer, String> underTest = Result.of(1);
        assertThat(underTest.getOptional()).isPresent();
        assertThat(underTest.getOptional().get()).isEqualTo(1);
    }

    @Test
    public void getErrorOptional() {
        Result<Integer, String> underTest = Result.of(1);
        assertThat(underTest.getErrorOptional()).isNotPresent();
    }

    @Test
    public void get() {
        Result<Integer, String> underTest = Result.of(1);
        assertThat(underTest.get()).isEqualTo(1);
    }

    @Test(expected = NoSuchElementException.class)
    public void getError() {
        Result<Integer, String> underTest = Result.of(1);
        underTest.getError();
    }

    @Test
    public void isOk() {
        Result<Integer, String> underTest = Result.of(1);
        assertThat(underTest.isOk()).isTrue();
    }

    @Test
    public void isError() {
        Result<Integer, String> underTest = Result.of(1);
        assertThat(underTest.isError()).isFalse();
    }

    @Test
    public void ifPresent() {
        List<Integer> result = new ArrayList<>();
        Result.of(1).ifPresent(result::add);
        assertThat(result).contains(1);
    }

    @Test
    public void ifPresentOrElse() {
        List<Integer> result = new ArrayList<>();
        Result.of(1).ifPresentOrElse(result::add, () -> result.add(0));
        assertThat(result).contains(1);
    }

    @Test
    public void filter_found() {
        Optional<Integer> result = Result.of(1).filter(value -> value.equals(1));
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void filter_notfound() {
        Optional<Integer> result = Result.of(1).filter(value -> value.equals(0));
        assertThat(result).isNotPresent();
    }

    @Test
    public void map() {
        Result<Integer, Object> result = Result.of(1).map(value -> value +  value);
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(2);
    }

    @Test
    public void mapError() {
        Result<Integer, Integer> result = Result.<Integer, Integer>of(1).mapError(error -> error + error);
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void flatMap() {
        Result<Integer, Object> result = Result.of(1).flatMap(value -> Result.of(value +  value));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(2);
    }

    @Test
    public void flatMapError() {
        Result<Integer, Integer> result = Result.<Integer, Integer>of(1).flatMapError(error -> Result.of(2));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void or_withOk() {
        Result<Integer, Object> result = Result.of(1).or(() -> Result.of(2));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void or_withErr() {
        Result<Integer, Integer> result = Result.<Integer, Integer>of(1).or(() -> Result.error(2));
        assertThat(result.isOk()).isTrue();
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void stream() {
        List<Integer> result = Result.of(1).stream().collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(1);
    }

    @Test
    public void streamError() {
        List<String> result = Result.<Integer, String>of(1).streamError().collect(Collectors.toList());
        assertThat(result).isEmpty();
    }

    @Test
    public void orElse() {
        Integer result = Result.of(1).orElse(2);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void orElseGet() {
        Integer result = Result.of(1).orElseGet(() -> 2);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void orElseThrow() {
        Integer result = Result.of(1).orElseThrow(NoSuchElementException::new);
        assertThat(result).isEqualTo(1);
    }
}

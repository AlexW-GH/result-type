package tech.wendt.resulttype;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {


    @Test
    public void of() {
        Result<Integer, Object> result = Result.of(5);
        assertThat(result.get()).isEqualTo(5);
    }

    @Test
    public void error() {
        Result<Object, String> result = Result.error("error");
        assertThat(result.getError()).isEqualTo("error");
    }

    @Test
    public void ofNullable_value() {
        Result<Integer, String> result = Result.ofNullable(5, "error");
        assertThat(result.get()).isEqualTo(5);
    }

    @Test
    public void ofNullable_null() {
        Result<Integer, String> result = Result.ofNullable(null, "error");
        assertThat(result.getError()).isEqualTo("error");
    }
}

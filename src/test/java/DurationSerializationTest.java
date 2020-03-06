import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.junit.jupiter.api.Assertions.*;


public class DurationSerializationTest {

    private ObjectWriter writer;

    @BeforeEach
    public void init() {
        writer = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build()
            .writer();
    }

    // Default behavior doesn't seem to work
    @Test
    public void test_defaultFeature() throws Exception {
        String result = writer.writeValueAsString(Duration.ofMillis(-1));
        assertEquals("-0.001000000", result);
    }

    // Handling with WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS disabled seems to work
    @Test
    public void test_withoutNanosecondFeature() throws Exception {
        String result = writer
            .without(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .writeValueAsString(Duration.ofMillis(-1));
        assertEquals("-1", result);
    }

    // Verification that handling with WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS matches the default handling
    @Test
    public void test_withNanosecondFeature() throws Exception {
        String result = writer
            .with(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .writeValueAsString(Duration.ofMillis(-1));
        assertEquals("-0.001000000", result);
    }
}

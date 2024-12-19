import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class DurationTest {
    // Default behavior doesn't seem to work
    @Test
    public void test_default() throws Exception {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        Duration duration = Duration.parse("PT-43.636S");

        String ser = mapper.writeValueAsString(duration);

        // 2.12.0+
        assertEquals("-43.636000000", ser);

        // 2.11.4
        // assertEquals("-44.364000000", ser);

        Duration deser = mapper.readValue(ser, Duration.class);

        // 2.12.0+
        assertNotEquals(duration, deser);
        assertEquals(deser.toString(), "PT-42.364S");

        // 2.11.4
        // assertEquals(duration, deser);
        // assertEquals(deser.toString(), "PT-43.636S");
    }

    // Handling with WRITE_DURATIONS_AS_TIMESTAMPS enabled can't round-trip a value
    @Test
    public void test_with() throws Exception {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true)
                .build();

        Duration duration = Duration.parse("PT-43.636S");

        String ser = mapper.writeValueAsString(duration);

        // 2.12.0+
        assertEquals("-43.636000000", ser);

        // 2.11.4
        // assertEquals("-44.364000000", ser);

        Duration deser = mapper.readValue(ser, Duration.class);

        // 2.12.0+
        assertNotEquals(duration, deser);
        assertEquals(deser.toString(), "PT-42.364S");

        // 2.11.4
        // assertEquals(duration, deser);
        // assertEquals(deser.toString(), "PT-43.636S");
    }

    // Handling with WRITE_DURATIONS_AS_TIMESTAMPS disabled works
    @Test
    public void test_without() throws Exception {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .build();

        Duration duration = Duration.parse("PT-43.636S");

        String ser = mapper.writeValueAsString(duration);
        assertEquals("\"PT-43.636S\"", ser);

        Duration deser = mapper.readValue(ser, Duration.class);
        assertEquals(duration, deser);
    }
}

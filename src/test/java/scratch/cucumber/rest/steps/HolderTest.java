package scratch.cucumber.rest.steps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HolderTest {

    @Test
    public void I_can_create_a_holder_with_a_value() {

        final String value = "test value";

        assertEquals("the value should be correct", value, new Holder<>(value).get());
    }

    @Test
    public void I_can_create_a_holder_with_null() {

        assertNull("the value should be null", new Holder<>(null).get());
    }
}

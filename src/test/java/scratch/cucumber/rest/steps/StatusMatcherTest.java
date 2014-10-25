package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.StatusMatcher.statusEquals;

public class StatusMatcherTest {

    @Test
    public void I_can_match_a_responses_status() {

        final int status = 999;

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(status);

        assertThat("the response should be correct.", response, statusEquals(status));
    }

    @Test(expected = AssertionError.class)
    public void I_cannot_match_a_responses_status() {

        final int status = 999;

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(status);

        assertThat("the response should be correct.", response, statusEquals(998));
    }
}

package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponsesTest {

    private ClientResponse responseOne;
    private ClientResponse responseTwo;
    private ClientResponse responseThree;

    private List<ClientResponse> responsesList;

    @Before
    public void setUp() {

        responseOne = mock(ClientResponse.class);
        responseTwo = mock(ClientResponse.class);
        responseThree = mock(ClientResponse.class);

        responsesList = asList(responseOne, responseTwo, responseThree);
    }

    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void I_can_create_a_an_empty_responses_history() {

        assertNoResponses(new Responses());
    }

    @Test
    public void I_can_create_a_responses_history_with_a_deque() {

        final Deque<ClientResponse> responseDeque = new ArrayDeque<>();

        for (ClientResponse response : responsesList) {
            responseDeque.push(response);
        }

        assertIterator(responsesList, new Responses(responseDeque));
    }

    @Test
    public void I_can_add_responses_to_a_responses_history() {

        final Responses responses = new Responses();

        for (ClientResponse response : responsesList) {
            responses.add(response);
        }

        assertIterator(responsesList, responses);
    }

    @Test(expected = NullPointerException.class)
    public void I_cannot_create_a_responses_with_a_null_deque() {

        new Responses().add(null);
    }

    @Test
    public void I_can_get_the_latest_response() {

        final Responses responses = new Responses();

        responses.add(responseOne);
        assertEquals("the latest response should be correct.", responseOne, responses.latest());

        responses.add(responseTwo);
        assertEquals("the latest response should be correct.", responseTwo, responses.latest());

        responses.add(responseThree);
        assertEquals("the latest response should be correct.", responseThree, responses.latest());
    }

    @Test
    public void I_cannot_get_the_latest_response_from_an_empty_responses_history() {

        final Responses responses = new Responses();

        assertNull("the latest response should be null.", responses.latest());
    }

    @Test
    public void I_can_filter_the_responses_history_for_the_type_of_response_I_want() {

        when(responseOne.getStatus()).thenReturn(CREATED.getStatusCode());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.getStatusCode());
        when(responseThree.getStatus()).thenReturn(OK.getStatusCode());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created response should be correct.", responses.filter(CREATED), hasItems(responseOne));
        assertThat("the accepted response should be correct.", responses.filter(ACCEPTED), hasItems(responseTwo));
        assertThat("the ok response should be correct.", responses.filter(OK), hasItems(responseThree));
        assertThat("the not found responses should be empty.", responses.filter(NOT_FOUND).get(), empty());
    }

    @Test
    public void I_can_get_all_the_created_responses_from_the_responses_history() {

        when(responseOne.getStatus()).thenReturn(CREATED.getStatusCode());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.getStatusCode());
        when(responseThree.getStatus()).thenReturn(CREATED.getStatusCode());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created responses should be correct.", responses.created(),
                hasItems(responseOne, responseThree));
    }

    @Test
    public void I_cannot_get_any_created_responses_from_the_responses_if_non_exists() {

        when(responseOne.getStatus()).thenReturn(OK.getStatusCode());
        when(responseTwo.getStatus()).thenReturn(ACCEPTED.getStatusCode());
        when(responseThree.getStatus()).thenReturn(NO_CONTENT.getStatusCode());

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        assertThat("the created responses should be empty.", responses.created().get(), empty());
    }

    @Test
    public void I_can_clear_the_responses_history() {

        final Responses responses = new Responses();
        responses.add(responseOne);
        responses.add(responseTwo);
        responses.add(responseThree);

        responses.clear();

        assertNoResponses(responses);
    }

    public static void assertIterator(List<ClientResponse> responsesList, Responses responses) {

        int lastIndex = responsesList.size() - 1;
        int count = 0;

        for (ClientResponse response : responses) {

            assertEquals("the response should be correct.", responsesList.get(lastIndex - count++), response);
        }

        assertEquals("the number of responses should be correct.", responsesList.size(), count);
    }

    private static void assertNoResponses(Responses responses) {
        for (ClientResponse response : responses) {
            fail("there should be no responses: " + response);
        }
    }
}

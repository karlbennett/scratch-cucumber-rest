package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.HEAD;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestsTest {

    private ClientRequest requestOne;
    private ClientRequest requestTwo;
    private ClientRequest requestThree;

    private List<ClientRequest> requestsList;

    @Before
    public void setUp() {

        requestOne = mock(ClientRequest.class);
        requestTwo = mock(ClientRequest.class);
        requestThree = mock(ClientRequest.class);

        requestsList = asList(requestOne, requestTwo, requestThree);
    }

    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void I_can_create_a_an_empty_requests_history() {

        assertNoRequests(new Requests());
    }

    @Test
    public void I_can_create_a_requests_history_with_a_deque() {

        final Deque<ClientRequest> requestDeque = new ArrayDeque<>();

        for (ClientRequest request : requestsList) {
            requestDeque.push(request);
        }

        assertIterator(requestsList, new Requests(requestDeque));
    }

    @Test
    public void I_can_add_requests_to_a_requests_history() {

        final Requests requests = new Requests();

        for (ClientRequest request : requestsList) {
            requests.add(request);
        }

        assertIterator(requestsList, requests);
    }

    @Test(expected = NullPointerException.class)
    public void I_cannot_create_a_requests_with_a_null_deque() {

        new Requests().add(null);
    }

    @Test
    public void I_can_get_the_latest_request() {

        final Requests requests = new Requests();

        requests.add(requestOne);
        assertEquals("the latest request should be correct.", requestOne, requests.latest());

        requests.add(requestTwo);
        assertEquals("the latest request should be correct.", requestTwo, requests.latest());

        requests.add(requestThree);
        assertEquals("the latest request should be correct.", requestThree, requests.latest());
    }

    @Test
    public void I_cannot_get_the_latest_request_from_an_empty_requests_history() {

        final Requests requests = new Requests();

        assertNull("the latest request should be null.", requests.latest());
    }

    @Test
    public void I_can_filter_the_requests_history_for_the_type_of_request_I_want() {

        when(requestOne.getMethod()).thenReturn(POST);
        when(requestTwo.getMethod()).thenReturn(GET);
        when(requestThree.getMethod()).thenReturn(PUT);

        final Requests requests = new Requests();
        requests.add(requestOne);
        requests.add(requestTwo);
        requests.add(requestThree);

        assertThat("the post request should be correct.", requests.filter(POST), hasItems(requestOne));
        assertThat("the get request should be correct.", requests.filter(GET), hasItems(requestTwo));
        assertThat("the put request should be correct.", requests.filter(PUT), hasItems(requestThree));
        assertThat("the delete requests should be empty.", requests.filter(DELETE).get(), empty());
    }

    @Test
    public void I_can_get_all_the_created_requests_from_the_requests_history() {

        when(requestOne.getMethod()).thenReturn(POST);
        when(requestTwo.getMethod()).thenReturn(PUT);
        when(requestThree.getMethod()).thenReturn(POST);

        final Requests requests = new Requests();
        requests.add(requestOne);
        requests.add(requestTwo);
        requests.add(requestThree);

        assertThat("the created requests should be correct.", requests.created(),
                hasItems(requestOne, requestThree));
    }

    @Test
    public void I_can_get_all_the_updated_requests_from_the_requests_history() {

        when(requestOne.getMethod()).thenReturn(PUT);
        when(requestTwo.getMethod()).thenReturn(GET);
        when(requestThree.getMethod()).thenReturn(PUT);

        final Requests requests = new Requests();
        requests.add(requestOne);
        requests.add(requestTwo);
        requests.add(requestThree);

        assertThat("the updated requests should be correct.", requests.updated(),
                hasItems(requestOne, requestThree));
    }

    @Test
    public void I_cannot_get_any_created_requests_from_the_requests_if_non_exists() {

        when(requestOne.getMethod()).thenReturn(GET);
        when(requestTwo.getMethod()).thenReturn(PUT);
        when(requestThree.getMethod()).thenReturn(HEAD);

        final Requests requests = new Requests();
        requests.add(requestOne);
        requests.add(requestTwo);
        requests.add(requestThree);

        assertThat("the created requests should be empty.", requests.created().get(), empty());
    }

    @Test
    public void I_can_clear_the_requests_history() {

        final Requests requests = new Requests();
        requests.add(requestOne);
        requests.add(requestTwo);
        requests.add(requestThree);

        requests.clear();

        assertNoRequests(requests);
    }

    public static void assertIterator(List<ClientRequest> requestsList, Requests requests) {

        int lastIndex = requestsList.size() - 1;
        int count = 0;

        for (ClientRequest request : requests) {

            assertEquals("the request should be correct.", requestsList.get(lastIndex - count++), request);
        }

        assertEquals("the number of requests should be correct.", requestsList.size(), count);
    }

    private static void assertNoRequests(Requests requests) {
        for (ClientRequest request : requests) {
            fail("there should be no requests: " + request);
        }
    }
}

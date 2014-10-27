package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.WebTarget;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockRetrieveUser;

@RunWith(MockitoJUnitRunner.class)
public class UserRetrieveStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Responses responses;

    @InjectMocks
    private UserRetrieveSteps steps;

    @Test
    public void I_can_retrieve_a_user() {

        final Builder builder = mockRetrieveUser(responses, client);

        steps.I_request_an_existing_user();

        verify(builder).get();
    }

    @Test
    public void I_can_retrieve_all_users() {

        final Builder builder = mockRetrieveUser(responses, client, "");

        steps.I_request_all_existing_user();

        verify(builder).get();
    }

    @Test
    public void I_can_check_that_the_user_is_in_the_response_body() {

        final HashMap<String, Object> map = new HashMap<>();

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(Map.class)).thenReturn(map);

        when(user.toMap()).thenReturn(map);
        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_contain_the_user();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_the_user_is_not_in_the_response_body() {

        when(user.toMap()).thenReturn(Collections.<String, Object>singletonMap("email", 1));

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(Map.class)).thenReturn(Collections.<String, Object>singletonMap("email", 2));

        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_contain_the_user();
    }

    @Test
    public void I_can_check_that_the_right_users_are_in_the_response_body() {

        final Map map = mock(Map.class);
        final Set<Map> users = singleton(map);

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(Set.class)).thenReturn(users);
        when(response.readEntity(Map.class)).thenReturn(map);

        when(responses.latest()).thenReturn(response);
        when(responses.created()).thenReturn(responses);
        when(responses.iterator()).thenReturn(singleton(response).iterator());

        steps.the_response_body_should_contain_all_the_requested_users();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_the_wrong_users_are_in_the_response_body() {

        final Set<Map> users = singleton(mock(Map.class));

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(Set.class)).thenReturn(users);
        when(response.readEntity(Map.class)).thenReturn(mock(Map.class));

        when(responses.latest()).thenReturn(response);
        when(responses.created()).thenReturn(responses);
        when(responses.iterator()).thenReturn(singleton(response).iterator());

        steps.the_response_body_should_contain_all_the_requested_users();
    }
}

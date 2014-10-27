package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockClientResponse;
import static scratch.cucumber.rest.steps.UserFields.ID;

@RunWith(MockitoJUnitRunner.class)
public class UserCreateStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Responses responses;

    @InjectMocks
    private UserCreateSteps steps;

    private Builder builder;
    private Map map;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        map = mock(Map.class);

        when(user.toMap()).thenReturn(map);

        builder = mock(Builder.class);

        when(client.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
    }

    @Test
    public void I_can_create_a_user() {

        steps.I_create_the_user();

        verify(builder, times(1)).post(any(Entity.class));
    }

    @Test
    public void I_can_check_for_a_persisted_user() {

        final Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("address", new HashMap<String, Object>() {{
            put("id", 2);
        }});

        mockRetrieveUser(map, map);

        steps.the_new_user_should_be_persisted();
    }

    @Test
    public void I_can_check_for_a_persisted_user_with_no_address() {

        final Map<String, Object> map = new HashMap<>();
        map.put("id", 1);

        mockRetrieveUser(map, map);

        steps.the_new_user_should_be_persisted();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_for_a_non_persisted_user() {

        final Map<String, Object> expected = new HashMap<>();
        expected.put("id", 1);

        final Map<String, Object> actual = new HashMap<>();
        actual.put("id", 1);
        actual.put("email", "test email");

        mockRetrieveUser(expected, actual);

        steps.the_new_user_should_be_persisted();
    }

    @Test
    public void I_can_check_that_the_response_body_only_contains_the_id() {

        when(map.keySet()).thenReturn(singleton("id"));

        final ClientResponse response = mockClientResponse(map);

        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_contain_an_id();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_the_response_body_does_not_just_contain_the_id() {

        final Set<String> keys = new HashSet<>();
        keys.add("id");
        keys.add("email");

        when(map.keySet()).thenReturn(keys);

        final ClientResponse response = mockClientResponse(map);

        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_contain_an_id();
    }

    @SuppressWarnings("unchecked")
    private void mockRetrieveUser(Map expected, Map actual) {

        final ClientResponse clientResponse = mockClientResponse(actual);

        final Response response = mock(Response.class);
        when(response.readEntity(Map.class)).thenReturn(actual);

        when(user.toMap()).thenReturn(expected);
        when(responses.latest()).thenReturn(clientResponse);
        when(client.path(actual.get(ID).toString())).thenReturn(client);
        when(builder.get()).thenReturn(response);
    }
}

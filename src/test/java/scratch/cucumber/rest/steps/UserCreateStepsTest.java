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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockClientResponse;

@RunWith(MockitoJUnitRunner.class)
public class UserCreateStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Responses responses;

    @Mock
    private UserSteps userSteps;

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

        final int id = 1;

        final ClientResponse clientResponse = mockClientResponse(singletonMap("id", id));

        when(responses.latest()).thenReturn(clientResponse);

        steps.the_new_user_should_be_persisted();

        verify(userSteps).the_user_should_be_persisted_with_id(id);
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
}

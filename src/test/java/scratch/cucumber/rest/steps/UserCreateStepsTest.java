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
import java.util.Map;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockClientResponse;
import static scratch.cucumber.rest.steps.Mocks.mockMap;

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

        mockRetrieveUser(map, map);

        steps.the_new_user_should_be();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_for_a_non_persisted_user() {

        mockRetrieveUser(map, mock(Map.class));

        steps.the_new_user_should_be();
    }

    private void mockRetrieveUser(Map expected, Map actual) {

        final String id = "test id";

        mockMap(expected, id);

        final ClientResponse clientResponse = mockClientResponse(expected);

        final Response response = mock(Response.class);
        when(response.readEntity(Map.class)).thenReturn(actual);

        when(responses.latest()).thenReturn(clientResponse);
        when(client.path(id)).thenReturn(client);
        when(builder.get()).thenReturn(response);
    }
}

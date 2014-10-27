package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockRetrieveUser;

@RunWith(MockitoJUnitRunner.class)
public class UserUpdateStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Requests requests;

    @Mock
    private Responses responses;

    @Mock
    private UserSteps userSteps;

    @InjectMocks
    private UserUpdateSteps steps;

    @Test
    public void I_can_update_a_user() {

        final Builder builder = mockRetrieveUser(responses, client);

        steps.I_update_the_user();

        verify(builder).put(any(Entity.class));
    }

    @Test
    public void I_can_check_that_the_user_has_been_updated() throws URISyntaxException {

        final int id = 1;

        final ClientRequest request = mock(ClientRequest.class);
        when(request.getUri()).thenReturn(new URI("/test/path/" + id));

        when(requests.updated()).thenReturn(requests);
        when(requests.latest()).thenReturn(request);

        steps.the_user_should_be_updated();

        verify(userSteps).the_user_should_be_persisted_with_id(id);
    }
}

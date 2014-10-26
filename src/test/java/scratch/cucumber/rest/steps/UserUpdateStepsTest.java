package scratch.cucumber.rest.steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static scratch.cucumber.rest.steps.Mocks.mockRetrieveUser;

@RunWith(MockitoJUnitRunner.class)
public class UserUpdateStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Responses responses;

    @InjectMocks
    private UserUpdateSteps steps;

    @Test
    public void I_can_update_a_user() {

        final Builder builder = mockRetrieveUser(responses, client);

        steps.I_update_the_user();

        verify(builder).put(any(Entity.class));
    }
}

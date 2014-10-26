package scratch.cucumber.rest.steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.verify;
import static scratch.cucumber.rest.steps.Mocks.mockRetrieveUser;

@RunWith(MockitoJUnitRunner.class)
public class UserRetrieveStepsTest {

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
}

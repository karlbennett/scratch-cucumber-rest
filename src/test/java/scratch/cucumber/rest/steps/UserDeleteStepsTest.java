package scratch.cucumber.rest.steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.Mocks.mockRetrieveUser;

@RunWith(MockitoJUnitRunner.class)
public class UserDeleteStepsTest {

    @Mock
    private WebTarget client;

    @Mock
    private Responses responses;

    @InjectMocks
    private UserDeleteSteps steps;

    @Test
    public void I_can_delete_a_user() {

        final Builder builder = mockRetrieveUser(responses, client);

        steps.I_delete_an_existing_user();

        verify(builder).delete();
    }

    @Test
    public void I_can_check_that_a_user_has_been_deleted() {

        mockGet(NOT_FOUND.getStatusCode());

        steps.the_user_should_be_deleted();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_a_user_has_not_been_deleted() {

        mockGet(OK.getStatusCode());

        steps.the_user_should_be_deleted();
    }

    private void mockGet(int status) {

        final Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(status);

        final Builder builder = mockRetrieveUser(responses, client);
        when(builder.get()).thenReturn(response);
    }
}

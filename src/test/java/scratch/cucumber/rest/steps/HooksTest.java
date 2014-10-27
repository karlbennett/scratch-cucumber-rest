package scratch.cucumber.rest.steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Set;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HooksTest {

    @Mock
    private PropertyObject user;

    @Mock
    private WebTarget client;

    @Mock
    private Requests requests;

    @Mock
    private Responses responses;

    @InjectMocks
    private Hooks hooks;

    @Test
    public void I_can_setup_for_a_scenario() {

        final int id = 1;

        final Response response = mock(Response.class);
        when(response.readEntity(Set.class)).thenReturn(Collections.singleton(Collections.singletonMap("id", id)));

        final Builder builder = mock(Builder.class);
        when(builder.get()).thenReturn(response);

        when(client.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
        when(client.path(String.valueOf(id))).thenReturn(client);

        hooks.setup();

        verify(user).clear();
        verify(requests).clear();
        verify(responses).clear();
        verify(builder).delete();
    }
}

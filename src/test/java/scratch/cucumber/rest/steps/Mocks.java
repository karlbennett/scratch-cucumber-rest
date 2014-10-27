package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static scratch.cucumber.rest.steps.UserFields.ID;

/**
 * Helper methods for building mocks for the tests.
 */
public class Mocks {

    public static Builder mockRetrieveUser(Responses responses, WebTarget client) {

        return mockRetrieveUser(responses, client, "test id");
    }

    public static Builder mockRetrieveUser(Responses responses, WebTarget client, String id) {

        final Map map = singletonMap(ID, id);

        final ClientResponse clientResponse = mockClientResponse(map);

        mockLatestCreateResponse(responses, clientResponse);

        final Response response = mock(Response.class);
        when(response.readEntity(Map.class)).thenReturn(map);

        final Builder builder = mock(Builder.class);
        when(builder.get()).thenReturn(response);

        mockPath(client, builder, id);

        return builder;
    }

    public static ClientResponse mockClientResponse(Map map) {

        final ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.readEntity(Map.class)).thenReturn(map);

        return clientResponse;
    }

    public static void mockLatestCreateResponse(Responses responses, ClientResponse response) {

        when(responses.created()).thenReturn(responses);
        when(responses.latest()).thenReturn(response);
    }

    public static void mockPath(WebTarget client, Builder builder, String id) {

        when(client.path(id)).thenReturn(client);
        when(client.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
    }
}

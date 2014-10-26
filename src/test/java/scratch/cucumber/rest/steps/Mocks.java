package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static javax.ws.rs.client.Invocation.Builder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Helper methods for building mocks for the tests.
 */
public class Mocks {

    public static Map mockMap(String id) {

        final Map map = mock(Map.class);

        mockMap(map, id);

        return map;
    }

    public static void mockMap(Map map, String id) {

        when(map.get("id")).thenReturn(id);
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

    public static Builder mockRetrieveUser(Responses responses, WebTarget client) {

        return mockRetrieveUser(responses, client, "test id");
    }

    public static Builder mockRetrieveUser(Responses responses, WebTarget client, String id) {

        final Map map = mockMap(id);

        final ClientResponse response = mockClientResponse(map);

        mockLatestCreateResponse(responses, response);

        final Builder builder = mock(Builder.class);

        mockPath(client, builder, id);

        return builder;
    }
}

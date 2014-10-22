package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.Map.Entry;
import static javax.ws.rs.core.Response.StatusType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerboseResponseTest {

    private static final String NEW_LINE = System.lineSeparator();

    private static final int STATUS_CODE = 999;
    private static final String STATUS_REASON = "Test Reason";
    private static final String HEADER_NAME = "Test";
    private static final String HEADER_VALUE = "Header";
    private static final String BODY = "Test Body";

    @Test
    @SuppressWarnings("unchecked")
    public void I_can_to_string_a_verbose_response() {

        assertEquals("the to string should be correct.",
                format("%sHTTP/1.1 %d %s%s%s: %s%s%s%s%s",
                        NEW_LINE,
                        STATUS_CODE, STATUS_REASON, NEW_LINE,
                        HEADER_NAME, HEADER_VALUE, NEW_LINE,
                        NEW_LINE,
                        BODY, NEW_LINE),
                new VerboseResponse(mockResponse()).toString());
    }

    @Test
    public void testGetCookies() {

        @SuppressWarnings("unchecked")
        final Map<String, NewCookie> cookies = mock(Map.class);

        final ClientResponse response = mockResponse();
        when(response.getCookies()).thenReturn(cookies);

        assertEquals("get cookies should delegate.", cookies, new VerboseResponse(response).getCookies());
    }

    @Test
    public void testGetEntity() {

        final Object entity = new Object();

        final ClientResponse response = mockResponse();
        when(response.getEntity()).thenReturn(entity);

        assertEquals("get entity should delegate.", entity, new VerboseResponse(response).getEntity());
    }

    @Test
    public void testGetLinks() {

        @SuppressWarnings("unchecked")
        final Set<Link> links = mock(Set.class);

        final ClientResponse response = mockResponse();
        when(response.getLinks()).thenReturn(links);

        assertEquals("get links should delegate.", links, new VerboseResponse(response).getLinks());
    }

    @Test
    public void testGetRequestContext() {

        final ClientRequest requestContext = mock(ClientRequest.class);

        final ClientResponse response = mockResponse();
        when(response.getRequestContext()).thenReturn(requestContext);

        assertEquals("get requestContext should delegate.", requestContext,
                new VerboseResponse(response).getRequestContext());
    }

    @Test
    public void testGetResolvedRequestUri() {

    }

    @Test
    public void testGetServiceLocator() {

    }

    @Test
    public void testGetStatus() {

    }

    @Test
    public void testGetStatusInfo() {

    }

    @Test
    public void testReadEntityWithClass() {

    }

    @Test
    public void testReadEntityWithClassAndAnnotations() {

    }

    @Test
    public void testReadEntityWithGenericType() {

    }

    @Test
    public void testReadEntityGenericTypeAndAnnotations() {

    }

    @Test
    public void testSetResolvedRequestUri() {

    }

    @Test
    public void testSetStatus() {

    }

    @Test
    public void testSetStatusInfo() {

    }

    @SuppressWarnings("unchecked")
    private static ClientResponse mockResponse() {

        final StatusType statusType = mock(StatusType.class);
        when(statusType.getReasonPhrase()).thenReturn(STATUS_REASON);

        final Entry header = mock(Entry.class);
        when(header.getKey()).thenReturn(HEADER_NAME);
        when(header.getValue()).thenReturn(HEADER_VALUE);

        final MultivaluedMap headers = mock(MultivaluedMap.class);
        when(headers.entrySet()).thenReturn(singleton(header));

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(STATUS_CODE);
        when(response.getStatusInfo()).thenReturn(statusType);
        when(response.getHeaders()).thenReturn(headers);
        when(response.readEntity(String.class)).thenReturn(BODY);

        return response;
    }
}

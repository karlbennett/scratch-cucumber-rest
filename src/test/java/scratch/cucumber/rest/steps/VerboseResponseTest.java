package scratch.cucumber.rest.steps;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.Map.Entry;
import static javax.ws.rs.core.Response.StatusType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    public void testGetResolvedRequestUri() throws URISyntaxException {

        final URI uri = new URI("");

        final ClientResponse response = mockResponse();
        when(response.getResolvedRequestUri()).thenReturn(uri);

        assertEquals("get uri should delegate.", uri, new VerboseResponse(response).getResolvedRequestUri());
    }

    @Test
    public void testGetServiceLocator() {

        final ServiceLocator serviceLocator = mock(ServiceLocator.class);

        final ClientResponse response = mockResponse();
        when(response.getServiceLocator()).thenReturn(serviceLocator);

        assertEquals("get serviceLocator should delegate.", serviceLocator,
                new VerboseResponse(response).getServiceLocator());
    }

    @Test
    public void testGetStatus() {

        final int status = 1;

        final ClientResponse response = mockResponse();
        when(response.getStatus()).thenReturn(status);

        assertEquals("get status should delegate.", status, new VerboseResponse(response).getStatus());
    }

    @Test
    public void testGetStatusInfo() {

        final StatusType statusType = mock(StatusType.class);

        final ClientResponse response = mockResponse();
        when(response.getStatusInfo()).thenReturn(statusType);

        assertEquals("get statusType should delegate.", statusType, new VerboseResponse(response).getStatusInfo());
    }

    @Test
    public void testReadEntityWithClass() {

        final String string = "test entity value";

        final ClientResponse response = mockResponse();
        when(response.readEntity(String.class)).thenReturn(string);

        assertEquals("get entity should delegate.", string, new VerboseResponse(response).readEntity(String.class));
    }

    @Test
    public void testReadEntityWithClassAndAnnotations() {

        final Annotation[] annotations = {};
        final String string = "test entity value";

        final ClientResponse response = mockResponse();
        when(response.readEntity(String.class, annotations)).thenReturn(string);

        assertEquals("get entity should delegate.", string,
                new VerboseResponse(response).readEntity(String.class, annotations));
    }

    @Test
    public void testReadEntityWithGenericType() {

        final GenericType type = mock(GenericType.class);
        final Object entity = new Object();

        final ClientResponse response = mockResponse();
        when(response.readEntity(type)).thenReturn(entity);

        assertEquals("get entity should delegate.", entity, new VerboseResponse(response).readEntity(type));
    }

    @Test
    public void testReadEntityGenericTypeAndAnnotations() {

        final Annotation[] annotations = {};
        final GenericType type = mock(GenericType.class);
        final Object entity = new Object();

        final ClientResponse response = mockResponse();
        when(response.readEntity(type, annotations)).thenReturn(entity);

        assertEquals("get entity should delegate.", entity,
                new VerboseResponse(response).readEntity(type, annotations));
    }

    @Test
    public void testSetResolvedRequestUri() throws URISyntaxException {

        final URI uri = new URI("");

        final ClientResponse response = mockResponse();

        new VerboseResponse(response).setResolvedRequestUri(uri);

        verify(response, times(1)).setResolvedRequestUri(uri);
    }

    @Test
    public void testSetStatus() {

        final int status = 1;

        final ClientResponse response = mockResponse();

        new VerboseResponse(response).setStatus(status);

        verify(response, times(1)).setStatus(status);
    }

    @Test
    public void testSetStatusInfo() {

        final StatusType status = mock(StatusType.class);

        final ClientResponse response = mockResponse();

        new VerboseResponse(response).setStatusInfo(status);

        verify(response, times(1)).setStatusInfo(status);
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

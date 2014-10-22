package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.CREATED;

public class Responses extends Holder<Deque<ClientResponse>> implements Iterable<ClientResponse> {

    public Responses() {
        this(new ArrayDeque<ClientResponse>());
    }

    public Responses(Deque<ClientResponse> responses) {
        super(responses);
    }

    public void add(ClientResponse response) {
        get().push(response);
    }

    public ClientResponse latest() {
        return get().peek();
    }

    public Responses created() {

        return filter(CREATED);
    }

    public Responses filter(Status status) {

        final Responses responses = new Responses();

        for (ClientResponse response : get()) {

            if (status.getStatusCode() == response.getStatus()) {
                responses.get().addLast(response);
            }
        }

        return responses;
    }

    public void clear() {
        get().clear();
    }

    @Override
    public Iterator<ClientResponse> iterator() {
        return get().iterator();
    }
}

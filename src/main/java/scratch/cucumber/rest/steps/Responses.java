package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;

import java.util.ArrayDeque;
import java.util.Deque;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.CREATED;

public class Responses extends History<Responses, ClientResponse, Status> {

    public Responses() {
        this(new ArrayDeque<ClientResponse>());
    }

    public Responses(Deque<ClientResponse> responses) {
        super(responses, CREATED);
    }

    @Override
    public Responses filter(Status status) {

        final Responses responses = new Responses();

        for (ClientResponse response : get()) {

            if (status.getStatusCode() == response.getStatus()) {
                responses.get().addLast(response);
            }
        }

        return responses;
    }
}

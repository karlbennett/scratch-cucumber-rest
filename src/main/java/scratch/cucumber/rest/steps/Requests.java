package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientRequest;

import java.util.ArrayDeque;
import java.util.Deque;

import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;

public class Requests extends History<Requests, ClientRequest, String> {

    public Requests() {
        this(new ArrayDeque<ClientRequest>());
    }

    public Requests(Deque<ClientRequest> responses) {
        super(responses, POST);
    }

    public Requests updated() {

        return filter(PUT);
    }

    @Override
    public Requests filter(String method) {

        final Requests requests = new Requests();

        for (ClientRequest request : get()) {

            if (method.equals(request.getMethod())) {
                requests.get().addLast(request);
            }
        }

        return requests;
    }
}

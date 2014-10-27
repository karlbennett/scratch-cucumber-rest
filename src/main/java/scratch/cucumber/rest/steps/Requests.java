package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientRequest;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static javax.ws.rs.HttpMethod.POST;

public class Requests extends Holder<Deque<ClientRequest>> implements Iterable<ClientRequest> {

    public Requests() {
        this(new ArrayDeque<ClientRequest>());
    }

    public Requests(Deque<ClientRequest> responses) {
        super(responses);
    }

    public void add(ClientRequest response) {
        get().push(response);
    }

    public ClientRequest latest() {
        return get().peek();
    }

    public Requests created() {

        return filter(POST);
    }

    public Requests filter(String method) {

        final Requests requests = new Requests();

        for (ClientRequest request : get()) {

            if (method.equals(request.getMethod())) {
                requests.get().addLast(request);
            }
        }

        return requests;
    }

    public void clear() {
        get().clear();
    }

    @Override
    public Iterator<ClientRequest> iterator() {
        return get().iterator();
    }
}

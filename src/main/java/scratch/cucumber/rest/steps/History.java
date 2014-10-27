package scratch.cucumber.rest.steps;

import java.util.Deque;
import java.util.Iterator;

public abstract class History<H extends History, T, F> extends Holder<Deque<T>> implements Iterable<T> {

    private final F create;

    public History(Deque<T> responses, F create) {
        super(responses);

        this.create = create;
    }

    public void add(T response) {
        get().push(response);
    }

    public T latest() {
        return get().peek();
    }

    public H created() {

        return filter(create);
    }

    public abstract H filter(F filter);

    public void clear() {
        get().clear();
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }
}

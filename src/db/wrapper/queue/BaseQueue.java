package db.wrapper.queue;

import java.util.LinkedList;
import java.util.Queue;

public abstract class BaseQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    abstract String increaseSQL (Class clazz);
    public void offer (T s) {
        queue.offer(s);
    }
    public Integer size () {
        return queue.size();
    }
    public T poll () {
        return queue.poll();
    }
}

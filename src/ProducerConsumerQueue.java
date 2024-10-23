import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerQueue<T> {
    private final Queue<T> q = new LinkedList<>();
    private static final int SIZE = 10;
    private final Object lock = new Object();

    public void enqueue(T val) throws InterruptedException {
        synchronized (lock) {
            while (q.size() >= SIZE) {
                lock.wait();
            }
            q.add(val);
            lock.notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {
        synchronized (lock) {
            while (q.isEmpty()) {
                lock.wait();
            }
            T result = q.poll();
            lock.notifyAll();
            return result;
        }
    }
}

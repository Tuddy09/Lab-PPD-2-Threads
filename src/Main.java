import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void scalarProduct(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Vectors are of different sizes");
        }

        ProducerConsumerQueue<Integer> threadSafeQueue = new ProducerConsumerQueue<>();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < a.size(); i++) {
                    int pairProd = a.get(i) * b.get(i);
                    threadSafeQueue.enqueue(pairProd);
                }
                threadSafeQueue.enqueue(null);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            int sum = 0;
            try {
                while (true) {
                    Integer k = threadSafeQueue.dequeue();
                    if (k == null) {
                        break;
                    }
                    sum += k;
                }
                System.out.println(sum);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        List<Integer> matrix = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            matrix.add(1);
        }

        try {
            scalarProduct(matrix, matrix);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        List<Integer> matrix2 = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            matrix2.add(i);
        }

        try {
            scalarProduct(matrix2, matrix2);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

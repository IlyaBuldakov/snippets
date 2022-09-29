import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReentrantLock lock = new ReentrantLock();

        executor.submit(new Task(lock));
        executor.submit(new Task(lock));

        executor.shutdown();
    }
}

class Task implements Runnable {

    ReentrantLock lock;

    public Task(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("Поток " + Thread.currentThread().getName() + " зашёл в свой метод run()");
        if (lock.tryLock()) {
            System.out.println("Поток " + Thread.currentThread().getName() + " поставил лок и начал задачу");
            CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
                int num = 0;
                for (int i = 0; i < Integer.MAX_VALUE / 2; i++) {
                    num++;
                }
                System.out.println("Промежуточный результат от " + Thread.currentThread().getName() + " - " + num);
                return num;
            });
            CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
                int num = 0;
                for (int i = 0; i < Integer.MAX_VALUE / 4; i++) {
                    num += 2;
                }
                System.out.println("Промежуточный результат 2 " + Thread.currentThread().getName() + " - " + num);
                return num;
            });
            CompletableFuture<Integer> result = cf1.thenCombine(cf2, Integer::sum);
            MainWindow window = new MainWindow(result);
        } else {
            System.out.println("Поток " + Thread.currentThread().getName() + " не смог поставить лок. Делает другую задачу.");
            for (int i = 0; i < 100; i++) {
                System.out.println(i);
            }
        }
        System.out.println("Поток " + Thread.currentThread().getName() + " закончил свою работу.");
    }
}

class MainWindow extends JFrame {

    public MainWindow(CompletableFuture<Integer> cf1) {
        try {
            this.add(new JLabel(cf1.get().toString()));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.setVisible(true);
        this.pack();
        this.setPreferredSize(new Dimension(200, 100));
    }
}
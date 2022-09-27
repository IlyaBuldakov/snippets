import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

  // Синхронизатор со счётчиком операций.
  static CountDownLatch countDownLatch = new CountDownLatch(3);

  public static void main(String[] args) {
    // Создаем поток.
    CustomThread thread1 = new CustomThread(countDownLatch);

    // Запускаем поток. Он будет в состоянии ожидания.
    thread1.start();

    // Выполняем операции, каждая из которых уменьшает счётчик на 1.
    doWork1();
    doWork2();
    doWork3();
  }

  public static void doWork1() {
    try {
      Thread.sleep(200);
      System.out.println("Работа 1 произведена");
      countDownLatch.countDown();
      System.out.println("Счётчик - " + countDownLatch.getCount());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void doWork2() {
    try {
      Thread.sleep(200);
      System.out.println("Работа 2 произведена");
      countDownLatch.countDown();
      System.out.println("Счётчик - " + countDownLatch.getCount());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void doWork3() {
    try {
      Thread.sleep(200);
      System.out.println("Работа 3 произведена");
      countDownLatch.countDown();
      System.out.println("Счётчик - " + countDownLatch.getCount());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}

class CustomThread extends Thread {
  private final CountDownLatch countDownLatch;

  public CustomThread(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }

  // Ждём, пока счётчик станет 0 и начинаем выполнять полезную работу.
  @Override
  public void run() {
    try {
      countDownLatch.await();
      System.out.println("Начался метод run потока " + Thread.currentThread().getName());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
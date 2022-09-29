public class SynchronizedTest {
  public static void main(String[] args) {
    Thread thread1 = new Thread(SynchronizedTest::doWorkStatic);
    Thread thread2 = new Thread(SynchronizedTest::doWorkStatic);

    thread1.start();
    thread2.start();
  }

  public static void doWorkStatic() {
    System.out.println("Поток " + Thread.currentThread().getName() + " зашёл в метод doWorkStatic");
    synchronized (SynchronizedTest.class) {
      System.out.println("Поток " + Thread.currentThread().getName() + " начал работу...");
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("Поток " + Thread.currentThread().getName() + " закончил работу, освободил монитор");
    }
  }
}
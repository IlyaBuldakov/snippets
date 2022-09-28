public class MutexTest {

  final Object lock = new Object();

  public static void main(String[] args) {
    MutexTest instance = new MutexTest();
    Thread thread1 = new Thread(instance::doWork1);
    Thread thread2 = new Thread(instance::doWork2);

    thread1.start();
    thread2.start();
  }

  public void doWork1() {
    System.out.println("Поток " + Thread.currentThread().getName() + " зашел в метод doWork1");
    synchronized (lock) {
      System.out.println("Поток " + Thread.currentThread().getName() + " захватил mutex объекта lock");
      System.out.println("Hello from doWork1");
    }
  }

  public void doWork2() {
    System.out.println("Поток " + Thread.currentThread().getName() + " зашел в метод doWork2");
    synchronized (lock) {
      System.out.println("Поток " + Thread.currentThread().getName() + " захватил mutex объекта lock");
      System.out.println("Hello from doWork2");
    }
  }
}

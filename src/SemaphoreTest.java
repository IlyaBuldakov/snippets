import java.util.concurrent.Semaphore;

public class SemaphoreTest {

  public static void main(String[] args) {
    /*
    Объявляем семафор, который может пустить только два потока.
    С каждым впущенным потоком счётчик семафора уменьшается на 1.
    Когда он будет равен 0, это значит, что все остальные потоки,
    которые хотят сделать acquire() - будут приостановлены.
     */
    Semaphore semaphore = new Semaphore(2);

    // Объявляем потоки.
    MyThread thread1 = new MyThread(semaphore);
    MyThread thread2 = new MyThread(semaphore);
    MyThread thread3 = new MyThread(semaphore);
    MyThread thread4 = new MyThread(semaphore);

    // Запускаем потоки.
    thread1.start();
    thread2.start();
    thread3.start();
    thread4.start();
  }
}
class MyThread extends Thread {

  // Семафор, приходящий в конструкторе. Общий для всех 4 потоков.
  Semaphore semaphore;

  public MyThread(Semaphore semaphore) {
    this.semaphore = semaphore;
  }

  @Override
  public void run() {
    try {
      System.out.println(Thread.currentThread().getName() + " начал работать, ждет разрешения семафора");
      // Строка ниже - спрашиваем у семафора разрешение на выполнение.
      semaphore.acquire();

      /*
      Оказались тут - значит семафор нас пустил. Его счётчик убавился на единицу. То есть если он
      изначально был 3, то после того, как семафор пустил поток - счётчик стал 2 (теперь семафор
      может дать "зелёный свет" только 2-ум потокам.
       */

      // ------ Полезная работа потока ------
      System.out.println("Семафор пустил " + Thread.currentThread().getName() + ". Поток делает свою работу...");
      Thread.sleep(1500);
      System.out.println("Поток " + Thread.currentThread().getName() + " закончил свою работу.");
      // ------ Полезная работа потока ------

      // Освобождаем место. Говорим семафору что мы закончили. Семафор увеличивает свой счётчик на 1.
      semaphore.release();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}

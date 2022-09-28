import java.util.concurrent.Exchanger;

public class ExchangerTest {
  public static void main(String[] args) {
    // Создаем Exchanger, общий для двух потоков.
    Exchanger<String> ex = new Exchanger<>();

    // Создаем потоки.
    CustomThread0 thread1 = new CustomThread0(ex);
    CustomThread1 thread2 = new CustomThread1(ex);

    // Запускаем потоки.
    thread1.start();
    thread2.start();
  }
}

// Первый поток.
class CustomThread0 extends Thread {

  // Exchanger, приходящий в конструкторе.
  Exchanger<String> ex;

  public CustomThread0(Exchanger<String> ex) {
    this.ex = ex;
  }

  // Приветствие этого потока, которое пойдет в обменник другому.
  String greeting = "Hello from CustomThread0";

  // Поле для внедрения приветствия другого потока, пришедшее через обменник.
  String arrivedGreeting;

  @Override
  public void run() {
    try {
      arrivedGreeting = ex.exchange(greeting);
      // STOP. После строки выше поток приостанавливается, пока не произойдет обмен данными.
      System.out.print(Thread.currentThread().getName() + " said: ");
      // Вывод прибывшего через обменник сообщения.
      System.out.println(arrivedGreeting);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}

class CustomThread1 extends Thread {

  // Exchanger, приходящий в конструкторе.
  Exchanger<String> ex;

  // Приветствие этого потока, которое пойдет в обменник другому.
  String greeting = "Hello from CustomThread1";

  // Поле для внедрения приветствия другого потока, пришедшее через обменник.
  String arrivedGreeting;

  public CustomThread1(Exchanger<String> ex) {
    this.ex = ex;
  }

  @Override
  public void run() {
    try {
      arrivedGreeting = ex.exchange(greeting);
      // STOP. После строки выше поток приостанавливается, пока не произойдет обмен данными.
      System.out.print(Thread.currentThread().getName() + " said: ");
      // Вывод прибывшего через обменник сообщения.
      System.out.println(arrivedGreeting);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
public class Join {
  public static void main(String[] args) throws InterruptedException {

    // Инициализировали потоки с их задачами.
    Thread thread1 = new Thread(() -> System.out.println("Поток 1 оставил след в консоли"));
    Thread thread2 = new Thread(() -> System.out.println("Поток 2 оставил след в консоли"));
    Thread thread3 = new Thread(() -> System.out.println("Поток 3 оставил след в консоли"));

    // Запустили потоки.
    thread1.start();
    thread2.start();
    thread3.start();

    // Сказали о том, что эти потоки нужно подождать.
    thread1.join();
    thread2.join();
    thread3.join();

    /*
     Этот текст выведется только после
     завершения работы созданных нами потоков.
     */
    System.out.println("Метод main завершился");
  }
}

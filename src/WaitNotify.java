public class WaitNotify {
  public static void main(String[] args) {
    Depot depot = new Depot();

    // Создаём два потока, внедрив в них один объект депо.
    Worker worker = new Worker(depot);
    Consumer consumer = new Consumer(depot);

    // Запуск потоков.
    worker.start();
    consumer.start();
  }
}

// Склад (депо)
class Depot {

  /*
  Количество предметов на складе, при котором рабочий "уходит на перерыв",
  так как на складе имеется минимальное количество предметов. Consumer их забирает.
   */
  private static final int MIN_ITEMS = 3;

  // Количество предметов в депо.
  int itemsCount;

  /*
  Метод добавления предмета на склад (загрузка). Он (как и метод get) синхронизирован, а значит что тот поток,
  который первым вызовет put или get метод - займет mutex объекта склада. Отсюда следует, что в
  отношении склада одновременно может выполняться только одна операция одновременно (put или get)
   */
  public synchronized void put() throws InterruptedException {
    while (itemsCount >= MIN_ITEMS) {
      wait();
    }
    itemsCount++;
    System.out.println("Worker положил предмет на склад, теперь их: " + itemsCount);
    notify();
  }

  // Метод получения предмета со склада (разгрузка).
  public synchronized void get() throws InterruptedException {
    while (itemsCount == 0) {
      wait();
    }
    itemsCount--;
    System.out.println("Consumer заказал и выгрузил предмет со склада, теперь их: " + itemsCount);
    notify();
  }
}

// Рабочий (грузчик). Пополняет склад предметами.
class Worker extends Thread {

  // Кол-во предметов, которые доставит рабочий на склад.
  private static final int COUNT_OF_DELIVERIES = 15;

  // Склад.
  Depot depot;

  public Worker(Depot depot) {
    this.depot = depot;
  }

  @Override
  public void run() {
    try {
      for (int i = 0; i < COUNT_OF_DELIVERIES; i++) {
        depot.put();
      }
    } catch (InterruptedException e) {
      System.err.println("Worker вынуждено закончил свою работу");
    }
  }
}

// Потребитель. Заказывает предметы со склада.
class Consumer extends Thread {

  // Количество предметов, которое требуются потребителю (сколько предметов он закажет).
  private static final int COUNT_OF_ORDERS = 15;

  // Склад (депо).
  Depot depot;

  public Consumer(Depot depot) {
    this.depot = depot;
  }

  @Override
  public void run() {
    try {
      for (int i = 0; i < COUNT_OF_ORDERS; i++) {
        depot.get();
      }
    } catch (InterruptedException e) {
      System.err.println("У Consumer'а отпала потребность в заказах");
    }
  }
}

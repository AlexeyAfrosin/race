import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainClass {
    private static final int CARS_COUNT = 5;
    private static final int WAIT_THREAD_COUNT = CARS_COUNT + 1;
    private static AtomicBoolean isWinnerExist = new AtomicBoolean(false);

    private static class PrintStartRace implements Runnable {
        public void run() {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        }
    }

    private static class PrintEndRace implements Runnable {
        public void run() {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        }
    }

    public static void main(String[] args) {
        Semaphore tunnelSemaphore = new Semaphore(CARS_COUNT / 2);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        System.out.println(" >>> Подготовка трассы!!!");
        Race race = new Race(new Road(60), new Tunnel(tunnelSemaphore), new Road(40));

        System.out.println(" >>> Подготовка машин!!!");

        CyclicBarrier startLocker = new CyclicBarrier(WAIT_THREAD_COUNT, new PrintStartRace());
        CyclicBarrier finishLocker = new CyclicBarrier(WAIT_THREAD_COUNT, new PrintEndRace());


        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startLocker, finishLocker, isWinnerExist);
        }
        System.out.println(" >>> Подготовка завершена!!!");


        for (Car car : cars) {
            new Thread(car).start();
        }

        try {
            startLocker.await();
            finishLocker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


    }
}

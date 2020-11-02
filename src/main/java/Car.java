import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private AtomicBoolean isWinnerExist;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier startLocker;
    private CyclicBarrier finishLocker;


    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier startLocker, CyclicBarrier finishLocker, AtomicBoolean isWinnerExist) {
        this.race = race;
        this.speed = speed;
        this.startLocker = startLocker;
        this.finishLocker = finishLocker;
        this.isWinnerExist = isWinnerExist;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startLocker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        try {
            if (isWinnerExist.compareAndSet(false, true)) {
                System.out.println(" >>> ВАЖНОЕ ОБЪЯВЛЕНИЕ: " + this.name + " ФИНИШИРОВАЛ ПЕРВЫМ!!!!!!");
            }
            finishLocker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

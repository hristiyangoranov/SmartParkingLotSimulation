import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Car implements Runnable{
    final private String carId;
    private BlockingQueue<Car> waitingToEnter;
    private BlockingQueue<Car> waitingToLeave;
    private Semaphore isParked;
    private final Integer maxPark = 7000;
    private final Integer minPark = 2000;


    public Car(String carId, BlockingQueue<Car> waitingToEnter, BlockingQueue<Car> waitingToLeave){
        this.carId = carId;
        this.waitingToEnter = waitingToEnter;
        this.waitingToLeave = waitingToLeave;
        isParked = new Semaphore(0);
    }

    @Override
    public void run(){
        waitingToEnter.add(this);
        System.out.println("Car "+carId+" just arrived at the queue");

        try {
            isParked.acquire();

            Random r = new Random();
            Thread.sleep(r.nextInt(maxPark - minPark + 1) + minPark);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        waitingToLeave.add(this);
    }
    public Semaphore getisParked(){
        return isParked;
    }

    public String getId(){
        return carId;
    }
}

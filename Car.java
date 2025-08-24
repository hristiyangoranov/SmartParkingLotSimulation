import java.util.Random;
import java.util.concurrent.Semaphore;

public class Car implements Runnable{
    final private String carId;
    private Semaphore isParked;
    private Semaphore isReadyToLeave;
    private final Integer maxPark = 7000;
    private final Integer minPark = 2000;
    private final Boolean isVIP;


    public Car(String carId, boolean isVIP){
        this.carId = carId;
        isParked = new Semaphore(0);
        isReadyToLeave = new Semaphore(0);
        this.isVIP = isVIP;
    }

    public void park(ParkingLot lot){
        lot.addToWaitingToEnter(this);
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run(){
        System.out.println("Car "+carId+" just arrived at the queue");

        try {
            isParked.acquire();

            Random r = new Random();
            Thread.sleep(r.nextInt(maxPark - minPark + 1) + minPark);
            isReadyToLeave.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public Semaphore getisParked(){
        return isParked;
    }

    public Semaphore getIsReadyToLeave(){
        return isReadyToLeave;
    }

    public String getId(){
        return carId;
    }

    public Boolean getIsVip(){
        return isVIP;
    }
}

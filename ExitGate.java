import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExitGate implements Runnable{
    private String gateId;
    private BlockingQueue<Car> waitingToLeave;
    private AtomicBoolean isBeingUsed = new AtomicBoolean(false);
    private ParkingLot lot;

    public ExitGate(String gateId, BlockingQueue<Car> waitingToLeave, ParkingLot lot){
        this.gateId = gateId;
        this.waitingToLeave = waitingToLeave;
        this.lot = lot;
    }

    public synchronized void passACar(Car car){
        isBeingUsed.set(true);
        System.out.println("Exit gate "+gateId+" is starting to pass car " +car.getId());

        lot.removeACar(car);

        isBeingUsed.set(false);
    }

    @Override
    public void run(){
        while(true){
            try {
                Semaphore s = lot.getS();
                Car c = waitingToLeave.take();
                passACar(c);
                s.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

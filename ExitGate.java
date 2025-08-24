import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class ExitGate implements Runnable{
    private String gateId;
    private BlockingQueue<Car> waitingToLeave;
    private ParkingLot lot;

    public ExitGate(String gateId, BlockingQueue<Car> waitingToLeave, ParkingLot lot){
        this.gateId = gateId;
        this.waitingToLeave = waitingToLeave;
        this.lot = lot;
    }

    public synchronized void passACar(Car car){
        System.out.println("Exit gate "+gateId+" is starting to pass car " +car.getId());

        lot.removeACar(car);
    }

    @Override
    public void run(){
        while(true){
            Semaphore s = lot.getS();
            try {
                Car c = waitingToLeave.take();
                passACar(c);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally{
                s.release();
            }
        }
    }
}

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntryGate implements Runnable{
    private String gateId;
    private AtomicBoolean isBeingUsed = new AtomicBoolean(false);
    private ParkingLot lot;
    private BlockingQueue<Car> waitingToEnter; 


    public EntryGate(String gateId, BlockingQueue<Car> waitingToEnter, ParkingLot lot){
        this.gateId = gateId;
        this.waitingToEnter = waitingToEnter;
        this.lot = lot;
    }

    public synchronized void passACar(Car car){
        isBeingUsed.set(true);
        System.out.println("Entry gate "+gateId+" is starting to pass car " +car.getId());

        lot.parkCar(car);
        car.getisParked().release();

        isBeingUsed.set(false);
    }
    
    public boolean isBeingUsed(){
        return isBeingUsed.get();
    }

    @Override
    public void run(){
        while(true){
            Semaphore s = lot.getS();
            try{
                s.acquire();
                Car c = waitingToEnter.take();
                passACar(c);
                
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}

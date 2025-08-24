import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class EntryGate implements Runnable{
    private String gateId;
    private ParkingLot lot;
    private BlockingQueue<Car> waitingToEnter; 


    public EntryGate(String gateId, BlockingQueue<Car> waitingToEnter, ParkingLot lot){
        this.gateId = gateId;
        this.waitingToEnter = waitingToEnter;
        this.lot = lot;
    }

    public synchronized void passACar(Car car){
        System.out.println("Entry gate "+gateId+" is starting to pass car " +car.getId());

        lot.parkCar(car);

        car.getisParked().release(); //signals that the car is parked

        try {
            car.getIsReadyToLeave().acquire();
            lot.addToWaitingToLeave(car);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally{
            car.getIsReadyToLeave().release();
        }
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

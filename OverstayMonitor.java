import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

public class OverstayMonitor implements Runnable{
    private ParkingLot lot;

    public OverstayMonitor(ParkingLot lot){
        this.lot = lot;
    }

    @Override
    public void run(){
        while(true){
            ConcurrentHashMap<String, Ticket> tickets = lot.getTickets();
            Integer maxStay = lot.getMaxStay();
            LocalTime now = LocalTime.now();
            for(String s : tickets.keySet()){
                LocalTime entryTime = tickets.get(s).entryTime();
                Duration d = Duration.between(entryTime, now);
                if(d.getSeconds()>maxStay){
                    lot.getFinesIssued().incrementAndGet();
                    System.out.println("Car "+s+" is issued a fine");
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

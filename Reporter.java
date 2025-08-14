public class Reporter implements Runnable{
    private ParkingLot lot;
    
    public Reporter(ParkingLot lot){
        this.lot = lot;
    }

    @Override
    public void run(){
        while(true){
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("----------------------------");
            System.out.println("REPORT");
            System.out.println("Fines isued: "+lot.getFinesIssued());
            System.out.println("Cars served: "+lot.getCarsServed());
            System.out.println("Cars currently parked: "+lot.getCurrentlyParked());
            System.out.println("----------------------------");
        }
    }
}

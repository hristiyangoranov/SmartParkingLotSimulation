import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLot {
    private final int capacity = 2;
    private List<EntryGate> entryGates;
    private List<ExitGate> exitGates;
    private ConcurrentHashMap<String, Ticket> tickets;
    private ConcurrentHashMap<Integer, String> takenSpots;
    private PriorityBlockingQueue<Car> waitingToEnter;
    private PriorityBlockingQueue<Car> waitingToLeave; 
    private Semaphore s;
    private final Integer maxStay = 3;
    private AtomicInteger currentlyParked = new AtomicInteger(0);
    private AtomicInteger finesIssued = new AtomicInteger(0);
    private AtomicInteger carsServed = new AtomicInteger(0);
    private Reporter reporter;
    private OverstayMonitor overstayMonitor;

    public ParkingLot(PriorityBlockingQueue<Car> waitingToEnter, PriorityBlockingQueue<Car> waitingToLeave){
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
        this.tickets = new ConcurrentHashMap<>();
        this.takenSpots = new ConcurrentHashMap<>();
        for(int i=1;i<=capacity;i++){
            takenSpots.put(i, "");
        }
        s = new Semaphore(capacity);
        this.waitingToEnter = waitingToEnter;
        this.waitingToLeave = waitingToLeave;
        reporter = new Reporter(this);
        overstayMonitor = new OverstayMonitor(this);
    }

    public void addEntryGate(EntryGate gate){
        entryGates.add(gate);
    }

    public void addExitGate(ExitGate gate){
        exitGates.add(gate);
    }

    public void open(){
        if(entryGates.size() == 0){
            throw new GatesNotSufficientException("There needs to be at least one entry gate");
        }
        if(exitGates.size() == 0){
            throw new GatesNotSufficientException("There needs to be at least one exit gate");
        }
        for(EntryGate gate : entryGates){
            Thread t = new Thread(gate);
            t.start();
        }
        for(ExitGate gate : exitGates){
            Thread t = new Thread(gate);
            t.start();
        }
        Thread t1 = new Thread(this.reporter);
        t1.start();

        Thread t2 = new Thread(overstayMonitor);
        t2.start();
    }


    public void parkCar(Car car){
        synchronized(this){
            currentlyParked.incrementAndGet();
            carsServed.incrementAndGet();
            for(Integer i=1;i<=capacity;i++){
                if(takenSpots.get(i).equals("")){
                    Ticket t = new Ticket(i, LocalTime.now());
                    takenSpots.put(i, car.getId());
                    tickets.put(car.getId(), t);
                    System.out.println("Car "+car.getId()+" is parked at spot "+i);
                    break;
                }
            }
        }
    }

    public void removeACar(Car car){
        currentlyParked.decrementAndGet();
        int takenSpot = tickets.get(car.getId()).parkingSlotNumber();
        takenSpots.put(takenSpot, "");
        tickets.remove(car.getId());
        System.out.println("Car "+car.getId()+" is no longer in the park");
    }

    public Semaphore getS(){
        return s;
    }

    public ConcurrentHashMap<String, Ticket> getTickets(){
        return tickets;
    }

    public Integer getMaxStay(){
        return maxStay;
    }

    public AtomicInteger getFinesIssued(){
        return finesIssued;
    }

    public Integer getCarsServed(){
        return carsServed.get();
    }

    public Integer getCurrentlyParked(){
        return currentlyParked.get();
    }

    public void addToWaitingToEnter(Car c){
        waitingToEnter.add(c);
    }

    public void addToWaitingToLeave(Car c){
        waitingToLeave.add(c);
    }
}

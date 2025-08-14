import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main{
    public static void main(String[] args) {
        BlockingQueue<Car> waitingToEnter = new ArrayBlockingQueue<>(5);
        BlockingQueue<Car> waitingToLeave = new ArrayBlockingQueue<>(5);

        ParkingLot lot = new ParkingLot();

        EntryGate entryGate1 = new EntryGate("1", waitingToEnter, lot);
        EntryGate entryGate2 = new EntryGate("2", waitingToEnter, lot);

        ExitGate exitGate1 = new ExitGate("1", waitingToLeave, lot);
        ExitGate exitGate2 = new ExitGate("2", waitingToLeave, lot);

        lot.addEntryGate(entryGate1);
        lot.addEntryGate(entryGate2);
        lot.addExitGate(exitGate1);
        lot.addExitGate(exitGate2);

        lot.open();

        OverstayMonitor monitor = new OverstayMonitor(lot);
        Thread m1 = new Thread(monitor);
        m1.start();

        Car c1 = new Car("1", waitingToEnter, waitingToLeave);
        Car c2 = new Car("2", waitingToEnter, waitingToLeave);
        Car c3 = new Car("3", waitingToEnter, waitingToLeave);
        Car c4 = new Car("4", waitingToEnter, waitingToLeave);

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        Thread t3 = new Thread(c3);
        Thread t4 = new Thread(c4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Reporter r = new Reporter(lot);
        Thread r1 = new Thread(r);
        r1.start();
    }
}
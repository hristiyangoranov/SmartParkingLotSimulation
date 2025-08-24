import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class Main{
    public static void main(String[] args) {
        Comparator<Car> c = Comparator.comparing(Car::getIsVip).reversed();
        PriorityBlockingQueue<Car> waitingToEnter = new PriorityBlockingQueue<>(5, c);
        PriorityBlockingQueue<Car> waitingToLeave = new PriorityBlockingQueue<>(5, c);

        ParkingLot lot = new ParkingLot(waitingToEnter, waitingToLeave);

        EntryGate entryGate1 = new EntryGate("1", waitingToEnter, lot);
        EntryGate entryGate2 = new EntryGate("2", waitingToEnter, lot);

        ExitGate exitGate1 = new ExitGate("1", waitingToLeave, lot);
        ExitGate exitGate2 = new ExitGate("2", waitingToLeave, lot);

        lot.addEntryGate(entryGate1);
        lot.addEntryGate(entryGate2);
        lot.addExitGate(exitGate1);
        lot.addExitGate(exitGate2);

        try {
            lot.open();
        } catch (GatesNotSufficientException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        Car c1 = new Car("1", true);
        Car c2 = new Car("2", false);
        Car c3 = new Car("3", true);
        Car c4 = new Car("4", false);

        c1.park(lot);
        c2.park(lot);
        c3.park(lot);
        c4.park(lot);

    }
}
import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public synchronized void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public synchronized boolean allocateRoom(String type) {
        int available = rooms.getOrDefault(type, 0);
        if (available > 0) {
            rooms.put(type, available - 1);
            return true;
        }
        return false;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

class BookingProcessor extends Thread {
    private BookingRequestQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingRequestQueue queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getRequest();
            }

            if (r != null) {
                boolean success;
                synchronized (inventory) {
                    success = inventory.allocateRoom(r.getRoomType());
                }

                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " Confirmed: " + r.getGuestName() + " -> " + r.getRoomType());
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " Failed: " + r.getGuestName());
                }
            }
        }
    }
}

public class UseCaseHotelBookingApp {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("Diana", "Single"));

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();
    }
}
import java.util.*;

class Booking {
    String bookingId;
    String roomType;
    String roomId;
    boolean isCancelled;

    Booking(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }
}

public class UseCaseHotelBookingApp {

    static Map<String, Integer> inventory = new HashMap<>();
    static Map<String, List<String>> roomPool = new HashMap<>();
    static Map<String, Booking> bookings = new HashMap<>();
    static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {

        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        roomPool.put("Deluxe", new ArrayList<>(Arrays.asList("D1", "D2")));
        roomPool.put("Suite", new ArrayList<>(Arrays.asList("S1")));

        createBooking("B101", "Deluxe");
        createBooking("B102", "Suite");

        System.out.println("\n--- Before Cancellation ---");
        printState();

        cancelBooking("B101");

        System.out.println("\n--- After Cancellation ---");
        printState();

        cancelBooking("B999");
        cancelBooking("B101");
    }

    static void createBooking(String bookingId, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("No rooms available for " + roomType);
            return;
        }

        List<String> rooms = roomPool.get(roomType);
        String allocatedRoom = rooms.remove(0);

        inventory.put(roomType, inventory.get(roomType) - 1);

        Booking booking = new Booking(bookingId, roomType, allocatedRoom);
        bookings.put(bookingId, booking);

        System.out.println("Booking successful: " + bookingId + " -> Room " + allocatedRoom);
    }

    static void cancelBooking(String bookingId) {

        System.out.println("\nProcessing cancellation for: " + bookingId);

        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (booking.isCancelled) {
            System.out.println("Cancellation failed: Booking already cancelled.");
            return;
        }

        rollbackStack.push(booking.roomId);

        inventory.put(booking.roomType, inventory.get(booking.roomType) + 1);

        roomPool.get(booking.roomType).add(booking.roomId);

        booking.isCancelled = true;

        System.out.println("Cancellation successful for booking: " + bookingId);
    }

    static void printState() {
        System.out.println("Inventory: " + inventory);
        System.out.println("Room Pool: " + roomPool);

        System.out.println("Bookings:");
        for (Booking b : bookings.values()) {
            System.out.println(
                    b.bookingId + " | " + b.roomType + " | " + b.roomId +
                            " | Cancelled: " + b.isCancelled
            );
        }

        System.out.println("Rollback Stack: " + rollbackStack);
    }
}
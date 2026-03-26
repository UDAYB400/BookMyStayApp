import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailable(String type) {
        return rooms.getOrDefault(type, -1);
    }

    public void reduceRoom(String type) throws InvalidBookingException {
        int available = getAvailable(type);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }
        rooms.put(type, available - 1);
    }

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }
}

class BookingService {
    private Inventory inventory;

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {
        try {
            if (!inventory.isValidRoomType(r.getRoomType())) {
                throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
            }

            inventory.reduceRoom(r.getRoomType());

            System.out.println("Booking Confirmed: " + r.getGuestName() + " -> " + r.getRoomType());
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

public class UseCaseHotelBookingApp {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 0);

        BookingService service = new BookingService(inventory);

        service.processBooking(new Reservation("Alice", "Single"));
        service.processBooking(new Reservation("Bob", "Double"));
        service.processBooking(new Reservation("Charlie", "Suite"));
    }
}
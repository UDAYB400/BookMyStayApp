import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " - " + guestName + " - " + roomType;
    }
}

class Inventory implements Serializable {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public String toString() {
        return rooms.toString();
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public String toString() {
        return history.toString();
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_data.ser";

    public void save(Inventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("Data saved successfully");
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    public Object[] load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Inventory inventory = (Inventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("Data loaded successfully");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("No previous data found, starting fresh");
            return new Object[]{new Inventory(), new BookingHistory()};
        }
    }
}

public class UseCaseHotelBookingApp {
    public static void main(String[] args) {
        PersistenceService service = new PersistenceService();

        Object[] data = service.load();
        Inventory inventory = (Inventory) data[0];
        BookingHistory history = (BookingHistory) data[1];

        if (inventory.getRooms().isEmpty()) {
            inventory.addRoom("Single", 2);
            inventory.addRoom("Double", 1);
        }

        history.addReservation(new Reservation("R101", "Alice", "Single"));
        history.addReservation(new Reservation("R102", "Bob", "Double"));

        System.out.println("Inventory: " + inventory);
        System.out.println("Booking History: " + history);

        service.save(inventory, history);
    }
}
import java.util.*;

// Room Domain Model
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int available = inventory.getAvailability(type);

            // Validation: Only show available rooms
            if (available > 0 && roomData.containsKey(type)) {
                Room room = roomData.get(type);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: $" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available Count: " + available);
                System.out.println("---------------------------");
            }
        }
    }
}

// Main Class (Entry Point)
public class UseCaseHotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);   // Will be filtered out
        inventory.addRoom("Suite", 2);

        // Step 2: Create Room Data
        Map<String, Room> roomData = new HashMap<>();

        roomData.put("Single", new Room("Single", 1000,
                Arrays.asList("WiFi", "TV")));

        roomData.put("Double", new Room("Double", 2000,
                Arrays.asList("WiFi", "TV", "AC")));

        roomData.put("Suite", new Room("Suite", 5000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 3: Search Service (Read-only)
        SearchService searchService = new SearchService(inventory, roomData);

        // Step 4: Guest initiates search
        searchService.searchAvailableRooms();
    }
}
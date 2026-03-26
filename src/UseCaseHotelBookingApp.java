import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        System.out.println("Services for Reservation " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println(s);
        }
        System.out.println("Total Add-On Cost: $" + calculateTotalCost(reservationId));
    }
}

public class UseCaseHotelBookingApp {
    public static void main(String[] args) {
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Suite");

        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService breakfast = new AddOnService("Breakfast", 200);
        AddOnService spa = new AddOnService("Spa", 500);

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(r1.getReservationId(), wifi);
        manager.addService(r1.getReservationId(), breakfast);

        manager.addService(r2.getReservationId(), spa);
        manager.addService(r2.getReservationId(), breakfast);

        manager.displayServices(r1.getReservationId());
        System.out.println();
        manager.displayServices(r2.getReservationId());
    }
}
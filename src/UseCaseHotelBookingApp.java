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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("Booking History:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> reservations) {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            String type = r.getRoomType();
            summary.put(type, summary.getOrDefault(type, 0) + 1);
        }

        System.out.println("\nBooking Summary:");
        for (String type : summary.keySet()) {
            System.out.println(type + ": " + summary.get(type));
        }
    }
}

public class UseCaseHotelBookingApp {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("R101", "Alice", "Single"));
        history.addReservation(new Reservation("R102", "Bob", "Suite"));
        history.addReservation(new Reservation("R103", "Charlie", "Single"));
        history.addReservation(new Reservation("R104", "Diana", "Double"));

        BookingReportService reportService = new BookingReportService();

        reportService.displayAllBookings(history.getAllReservations());
        reportService.generateSummary(history.getAllReservations());
    }
}
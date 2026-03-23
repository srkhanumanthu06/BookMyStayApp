import java.util.*;

// ---------- Room Domain ----------
abstract class Room {
    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() { super("Single Room", 1, 2000); }
}

class DoubleRoom extends Room {
    DoubleRoom() { super("Double Room", 2, 3500); }
}

class SuiteRoom extends Room {
    SuiteRoom() { super("Suite Room", 3, 6000); }
}

// ---------- UC3: Inventory ----------
class RoomInventory {
    private HashMap<String, Integer> availability;

    RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 2);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    void decrement(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            availability.put(roomType, current - 1);
        }
    }

    void displayInventory() {
        System.out.println("===== Inventory =====");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// ---------- UC4: Search ----------
class SearchService {
    void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {
        System.out.println("\n===== Available Rooms =====\n");
        for (Room room : rooms) {
            int available = inventory.getAvailability(room.type);
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// ---------- UC5: Reservation ----------
class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------- UC5: Queue ----------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    void addRequest(Reservation r) {
        queue.offer(r);
    }

    Reservation getNext() {
        return queue.poll();
    }

    boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------- UC6: Booking Service ----------
class BookingService {

    private Map<String, Integer> roomCounters = new HashMap<>();

    String generateRoomId(String roomType) {
        int count = roomCounters.getOrDefault(roomType, 0) + 1;
        roomCounters.put(roomType, count);
        return roomType.substring(0, 2).toUpperCase() + "-" + count;
    }

    void processBookings(BookingQueue queue,
                         RoomInventory inventory,
                         BookingHistory history) {

        System.out.println("\n===== Processing Bookings =====\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNext();

            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                String roomId = generateRoomId(r.roomType);

                inventory.decrement(r.roomType);

                history.add(r); // ✅ UC8: record confirmed booking

                System.out.println("Booking Confirmed!");
                System.out.println("Reservation ID: " + r.reservationId);
                System.out.println("Guest: " + r.guestName);
                System.out.println("Room Type: " + r.roomType);
                System.out.println("Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("Booking Failed (No Availability)");
                System.out.println("Reservation ID: " + r.reservationId);
                System.out.println("Guest: " + r.guestName);
                System.out.println("Requested: " + r.roomType);
                System.out.println();
            }
        }
    }
}

// ---------- UC7: Add-On Service ----------
class AddOnService {
    String name;
    double price;

    AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    void display() {
        System.out.println(name + " - ₹" + price);
    }
}

// ---------- UC7: Add-On Service Manager ----------
class AddOnServiceManager {

    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for " + reservationId);
            return;
        }

        System.out.println("\nAdd-On Services for " + reservationId + ":");
        for (AddOnService s : services) {
            s.display();
        }
    }

    double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.price;
        }
        return total;
    }
}

// ---------- UC8: Booking History ----------
class BookingHistory {

    private List<Reservation> confirmedBookings = new ArrayList<>();

    void add(Reservation r) {
        confirmedBookings.add(r);
    }

    List<Reservation> getAllBookings() {
        return confirmedBookings;
    }

    void displayAll() {
        System.out.println("\n===== Booking History =====");
        for (Reservation r : confirmedBookings) {
            System.out.println("Reservation ID: " + r.reservationId +
                    ", Guest: " + r.guestName +
                    ", Room: " + r.roomType);
        }
    }
}

// ---------- UC8: Booking Report Service ----------
class BookingReportService {

    void generateSummary(BookingHistory history) {

        System.out.println("\n===== Booking Summary Report =====");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : history.getAllBookings()) {
            roomCount.put(r.roomType,
                    roomCount.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : roomCount.keySet()) {
            System.out.println(type + " booked: " + roomCount.get(type));
        }
    }
}

// ---------- Main ----------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====\n");

        // UC2: Room Setup
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // UC3: Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        // UC4: Search
        SearchService search = new SearchService();
        search.searchAvailableRooms(rooms, inventory);

        // UC5: Booking Queue
        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("R1", "Alice", "Single Room"));
        queue.addRequest(new Reservation("R2", "Bob", "Single Room"));
        queue.addRequest(new Reservation("R3", "Charlie", "Single Room"));
        queue.addRequest(new Reservation("R4", "David", "Suite Room"));

        // UC8: Booking History
        BookingHistory history = new BookingHistory();

        // UC6: Process Bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory, history);

        // UC7: Add-On Services
        AddOnServiceManager addOnManager = new AddOnServiceManager();
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa", 1500);
        AddOnService pickup = new AddOnService("Airport Pickup", 800);

        addOnManager.addService("R1", breakfast);
        addOnManager.addService("R1", spa);
        addOnManager.addService("R2", pickup);

        addOnManager.displayServices("R1");
        addOnManager.displayServices("R2");

        System.out.println("\nTotal Add-On Cost for R1: ₹" +
                addOnManager.calculateTotalCost("R1"));
        System.out.println("Total Add-On Cost for R2: ₹" +
                addOnManager.calculateTotalCost("R2"));

        // UC8: Display Booking History
        history.displayAll();

        // UC8: Generate Report
        BookingReportService reportService = new BookingReportService();
        reportService.generateSummary(history);

        // Final Inventory
        System.out.println("\n===== Final Inventory =====");
        inventory.displayInventory();
    }
}
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
        availability.put("Single Room", 5);
        availability.put("Double Room", 3);
        availability.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    void updateAvailability(String roomType, int count) {
        availability.put(roomType, count);
    }

    void displayInventory() {
        System.out.println("===== Centralized Room Inventory =====\n");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> Available: " + availability.get(type));
        }
    }
}

// ---------- UC4: Search (Read-only) ----------
class SearchService {
    void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {
        System.out.println("===== Available Rooms =====\n");
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
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// ---------- UC5: Booking Queue ----------
class BookingQueue {
    private Queue<Reservation> queue;

    BookingQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation r) {
        queue.offer(r); // FIFO
        System.out.println("Request added to queue:");
        r.display();
        System.out.println();
    }

    void viewQueue() {
        System.out.println("===== Booking Request Queue =====\n");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// ---------- Main ----------
public class BookMyStayApp {

    public static void main(String[] args) {

        // UC1: Welcome
        System.out.println("======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("======================================");
        System.out.println("Application: Hotel Booking Management System\n");

        // UC2: Rooms
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = { single, doubleRoom, suite };

        // UC3: Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        // UC4: Search
        SearchService search = new SearchService();
        System.out.println("\nGuest searching rooms...\n");
        search.searchAvailableRooms(rooms, inventory);

        // UC5: Booking Requests (FIFO Queue)
        BookingQueue bookingQueue = new BookingQueue();

        System.out.println("Guests submitting booking requests...\n");

        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        bookingQueue.viewQueue();
    }
}
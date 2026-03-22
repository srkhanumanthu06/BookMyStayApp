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
        availability.put(roomType, getAvailability(roomType) - 1);
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
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
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

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    String generateRoomId(String roomType, int count) {
        return roomType.substring(0, 2).toUpperCase() + "-" + count;
    }

    void processBookings(BookingQueue queue, RoomInventory inventory) {

        System.out.println("\n===== Processing Bookings =====\n");

        int idCounter = 1;

        while (!queue.isEmpty()) {

            Reservation r = queue.getNext();

            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                String roomId = generateRoomId(r.roomType, idCounter++);

                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    roomAllocations
                            .computeIfAbsent(r.roomType, k -> new HashSet<>())
                            .add(roomId);

                    inventory.decrement(r.roomType);

                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest: " + r.guestName);
                    System.out.println("Room Type: " + r.roomType);
                    System.out.println("Room ID: " + roomId);
                    System.out.println();

                }

            } else {
                System.out.println("Booking Failed (No Availability)");
                System.out.println("Guest: " + r.guestName);
                System.out.println("Requested: " + r.roomType);
                System.out.println();
            }
        }
    }
}

// ---------- Main ----------
public class BookMyStayApp {

    public static void main(String[] args) {

        // UC1
        System.out.println("===== Book My Stay App =====\n");

        // UC2
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // UC3
        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        // UC4
        SearchService search = new SearchService();
        search.searchAvailableRooms(rooms, inventory);

        // UC5
        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // exceeds
        queue.addRequest(new Reservation("David", "Suite Room"));

        // UC6
        BookingService service = new BookingService();
        service.processBookings(queue, inventory);

        System.out.println("\n===== Final Inventory =====");
        inventory.displayInventory();
    }
}
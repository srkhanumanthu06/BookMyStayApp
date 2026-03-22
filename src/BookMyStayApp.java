
//Use Case 1, 2 & 3: Entry + Room Types + Centralized Inventory

import java.util.HashMap;

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
    SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

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

public class BookMyStayApp {

    public static void main(String[] args) {

        // UC1: Welcome Message
        System.out.println("======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("======================================");
        System.out.println("Application: Hotel Booking Management System");
        System.out.println("Version: 1.0");
        System.out.println("System started successfully!\n");

        // UC2: Room Details
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("===== Room Details =====\n");

        single.displayDetails();
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println();

        suite.displayDetails();
        System.out.println();

        // UC3: Inventory
        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Update:\n");
        inventory.displayInventory();
    }
}
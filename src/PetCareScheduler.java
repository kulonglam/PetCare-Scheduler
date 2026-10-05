import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Main app to run the Pet Care Scheduler.
 */
public class PetCareScheduler {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Pet> pets = new HashMap<>();              // Task 2
    private static List<Appointment> appointments = new ArrayList<>();   // Task 2

    public static void main(String[] args) {
        loadDataFromFile();
        boolean running = true;
        while (running) {
            System.out.println("\n=== Pet Care Scheduler ===");
            System.out.println("1. Register Pet");
            System.out.println("2. Schedule Appointment");
            System.out.println("3. Store Data");
            System.out.println("4. Display Records");
            System.out.println("5. Generate Reports");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerPet();
                    break;
                case "2":
                    scheduleAppointment();
                    break;
                case "3":
                    saveDataToFile();
                    break;
                case "4":
                    displayRecords();
                    break;
                case "5":
                    generateReports();
                    break;
                case "6":
                    saveDataToFile();
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        }
    }

    // Task 3
    private static void registerPet() {
        // Prompt the user to enter a unique pet ID
        System.out.print("Enter pet ID: ");
        String id = scanner.nextLine().trim();

        // Reject empty or duplicate IDs
        if (id.isEmpty()) {
            System.out.println("Error: Pet ID cannot be empty.");
            return;
        }
        if (pets.containsKey(id)) {
            System.out.println("Error: Pet ID already exists.");
            return;
        }

        System.out.print("Enter pet name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter species/breed: ");
        String species = scanner.nextLine().trim();

        int age = 0;

        // Loop until a valid age is entered
        while (true) {
            try {
                System.out.print("Enter age in years: ");
                age = Integer.parseInt(scanner.nextLine().trim());

                // Check that age is not negative
                if (age < 0) throw new IllegalArgumentException();

                break;  // Exit loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Must be a whole number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid age. Cannot be negative.");
            }
        }

        System.out.print("Enter owner name: ");
        String owner = scanner.nextLine().trim();

        System.out.print("Enter contact info: ");
        String contact = scanner.nextLine().trim();

        // Create the pet and add it to the pets map (using ID as the key)
        Pet pet = new Pet(id, name, species, age, owner, contact);
        pets.put(id, pet);

        System.out.println("Pet registered successfully on " + pet.getRegistrationDate());
    }

    // Task 4
    private static void scheduleAppointment() {
        // Ask the user for the pet ID
        System.out.print("Enter pet ID: ");
        String id = scanner.nextLine().trim();

        // Look up the pet in the map by ID
        Pet pet = pets.get(id);

        // If pet not found, show error and exit
        if (pet == null) {
            // Task 8
            System.out.println("Error: Pet ID not found.");
            return;
        }

        // Ask for and validate the appointment type
        System.out.print("Enter appointment type (vet visit/vaccination/grooming): ");
        String type = Appointment.normalizeType(scanner.nextLine());
        if (type == null) {
            System.out.println("Error: Invalid appointment type.");
            return;
        }

        LocalDateTime dateTime = null;

        // Loop until a valid future date and time is entered
        while (true) {
            try {
                System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
                dateTime = LocalDateTime.parse(scanner.nextLine().trim(), Appointment.FORMAT);

                // Check that the appointment is in the future
                if (!dateTime.isAfter(LocalDateTime.now())) {
                    System.out.println("Invalid date. The appointment must be in the future.");
                    continue;
                }

                break;  // Exit loop if input is valid
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Example: 2030-12-01 14:30");
            }
        }

        // Notes are optional; the user can just press Enter
        System.out.print("Enter notes (optional): ");
        String notes = scanner.nextLine().trim();

        // Create the appointment and add it to the pet and the global list
        Appointment appointment = new Appointment(id, type, dateTime, notes);
        pet.addAppointment(appointment);
        appointments.add(appointment);

        System.out.println("Appointment scheduled for " + pet.getName() + ".");
    }

    // Task 6
    private static void displayRecords() {
        System.out.println("\n1. All registered pets");
        System.out.println("2. All appointments for a specific pet");
        System.out.println("3. Upcoming appointments for all pets");
        System.out.println("4. Past appointment history for each pet");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        LocalDateTime now = LocalDateTime.now();

        switch (choice) {
            case "1":
                if (pets.isEmpty()) {
                    System.out.println("No pets registered.");
                    return;
                }
                System.out.println("\nRegistered Pets:");
                for (Pet p : pets.values()) {
                    System.out.println(p);
                }
                break;

            case "2":
                System.out.print("Enter pet ID: ");
                Pet pet = pets.get(scanner.nextLine().trim());
                if (pet == null) {
                    System.out.println("Pet not found.");
                    return;
                }
                System.out.println("\nAppointments for " + pet.getName() + ":");
                if (pet.getAppointments().isEmpty()) {
                    System.out.println("No appointments scheduled.");
                } else {
                    List<Appointment> sorted = new ArrayList<>(pet.getAppointments());
                    Collections.sort(sorted);
                    for (Appointment a : sorted) {
                        System.out.println(a + "\n");
                    }
                }
                break;

            case "3":
                List<Appointment> upcoming = new ArrayList<>();
                for (Appointment a : appointments) {
                    if (a.getDateTime().isAfter(now)) {
                        upcoming.add(a);
                    }
                }
                Collections.sort(upcoming);
                System.out.println("\nUpcoming Appointments:");
                if (upcoming.isEmpty()) {
                    System.out.println("No upcoming appointments.");
                }
                for (Appointment a : upcoming) {
                    System.out.println("Pet: " + pets.get(a.getPetId()).getName() + "\n" + a + "\n");
                }
                break;

            case "4":
                if (pets.isEmpty()) {
                    System.out.println("No pets registered.");
                    return;
                }
                for (Pet p : pets.values()) {
                    System.out.println("\nPast appointments for " + p.getName() + ":");
                    List<Appointment> past = new ArrayList<>();
                    for (Appointment a : p.getAppointments()) {
                        if (!a.getDateTime().isAfter(now)) {
                            past.add(a);
                        }
                    }
                    Collections.sort(past);
                    if (past.isEmpty()) {
                        System.out.println("No past appointments.");
                    }
                    for (Appointment a : past) {
                        System.out.println(a + "\n");
                    }
                }
                break;

            default:
                System.out.println("Invalid choice. Please select 1-4.");
        }
    }

    // Task 7
    private static void generateReports() {
        // Check if there are any pets registered
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusWeeks(1);
        LocalDateTime sixMonthsAgo = now.minusMonths(6);

        // ------------------------------
        // Pets with appointments in the next week
        // ------------------------------
        System.out.println("\nPets with Appointments in the Next 7 Days:");
        boolean foundUpcoming = false;
        for (Pet p : pets.values()) {
            for (Appointment a : p.getAppointments()) {
                if (a.getDateTime().isAfter(now) && !a.getDateTime().isAfter(nextWeek)) {
                    System.out.println(p.getName() + " (ID: " + p.getPetId() + ") - "
                            + a.getAppointmentType() + " on " + a.getDateTime().format(Appointment.FORMAT));
                    foundUpcoming = true;
                }
            }
        }
        if (!foundUpcoming) {
            System.out.println("None.");
        }

        // ------------------------------
        // Pets overdue for a vet visit (no vet visit in the last 6 months)
        // ------------------------------
        System.out.println("\nPets Overdue for a Vet Visit:");
        boolean foundOverdue = false;
        for (Pet p : pets.values()) {
            boolean recentVisit = false;
            for (Appointment a : p.getAppointments()) {
                if (a.getAppointmentType().equals("Vet Visit")
                        && a.getDateTime().isAfter(sixMonthsAgo)
                        && !a.getDateTime().isAfter(now)) {
                    recentVisit = true;
                    break;
                }
            }
            if (!recentVisit) {
                System.out.println(p.getName() + " (ID: " + p.getPetId() + "), Owner: "
                        + p.getOwnerName() + ", Contact: " + p.getContactInfo());
                foundOverdue = true;
            }
        }
        if (!foundOverdue) {
            System.out.println("None.");
        }
    }

    // Task 5
    private static void saveDataToFile() {
        try {
            // Write both the pets map and the appointments list to "petcare.ser"
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("petcare.ser")
            );
            out.writeObject(pets);
            out.writeObject(appointments);
            out.close();
            System.out.println("Data saved.");
        } catch (IOException e) {
            // Task 8
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked") // Suppresses unchecked cast warning when reading the objects
    private static void loadDataFromFile() {
        try (
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("petcare.ser"))
        ) {
            // Objects must be read in the same order they were written
            pets = (Map<String, Pet>) in.readObject();
            appointments = (List<Appointment>) in.readObject();
            System.out.println("Pet care data loaded.");
        } catch (FileNotFoundException e) {
            // Task 8
            System.out.println("No saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

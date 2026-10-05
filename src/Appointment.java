//Task 1
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single appointment for a pet.
 */
public class Appointment implements Serializable, Comparable<Appointment> {
    private static final long serialVersionUID = 1L;

    public static final String[] VALID_TYPES = {"Vet Visit", "Vaccination", "Grooming"};
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String petId;
    private String appointmentType;
    private LocalDateTime dateTime;
    private String notes; // optional

    public Appointment(String petId, String appointmentType, LocalDateTime dateTime, String notes) {
        this.petId = petId;
        this.appointmentType = appointmentType;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    // Returns the properly capitalized type if valid, otherwise null
    public static String normalizeType(String input) {
        for (String type : VALID_TYPES) {
            if (type.equalsIgnoreCase(input.trim())) {
                return type;
            }
        }
        return null;
    }

    public String getPetId() {
        return petId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Sort appointments from earliest to latest
    @Override
    public int compareTo(Appointment other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    @Override
    public String toString() {
        return "Type: " + this.appointmentType +
                "\nDate/Time: " + this.dateTime.format(FORMAT) +
                "\nNotes: " + (this.notes.isEmpty() ? "None" : this.notes);
    }
}
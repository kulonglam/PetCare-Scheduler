//Task 1
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pet registered with the Pet Care Scheduler.
 */
public class Pet implements Serializable {
    // Ensures compatibility when serializing/deserializing saved pet data.
    private static final long serialVersionUID = 1L;

    private String petId;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    // Stores the pet's scheduled appointments in insertion order.
    private ArrayList<Appointment> appointments;

    public Pet(String petId, String name, String species, int age, String ownerName, String contactInfo) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = LocalDate.now();
        this.appointments = new ArrayList<>(); //Task 2
    }

    public String getPetId() {
        return petId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {

        this.name = name;
    }
    public String getSpecies() {

        return species;
    }
    public void setSpecies(String species) {

        this.species = species;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    @Override
    public String toString() {
        return "ID: " + this.petId +
                ", Name: " + this.name +
                ", Species/Breed: " + this.species +
                ", Age: " + this.age +
                ", Owner: " + this.ownerName +
                ", Contact: " + this.contactInfo +
                ", Registered: " + this.registrationDate;
    }
}
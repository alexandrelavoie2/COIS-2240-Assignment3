public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    public Vehicle(String make, String model, int year) {
    	this.make = capitalize(make);
    	this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
        if (!isValidPlate(plate)) {
            throw new IllegalArgumentException("License plate must be three letters followed by three numbers.");
        }

        this.licensePlate = plate.toUpperCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

    // I am reusing this helper to keep the constructor logic cleaner.
    private String capitalize(String input) {
    	if (input == null || input.isEmpty()) {
    		return null;
    	}

    	return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    // I am validating the license plate before assigning it to a vehicle.
    private boolean isValidPlate(String plate) {
    	if (plate == null || plate.isEmpty()) {
    		return false;
    	}

    	return plate.matches("[A-Za-z]{3}[0-9]{3}");
    }

}

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
    private static RentalSystem instance;
    private static final String sVehiclesFile = "vehicles.txt";
    private static final String sCustomersFile = "customers.txt";
    private static final String sRentalRecordsFile = "rental_records.txt";

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    private RentalSystem() {
        loadData();
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    // I am preventing duplicate license plates before adding a vehicle.
    public boolean addVehicle(Vehicle vehicle) {
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("A vehicle with this license plate already exists.");
            return false;
        }

        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            overwriteVehiclesFile();
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            overwriteVehiclesFile();
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    // I am appending each new vehicle to vehicles.txt when it is added.
    public void saveVehicle(Vehicle vehicle) {
        appendLine(sVehiclesFile, formatVehicle(vehicle));
    }

    // I am appending each new customer to customers.txt when it is added.
    public void saveCustomer(Customer customer) {
        String customerLine = customer.getCustomerId() + "|" + customer.getCustomerName();
        appendLine(sCustomersFile, customerLine);
    }

    // I am appending each rent or return event to rental_records.txt after it is created.
    public void saveRecord(RentalRecord record) {
        String recordLine = record.getRecordType() + "|" +
                record.getVehicle().getLicensePlate() + "|" +
                record.getCustomer().getCustomerId() + "|" +
                record.getCustomer().getCustomerName() + "|" +
                record.getRecordDate() + "|" +
                record.getTotalAmount();
        appendLine(sRentalRecordsFile, recordLine);
    }

    // I am loading the saved vehicles, customers, and rental records when the system starts.
    private void loadData() {
        loadVehiclesFromFile();
        loadCustomersFromFile();
        loadRecordsFromFile();
    }

    // I am rebuilding the vehicles list from vehicles.txt.
    private void loadVehiclesFromFile() {
        File fVehiclesFile = new File(sVehiclesFile);

        if (!fVehiclesFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fVehiclesFile))) {
            String sLine;

            while ((sLine = reader.readLine()) != null) {
                String[] aParts = sLine.split("\\|");

                if (aParts.length < 6) {
                    continue;
                }

                Vehicle vehicle = createVehicleFromData(aParts);

                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.out.println("I could not load data from " + sVehiclesFile + ".");
        }
    }

    // I am rebuilding the customers list from customers.txt.
    private void loadCustomersFromFile() {
        File fCustomersFile = new File(sCustomersFile);

        if (!fCustomersFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fCustomersFile))) {
            String sLine;

            while ((sLine = reader.readLine()) != null) {
                String[] aParts = sLine.split("\\|");

                if (aParts.length < 2) {
                    continue;
                }

                int iCustomerId = Integer.parseInt(aParts[0]);
                String sCustomerName = aParts[1];
                customers.add(new Customer(iCustomerId, sCustomerName));
            }
        } catch (IOException e) {
            System.out.println("I could not load data from " + sCustomersFile + ".");
        }
    }

    // I am rebuilding the rental history from rental_records.txt.
    private void loadRecordsFromFile() {
        File fRentalRecordsFile = new File(sRentalRecordsFile);

        if (!fRentalRecordsFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fRentalRecordsFile))) {
            String sLine;

            while ((sLine = reader.readLine()) != null) {
                String[] aParts = sLine.split("\\|");

                if (aParts.length < 6) {
                    continue;
                }

                String sPlate = aParts[1];
                int iCustomerId = Integer.parseInt(aParts[2]);
                String sCustomerName = aParts[3];
                LocalDate dtRecordDate = LocalDate.parse(aParts[4]);
                double dTotalAmount = Double.parseDouble(aParts[5]);
                Vehicle vehicle = findVehicleByPlate(sPlate);
                Customer customer = findCustomerById(iCustomerId);

                if (vehicle == null) {
                    continue;
                }

                if (customer == null) {
                    customer = new Customer(iCustomerId, sCustomerName);
                }

                rentalHistory.addRecord(new RentalRecord(vehicle, customer, dtRecordDate, dTotalAmount, aParts[0]));
            }
        } catch (IOException e) {
            System.out.println("I could not load data from " + sRentalRecordsFile + ".");
        }
    }

    private void overwriteVehiclesFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sVehiclesFile, false))) {
            for (Vehicle vehicle : vehicles) {
                writer.write(formatVehicle(vehicle));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("I could not update " + sVehiclesFile + ".");
        }
    }

    private void appendLine(String fileName, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("I could not save data to " + fileName + ".");
        }
    }

    private String formatVehicle(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            return "Car|" + vehicle.getLicensePlate() + "|" + vehicle.getMake() + "|" +
                    vehicle.getModel() + "|" + vehicle.getYear() + "|" + vehicle.getStatus() + "|" +
                    car.getNumSeats();
        }

        if (vehicle instanceof Minibus) {
            Minibus minibus = (Minibus) vehicle;
            return "Minibus|" + vehicle.getLicensePlate() + "|" + vehicle.getMake() + "|" +
                    vehicle.getModel() + "|" + vehicle.getYear() + "|" + vehicle.getStatus() + "|" +
                    minibus.isAccessible();
        }

        if (vehicle instanceof PickupTruck) {
            PickupTruck pickupTruck = (PickupTruck) vehicle;
            return "PickupTruck|" + vehicle.getLicensePlate() + "|" + vehicle.getMake() + "|" +
                    vehicle.getModel() + "|" + vehicle.getYear() + "|" + vehicle.getStatus() + "|" +
                    pickupTruck.getCargoSize() + "|" + pickupTruck.hasTrailer();
        }

        return "Vehicle|" + vehicle.getLicensePlate() + "|" + vehicle.getMake() + "|" +
                vehicle.getModel() + "|" + vehicle.getYear() + "|" + vehicle.getStatus();
    }

    private Vehicle createVehicleFromData(String[] aParts) {
        String sVehicleType = aParts[0];
        String sLicensePlate = aParts[1];
        String sMake = aParts[2];
        String sModel = aParts[3];
        int iYear = Integer.parseInt(aParts[4]);
        Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(aParts[5]);
        Vehicle vehicle;

        if (sVehicleType.equals("Car") && aParts.length >= 7) {
            int iNumSeats = Integer.parseInt(aParts[6]);
            vehicle = new Car(sMake, sModel, iYear, iNumSeats);
        } else if (sVehicleType.equals("Minibus") && aParts.length >= 7) {
            boolean bIsAccessible = Boolean.parseBoolean(aParts[6]);
            vehicle = new Minibus(sMake, sModel, iYear, bIsAccessible);
        } else if (sVehicleType.equals("PickupTruck") && aParts.length >= 8) {
            double dCargoSize = Double.parseDouble(aParts[6]);
            boolean bHasTrailer = Boolean.parseBoolean(aParts[7]);
            vehicle = new PickupTruck(sMake, sModel, iYear, dCargoSize, bHasTrailer);
        } else {
            return null;
        }

        vehicle.setLicensePlate(sLicensePlate);
        vehicle.setStatus(status);
        return vehicle;
    }

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
}

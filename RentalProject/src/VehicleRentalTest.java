import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public class VehicleRentalTest {

    @Test
    public void testLicensePlate() {
        Vehicle vCar = new Car("Toyota", "Corolla", 2019, 4);
        Vehicle vMinibus = new Minibus("Honda", "Civic", 2021, true);
        Vehicle vPickupTruck = new PickupTruck("Ford", "Focus", 2024, 2.5, false);

        vCar.setLicensePlate("AAA100");
        vMinibus.setLicensePlate("ABC567");
        vPickupTruck.setLicensePlate("ZZZ999");

        assertEquals("AAA100", vCar.getLicensePlate());
        assertEquals("ABC567", vMinibus.getLicensePlate());
        assertEquals("ZZZ999", vPickupTruck.getLicensePlate());

        assertThrows(IllegalArgumentException.class, () -> vCar.setLicensePlate(""));
        assertThrows(IllegalArgumentException.class, () -> vMinibus.setLicensePlate(null));
        assertThrows(IllegalArgumentException.class, () -> vPickupTruck.setLicensePlate("AAA1000"));
        assertThrows(IllegalArgumentException.class, () -> vCar.setLicensePlate("ZZZ99"));
    }

    @Test
    public void testRentAndReturnVehicle() {
        Vehicle vCar = new Car("Toyota", "Corolla", 2019, 4);
        Customer cCustomer = new Customer(1, "George");
        RentalSystem rsRentalSystem = RentalSystem.getInstance();

        vCar.setLicensePlate("AAA111");

        assertEquals(Vehicle.VehicleStatus.Available, vCar.getStatus());

        assertTrue(rsRentalSystem.rentVehicle(vCar, cCustomer, LocalDate.now(), 100.0));
        assertEquals(Vehicle.VehicleStatus.Rented, vCar.getStatus());

        assertFalse(rsRentalSystem.rentVehicle(vCar, cCustomer, LocalDate.now(), 100.0));

        assertTrue(rsRentalSystem.returnVehicle(vCar, cCustomer, LocalDate.now(), 0.0));
        assertEquals(Vehicle.VehicleStatus.Available, vCar.getStatus());

        assertFalse(rsRentalSystem.returnVehicle(vCar, cCustomer, LocalDate.now(), 0.0));
    }

    @Test
    public void testSingletonRentalSystem() throws Exception {
        Constructor<RentalSystem> cRentalSystemConstructor = RentalSystem.class.getDeclaredConstructor();
        int iModifiers = cRentalSystemConstructor.getModifiers();

        assertEquals(Modifier.PRIVATE, iModifiers);
        assertNotNull(RentalSystem.getInstance());
    }
}

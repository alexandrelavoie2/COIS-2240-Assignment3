import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}

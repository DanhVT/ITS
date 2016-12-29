package hcmut.its.vehicle;

public final class VehicleFacotry {

	public static Motor createDefaultMotor() {
		return new Motor("000", "AAA");
	}

	public static Car createDefaultCar() {
		return new CarImpl("000", "AAA", 5);
	}

	public static Truck createDefaultTruck() {
		return new TruckImpl("000", "AAA", 3, 2.5, 3.5);
	}

}

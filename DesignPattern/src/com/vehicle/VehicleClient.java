package com.vehicle;

public class VehicleClient {

	public static void main(String[] args) {
		Vehicle maruti = new Vehicle.VehicleBuilder("Maruti", 4).setAirbags(2).build();
		System.out.println(maruti);
		
		Vehicle honda = new Vehicle.VehicleBuilder("Honda", 2).build();
		System.out.println(honda);
	}
}

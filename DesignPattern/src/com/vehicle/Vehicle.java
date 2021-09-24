package com.vehicle;

public class Vehicle {

	private String engine;
	private int wheel;
	private int airbags;
	
	public String getEngine() {
		return engine;
	}
	
	public int getWheel() {
		return wheel;
	}
	
	public int getAirbags() {
		return airbags;
	}
	
	private Vehicle(VehicleBuilder builder) {
		this.engine=builder.engine;
		this.wheel=builder.wheel;
		this.airbags=builder.airbags;
	}
	
	@Override
	public String toString() {
		return "Vehicle [engine=" + engine + ", wheel=" + wheel + ", airbags=" + airbags + "]";
	}



	public static class VehicleBuilder{
		private String engine;
		private int wheel;
		private int airbags;
		
		VehicleBuilder(String engine, int wheel){
			this.engine=engine;
			this.wheel=wheel;
		}

		public VehicleBuilder setAirbags(int airbags) {
			this.airbags = airbags;
			return this;
		}

		public Vehicle build() {
			return new Vehicle(this);
		}
	}
}

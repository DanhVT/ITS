package hcmut.its.rule;

import hcmut.its.vehicle.Vehicle;

public interface Rule {
	
	public double maxWeight();
	
	public double minWeight();
	
	public double maxLoad();
	
	public double minLoad();
	
	public double maxTotalLoad();
	
	public double minTotalLoad();
	
	//in minutes
	public int startTime();
	
	//in minutes
	public int endTime();
	
	public boolean allows(Vehicle v, int time);
	
}

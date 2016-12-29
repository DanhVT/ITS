package hcmut.its.rule;

import hcmut.its.vehicle.Truck;
import hcmut.its.vehicle.Vehicle;

public class RuleImpl implements Rule {

	private double maxWeight;

	private double minWeight;

	private double maxLoad;

	private double minLoad;

	private double maxTotalLoad;

	private double minTotalLoad;

	private int startTime;

	private int endTime;

	public RuleImpl() {
		maxWeight = 50;
		minWeight = 0;
		minLoad = 0;
		maxLoad = 40;
		minTotalLoad = 0;
		maxTotalLoad = 50;
		startTime = 0;
		endTime = 1439;
	}

	public RuleImpl(double maxWeight, double minWeight, double maxLoad,
			double minLoad, double maxTotalLoad, double minTotalLoad,
			int startTime, int endTime) {
		super();
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
		this.maxLoad = maxLoad;
		this.minLoad = minLoad;
		this.maxTotalLoad = maxTotalLoad;
		this.minTotalLoad = minTotalLoad;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public double maxWeight() {
		return maxWeight;
	}

	@Override
	public double minWeight() {
		return minWeight;
	}

	@Override
	public double maxLoad() {
		return maxLoad;
	}

	@Override
	public double minLoad() {
		return minLoad;
	}

	@Override
	public double maxTotalLoad() {
		return maxTotalLoad;
	}

	@Override
	public double minTotalLoad() {
		return minTotalLoad;
	}

	@Override
	public int startTime() {
		return startTime;
	}

	@Override
	public int endTime() {
		return endTime;
	}

	@Override
	public boolean allows(Vehicle v, int time) {
		boolean allow = true;
		if (time > startTime && time < endTime) {
			if (v instanceof Truck) {
				Truck truck = (Truck) v;
				allow = truck.weight() > minWeight
						&& truck.weight() < maxWeight && truck.load() > minLoad
						&& truck.load() < maxLoad
						&& truck.totalLoad() > minTotalLoad
						&& truck.totalLoad() < maxTotalLoad;
			}
		}
		return allow;
		// return false;
	}

}

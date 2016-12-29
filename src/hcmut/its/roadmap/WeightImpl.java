package hcmut.its.roadmap;

import hcmut.its.util.MapUtils;

public class WeightImpl implements Weight {

	protected double[] speeds;

	protected double distance;

	protected double speed;

	protected int profile;


	public WeightImpl() {
	}

	public WeightImpl(double[] speeds, double distance, double speed,
			int profile) {
		super();
		this.speeds = speeds;
		this.distance = distance;
		this.speed = speed;
		this.profile = profile;
	}

	@Override
	public double profileSpeedAt(double time) {
		/**
		 * it's safety anyway!!
		 */
		if (hasProfile()) {
			int frame = MapUtils.time2Frame(time);
			double tmp = speeds[frame];
			return tmp == 40 ? staticSpeed() : tmp;
		}
		return staticSpeed();
	}

	@Override
	public double staticSpeed() {
		return speed;
	}

	@Override
	public double distance() {
		return distance;
	}

	@Override
	public void setDistance(double d) {
		distance = d;
	}

	@Override
	public boolean hasProfile() {
		return profile == 1 ? true : false;
	}

	/**
	 * no need to clone speed profile
	 */
	@Override
	public Weight clone() {
		return new WeightImpl(speeds, distance, speed, profile);
	}

}

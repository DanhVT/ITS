package hcmut.its.algorithm;

public class TimeLabel implements Comparable<TimeLabel> {

	private double time;
	private double distance;

	public TimeLabel(double d, double t) {
		distance = d;
		time = t;
	}

	public double time() {
		return time;
	}

	public double distance() {
		return distance;
	}

	public void setDistance(double d) {
		distance = d;
	}

	public void setTime(double t) {
		time = t;
	}

	@Override
	public int compareTo(TimeLabel o) {
		if (getClass() != o.getClass())
			throw new IllegalArgumentException();
		if (time > o.time())
			return 1;
		else if (time == o.time())
			return 0;
		else
			return -1;

	}

}

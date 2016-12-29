package hcmut.its.algorithm;

public class PreferenceLabel implements Comparable<PreferenceLabel> {

	private double p;
	private double time;
	private double distance;

	public PreferenceLabel(double d, double t, double p) {
		distance = d;
		time = t;
		this.p = p;
	}

	public double preference() {
		return p;
	}

	public void setPreference(double p) {
		this.p = p;
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
	public int compareTo(PreferenceLabel o) {
		if (getClass() != o.getClass())
			throw new IllegalArgumentException();
		if (p > o.preference())
			return 1;
		else if (p == o.preference())
			return 0;
		else
			return -1;

	}

}

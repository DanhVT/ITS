package hcmut.its.algorithm;

public class DistanceLabel implements Comparable<DistanceLabel> {

	private double distance;

	public DistanceLabel(double d) {
		distance = d;
	}

	public double distance() {
		return distance;
	}

	public void setDistance(double d) {
		distance = d;
	}

	@Override
	public int compareTo(DistanceLabel o) {
		if (getClass() != o.getClass())
			throw new IllegalArgumentException();
		if (distance > o.distance())
			return 1;
		else if (distance == o.distance())
			return 0;
		else
			return -1;
	}

}

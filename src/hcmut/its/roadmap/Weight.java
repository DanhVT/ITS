package hcmut.its.roadmap;

public interface Weight extends Cloneable {

	public double profileSpeedAt(double time);

	public double staticSpeed();

	public double distance();

	public void setDistance(double d);

	public boolean hasProfile();

	public Weight clone();
	
}

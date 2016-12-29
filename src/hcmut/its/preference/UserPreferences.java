package hcmut.its.preference;

public interface UserPreferences {
	
	public String user();
	
	public double preferenceOfSegment(String id);
	
	public void addPreference(String id, double value);

}

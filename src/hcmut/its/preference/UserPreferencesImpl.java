package hcmut.its.preference;

import java.util.HashMap;
import java.util.Map;

public class UserPreferencesImpl implements UserPreferences {

	private Map<String, Double> preferences;

	private String user;

	private static double DEFAULT = 0.5;

	public UserPreferencesImpl(String user) {
		preferences = new HashMap<>();
		this.user = user;
	}

	@Override
	public String user() {
		return user;
	}

	@Override
	public double preferenceOfSegment(String id) {
		Double value = preferences.get(id);
		return (value != null) ? value : DEFAULT;
	}

	@Override
	public void addPreference(String id, double value) {
		preferences.put(id, value);
	}
}

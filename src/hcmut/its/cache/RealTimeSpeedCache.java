package hcmut.its.cache;

public class RealTimeSpeedCache {

	public static final int CACHE_NOT_FOUND = -1;

	public static final int REAL_TIME_NOT_FOUND = -2;

	private static RealTimeSpeedCache instance = null;

	/**
	 * expired time in the cache is 15 minutes
	 */
	static {
		instance = new RealTimeSpeedCache(15);
	}

	public static RealTimeSpeedCache getInstance() {
		return instance;
	}

	private SimpleCache<String, Double> cache;

	private RealTimeSpeedCache(int minutes) {
		cache = new SimpleCache<String, Double>(minutes * 60);
	}

	public void cacheRealTimeSpeedOf(String id, double speed) {
		cache.put(id, new Double(speed));
	}

	public double getRealTimeSpeedOf(String id) {
		Double value = cache.get(id);
		return value != null ? value : CACHE_NOT_FOUND;
	}
}

package hcmut.its.dao;

import hcmut.its.db.ITSDB;
import hcmut.its.preference.UserPreferences;
import hcmut.its.preference.UserPreferencesImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class PreferenceDAO {

	/*
	 * protected int[] searchingBox; protected double[] srcBox; protected
	 * double[] destBox;
	 * 
	 * public PreferenceDAO(double lat1, double lng1, double lat2, double lng2)
	 * { searchingBox = MapUtils.getSearchingBox(lat1, lng1, lat2, lng2); srcBox
	 * = MapUtils.getBox(lat1, lng1); destBox = MapUtils.getBox(lat2, lng2); }
	 */

	public UserPreferences getPreferences(String user) {

		UserPreferences preferences = new UserPreferencesImpl(user);

		DBCollection preferenceColelction = ITSDB.getCollection(user);
		DBCursor cursor = preferenceColelction.find();
		while (cursor.hasNext()) {
			BasicDBObject preference = (BasicDBObject) cursor.next();
			String id = preference.getString("segment_id");
			double value = preference.getDouble("value");
			preferences.addPreference(id, value);
		}
		return preferences;
	}
}

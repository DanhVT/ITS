package hcmut.its.dao;

import hcmut.its.db.ITSDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class SpeedProfileDAO {

	private BasicDBObject profileBson = null;
	private static final String PROFILE_COLLECTION_NAME = "speed_profiles";
	private static final int FRAME_SIZE = 96;

	public SpeedProfileDAO(String id) {
		DBCollection profileColelction = ITSDB
				.getCollection(PROFILE_COLLECTION_NAME);
		profileBson = (BasicDBObject) profileColelction
				.findOne(new BasicDBObject("segment_id", (int) (Integer.parseInt(id))));
	}

	public double[] getSpeedProfile() {
		double[] profile = new double[FRAME_SIZE];
		for (int i = 0; i < FRAME_SIZE; i++) {
			//profile[i] = ((BasicDBObject) profileBson.get("speeds"))
			//		.getDouble("" + i);
			profile[i] = ((BasicDBObject) profileBson).getDouble("" + i);

		}
		return profile;
	}

}

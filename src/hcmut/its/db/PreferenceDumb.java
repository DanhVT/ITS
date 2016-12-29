package hcmut.its.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class PreferenceDumb {

	public static void build(String name) {

		DBCollection segmentCollection = ITSDB.getCollection("segments");
		DBCollection preferenceCollection = ITSDB.getCollection(name);

		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {
			DBObject segmentBson = cursor.next();
			DBObject preferenceBson = new BasicDBObject();
			if (segmentBson.get("street_type").equals("primary")) {
				preferenceBson.put("segment_id", segmentBson.get("segment_id"));
				preferenceBson.put("cell_x", segmentBson.get("cell_x"));
				preferenceBson.put("cell_y", segmentBson.get("cell_y"));
				preferenceBson.put("value", 10);
				preferenceCollection.insert(preferenceBson);
				System.out.println("Segment " + segmentBson.get("segment_id")
						+ " is inserted.");
			}
		}

	}

	public static void main(String[] args) {
		build("testuser");
	}

}

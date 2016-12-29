package hcmut.its.db;

import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class RefinedSpeedProfileBuilder extends SpeedProfileBuilder {

	public RefinedSpeedProfileBuilder() {
		super();
		name = "refined_speed_profiles";
	}

	@Override
	public void build(String name) {

		DBCollection segmentCollection = ITSDB
				.getCollection("refined_segments");

		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {

			BasicDBObject segmentBson = (BasicDBObject) cursor.next();

			if (segmentBson.getString("segment_type").equals("primary")) {

				DBCollection profileCollection = ITSDB
						.getCollection("speed_profiles");

				BasicDBObject profileBson = (BasicDBObject) profileCollection
						.findOne(new BasicDBObject("segment_id", segmentBson
								.getInt("segment_id")));

				DBCollection refinedProfileCollection = ITSDB
						.getCollection(name);

				BasicDBObject newProfileBson = new BasicDBObject();
				newProfileBson.put("segment_id",
						segmentBson.getInt("segment_id"));
				newProfileBson.put("cell_x", segmentBson.getInt("cell_x"));
				newProfileBson.put("cell_y", segmentBson.getInt("cell_y"));
				BasicDBObject speedBson = new BasicDBObject();
				for (int i = 0; i < SpeedProfileBuilder.FRAME_SIZE; i++) {
					speedBson.put(i + "", ((BasicDBObject) profileBson
							.get("speeds")).getInt("" + i));
				}
				newProfileBson.put("speeds", speedBson);
				refinedProfileCollection.insert(newProfileBson);
				System.out.println("Segment: "
						+ segmentBson.getInt("segment_id"));

			} else {
				BasicDBList subSegmentBson = (BasicDBList) segmentBson
						.get("sub_segments");

				double[] speeds = new double[FRAME_SIZE];

				for (int i = 0; i < FRAME_SIZE; i++)
					speeds[i] = 0;

				int subSegmentNum = 0;

				for (Iterator<Object> it = subSegmentBson.iterator(); it
						.hasNext();) {

					DBCollection profileCollection = ITSDB
							.getCollection("speed_profiles");

					BasicDBObject profileBson = (BasicDBObject) profileCollection
							.findOne(new BasicDBObject("segment_id", it.next()));

					for (int i = 0; i < FRAME_SIZE; i++) {
						speeds[i] = speeds[i]
								+ ((BasicDBObject) profileBson.get("speeds"))
										.getInt("" + i);
					}

					subSegmentNum++;
				}
				for (int i = 0; i < SpeedProfileBuilder.FRAME_SIZE; i++) {
					speeds[i] = speeds[i] / subSegmentNum;
				}

				DBCollection refinedProfileCollection = ITSDB
						.getCollection(name);

				BasicDBObject newProfileBson = new BasicDBObject();
				newProfileBson.put("segment_id",
						segmentBson.getInt("segment_id"));
				newProfileBson.put("cell_x", segmentBson.getInt("cell_x"));
				newProfileBson.put("cell_y", segmentBson.getInt("cell_y"));
				BasicDBObject speedBson = new BasicDBObject();
				for (int i = 0; i < SpeedProfileBuilder.FRAME_SIZE; i++) {
					speedBson.put(i + "", speeds[i]);
				}
				newProfileBson.put("speeds", speedBson);
				refinedProfileCollection.insert(newProfileBson);

				System.out.println("Segment: "
						+ segmentBson.getInt("segment_id"));

			}

		}

	}

}

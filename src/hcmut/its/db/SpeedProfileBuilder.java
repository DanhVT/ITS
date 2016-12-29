package hcmut.its.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class SpeedProfileBuilder extends CollectionBuilder {

	public static int FRAME_SIZE = 96;

	public static double UNKNOWN_SPEED = 40.0;

	protected double[] speeds = new double[FRAME_SIZE];

	protected List<String> dateList = new ArrayList<String>();

	public SpeedProfileBuilder() {

		super("speed_profiles");

		/*
		 * String d1 = "2014-01-28"; String d2 = "2014-01-29"; String d3 =
		 * "2014-01-30";
		 * 
		 * dateList.add(d1); dateList.add(d2); dateList.add(d3);
		 */

	}

	public SpeedProfileBuilder(List<String> dateList) {
		super("speed_profiles");
		this.dateList = dateList;
	}

	@Override
	public void build(String name) {

		DBCollection segmentCollection = ITSDB.getCollection("segments");
		DBCollection speedProfileCollection = ITSDB.getCollection(name);

		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {

			BasicDBObject segmentBson = (BasicDBObject) cursor.next();

			buildProfile(
					segmentBson.getString("segment_id"),
					segmentBson.getString("cell_x") + "-"
							+ segmentBson.getString("cell_y"), dateList);

			if (isUseful(speeds)) {

				// update profile
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set",
						new BasicDBObject().append("profile", 1));

				BasicDBObject searchQuery = new BasicDBObject().append(
						"segment_id", segmentBson.get("segment_id"));

				segmentCollection.update(searchQuery, newDocument);

				BasicDBObject profileBson = new BasicDBObject().append(
						"segment_id", segmentBson.getString("segment_id"));

				BasicDBObject speedBson = new BasicDBObject();
				for (int i = 0; i < FRAME_SIZE; i++)
					speedBson.append(i + "", speeds[i]);
				profileBson.append("speeds", speedBson);

				speedProfileCollection.insert(profileBson);

				System.out.println("Segment "
						+ segmentBson.getString("segment_id") + " is persited");
			}
		}

	}

	@Override
	public void index(String name) {
		DBCollection coll = ITSDB.getCollection(name);
		coll.ensureIndex(new BasicDBObject("segment_id", 1).append("unique",
				true));
	}

	protected static double[] getProfile(String segment, String cell,
			String date) {

		double[] speeds = new double[FRAME_SIZE];

		BufferedReader br = null;

		for (int i = 0; i < FRAME_SIZE; i++) {

			double speed = UNKNOWN_SPEED;
			try {
				String current = "";

				 br = new BufferedReader(new FileReader(
				 "/home/backup/BusDataBackUp/" + date + "/" + i + "/"
				 + cell + ".txt"));

				//br = new BufferedReader(new FileReader(
					//	"/Users/user/Desktop/ITS-Site/BusDataBackUp/" + date
						//		+ "/" + i + "/" + cell + ".txt"));

				while ((current = br.readLine()) != null) {
					String[] parts = current.split(",");
					String sgm = parts[0];
					String spd = parts[3];
					if (sgm.equals(segment)) {
						speed = Double.parseDouble(spd);
						break;
					}
				}

			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					// ex.printStackTrace();
				}
			}
			speeds[i] = speed;
		}

		return speeds;
	}

	private static boolean isUseful(double[] speeds) {
		boolean useful = false;
		int count = 0;
		for (int i = 0; i < FRAME_SIZE; i++)
			if (Math.abs(speeds[i] - 40.0) > 0)
				count++;
		if (count > 5)
			useful = true;
		return useful;
	}

	protected void buildProfile(String segment, String cell,
			List<String> dateList) {

		int[] counts = new int[FRAME_SIZE];

		for (int i = 0; i < FRAME_SIZE; i++) {
			speeds[i] = 0.0;
			counts[i] = 0;
		}

		for (String date : dateList) {
			double[] tmp = getProfile(segment, cell, date);
			for (int i = 0; i < FRAME_SIZE; i++) {
				if (tmp[i] != UNKNOWN_SPEED) {
					speeds[i] = speeds[i] + tmp[i];
					counts[i]++;
				}
			}
		}

		for (int i = 0; i < FRAME_SIZE; i++) {
			if (counts[i] > 0)
				speeds[i] = speeds[i] / counts[i];
			else
				speeds[i] = UNKNOWN_SPEED;
		}
	}

}

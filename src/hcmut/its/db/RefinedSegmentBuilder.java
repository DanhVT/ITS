package hcmut.its.db;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class RefinedSegmentBuilder extends SegmentBuilder{
	
	public RefinedSegmentBuilder() {
		name = "refined_segments";
	}
	
	@Override
	public void build(String name) {
		super.build(name);
		
		int segmentSize = 150000;

		DBCollection nodeCollection = ITSDB.getCollection("nodes");

		BasicDBObject q1 = new BasicDBObject();
		q1.put("in", 1);
		BasicDBObject q2 = new BasicDBObject();
		q2.put("out", 1);
		QueryBuilder query = new QueryBuilder();
		query.and(q1, q2);

		DBCursor cursor = nodeCollection.find(query.get());

		while (cursor.hasNext()) {
			
			DBObject nodeBson = cursor.next();

			// System.out.println(node.get("node_id") + "-" + node.get("in") +
			// "/" + node.get("out"));

			DBCollection segmentCollection = ITSDB
					.getCollection(name);

			BasicDBObject srcQuery = new BasicDBObject();
			srcQuery.put("src_id", nodeBson.get("node_id"));

			BasicDBObject asSrc = (BasicDBObject) segmentCollection
					.findOne(srcQuery);

			BasicDBObject tgtQuery = new BasicDBObject();
			tgtQuery.put("tgt_id", nodeBson.get("node_id"));

			BasicDBObject asTgt = (BasicDBObject) segmentCollection
					.findOne(tgtQuery);

			if (asSrc != null && asTgt != null) {
				BasicDBObject newSegment = new BasicDBObject();
				newSegment.put("segment_id", segmentSize++);
				newSegment.put("cell_id", asSrc.get("cell_id"));
				newSegment.put("cell_x", asSrc.get("cell_x"));
				newSegment.put("cell_y", asSrc.get("cell_y"));

				BasicDBObject srcBson = new BasicDBObject();
				srcBson.put("lat",
						((BasicDBObject) asTgt.get("src")).get("lat"));
				srcBson.put("lng",
						((BasicDBObject) asTgt.get("src")).get("lng"));
				newSegment.put("src_id", asTgt.get("src_id"));
				newSegment.put("src", srcBson);

				BasicDBObject tgtBson = new BasicDBObject();
				tgtBson.put("lat",
						((BasicDBObject) asSrc.get("tgt")).get("lat"));
				tgtBson.put("lng",
						((BasicDBObject) asSrc.get("tgt")).get("lng"));
				newSegment.put("tgt_id", asSrc.get("tgt_id"));
				newSegment.put("tgt", tgtBson);

				BasicDBObject streetBson = new BasicDBObject();
				streetBson.put("street_id",
						((BasicDBObject) asSrc.get("street")).get("street_id"));
				streetBson.put("street_type", ((BasicDBObject) asSrc
						.get("street")).get("street_type"));
				newSegment.put("street", streetBson);

				BasicDBList asTgtSubSegments = (BasicDBList) asTgt
						.get("sub_segments");
				BasicDBList asSrcSubSegments = (BasicDBList) asSrc
						.get("sub_segments");

				BasicDBList subSegments = orderedMerge(asTgtSubSegments,
						asSrcSubSegments);
				//subSegments.add(0, asTgt.get("segment_id"));
				//subSegments.add(asSrc.get("segment_id"));

				double distance = asTgt.getDouble("distance")
						+ asSrc.getDouble("distance");

				newSegment.put("distance", distance);
				
				newSegment.put("base_speed", 40.0);

				newSegment.put("segment_type", "compound");

				newSegment.put("sub_segments", subSegments);

				segmentCollection.insert(newSegment);
				segmentCollection.remove(asSrc);
				segmentCollection.remove(asTgt);
				System.out.println("Merging " + asTgt.get("segment_id") + "-"
						+ asSrc.get("segment_id"));

			}
			// }

			// System.out.println(src.get("src") + "-" + tgt.get("tgt"));

		}
	}

}

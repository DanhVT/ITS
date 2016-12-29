package hcmut.its.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class NodeBuilder extends CollectionBuilder {

	public NodeBuilder() {
		super("nodes");
	}

	@Override
	public void build(String name) {
		DBCollection nodeCollection = TrafficDB.getCollection("node");
		DBCursor cursor = nodeCollection.find();

		while (cursor.hasNext()) {
			DBObject nodeBson = cursor.next();

			DBObject newNodeBson = new BasicDBObject();
			newNodeBson.put("node_id", nodeBson.get("node_id"));
			newNodeBson.put("lat", nodeBson.get("node_lat"));
			newNodeBson.put("lng", nodeBson.get("node_lon"));
			
			DBCollection coll = ITSDB.getCollection(name);
			coll.insert(newNodeBson);
			System.out.println("Node " + nodeBson.get("node_id")
					+ " is inserted.");
		}
	}

	@Override
	public void index(String name) {
		DBCollection coll = ITSDB.getCollection(name);
		coll.ensureIndex(new BasicDBObject("node_id", 1).append("unique", true));
		coll.ensureIndex(new BasicDBObject("lat", 1).append("lng", 1));
	}

}

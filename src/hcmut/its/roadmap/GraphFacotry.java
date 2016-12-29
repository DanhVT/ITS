package hcmut.its.roadmap;

import hcmut.its.graph.Edge;
import hcmut.its.graph.EdgeImpl;
import hcmut.its.graph.Vertex;
import hcmut.its.graph.VertexImpl;

public final class GraphFacotry {

	public static int NODE_COUNT = 1;

	public static int SEGMENT_COUNT = 350000;

	public static int END_SEGMENT = 5000000;

	private static RoadMap instance = null;

	public static Vertex<Node> createVertex(String id, double lat, double lng) {
		return new VertexImpl<Node>(new NodeImpl(id, lat, lng));
	}

	public static Edge<Node, Segment, Weight> createEdge(String id, int x,
			int y, final SegmentInfo info, final Vertex<Node> source,
			final Vertex<Node> target, Weight weight) {
		return new EdgeImpl<Node, Segment, Weight>(new SegmentImpl(id, x, y,
				info), source, target, weight);
	}

	public static SegmentInfo createSegmentInfo(String id, String name, String type) {
		return new SegmentInfoImpl(id, name, type);
	}
	
	public static SegmentInfo createSegmentInfo(String id, String name, String type, boolean tunel,
			boolean bridge) {
		return new SegmentInfoImpl(id, name, type, tunel, bridge);
	}


	public static Weight createWeight(double[] speeds, double distance,
			double speed, int profile) {
		return new WeightImpl(speeds, distance, speed, profile);
	}
	
	/*
	 * for user preference
	 *
	public static Weight createWeight(double[] speeds, double distance,
			double speed, int profile, double preference) {
		return new WeightImpl(speeds, distance, speed, profile, preference);
	}
*/

	public static RoadMap getMapInstance() {
		if (instance == null)
			loadMapInstance();
		return instance;
	}

	public static void loadMapInstance() {
		instance = new RoadMapImpl();
		instance.build();
	}
}

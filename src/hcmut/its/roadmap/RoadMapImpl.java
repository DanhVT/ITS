package hcmut.its.roadmap;

import hcmut.its.dao.RoadMapDAO;
import hcmut.its.filter.Filter;
import hcmut.its.graph.Edge;
import hcmut.its.graph.GraphImpl;
import hcmut.its.graph.Vertex;
import hcmut.its.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

public class RoadMapImpl extends GraphImpl<Node, Segment, Weight> implements
		RoadMap {

	public RoadMapImpl() {
		super();
	}

	@Override
	public void build() {
		RoadMapDAO.build(this);
	}

	@Override
	public Vertex<Node> getVertexById(String id) {
		for (Vertex<Node> v : vertices()) {
			if (v.data().id().equals(id))
				return v;
		}
		return null;
	}

	@Override
	public Edge<Node, Segment, Weight> getEdgeById(String id) {
		for (Edge<Node, Segment, Weight> e : edges()) {
			if (e.data().id().equals(id))
				return e;
		}
		return null;
	}

	@Override
	public RoadMap clone(Filter f, int time) {
		RoadMap g = new RoadMapImpl();
		for (Edge<Node, Segment, Weight> e : edges()) {
			if (f.filter(e, time))
				g.addEdge(e.clone());
		}
		return g;
	}

	@Override
	public Vertex<Node> edgePartition(Edge<Node, Segment, Weight> e,
			double lat, double lng, int oneway) {

		/**
		 * reset static variables to default values if they overflow
		 */
		GraphFacotry.NODE_COUNT = GraphFacotry.NODE_COUNT == 10000 ? 0
				: GraphFacotry.NODE_COUNT;

		GraphFacotry.SEGMENT_COUNT = GraphFacotry.SEGMENT_COUNT == 700000 ? 350000
				: GraphFacotry.SEGMENT_COUNT;

		Vertex<Node> v = null;
		Vertex<Node> vv = null;

		Vertex<Node> v1 = e.source();
		Vertex<Node> v2 = e.target();

		if (v1.data().lat() == lat && v1.data().lng() == lng)
			v = v1;
		else if (v2.data().lat() == lat && v2.data().lng() == lng)
			v = v2;
		else {

			double[] vvLatLng = MapUtils.intersectionPointFromLine(v1.data()
					.lat(), v1.data().lng(), v2.data().lat(), v2.data().lng(),
					lat, lng);

			v = GraphFacotry.createVertex(GraphFacotry.NODE_COUNT++ + "", lat,
					lng);
			vv = GraphFacotry.createVertex(GraphFacotry.NODE_COUNT++ + "",
					vvLatLng[0], vvLatLng[1]);

			Weight w3 = e.weight().clone();
			w3.setDistance(MapUtils
					.distance(vvLatLng[0], vvLatLng[1], lat, lng));

			addEdge(GraphFacotry.createEdge(GraphFacotry.END_SEGMENT++ + "", e
					.data().cellX(), e.data().cellY(), e.data().info(), v, vv,
					w3));

			addEdge(GraphFacotry.createEdge(GraphFacotry.END_SEGMENT++ + "", e
					.data().cellX(), e.data().cellY(), e.data().info(), vv, v,
					w3));

			Weight w1 = e.weight().clone();
			w1.setDistance(MapUtils.distance(v1.data().lat(), v1.data().lng(),
					lat, lng));

			addEdge(GraphFacotry.createEdge(GraphFacotry.END_SEGMENT++ + "", e
					.data().cellX(), e.data().cellY(), e.data().info(), v1, vv,
					w1));

			Weight w2 = e.weight().clone();
			w2.setDistance(MapUtils.distance(v2.data().lat(), v2.data().lng(),
					lat, lng));

			addEdge(GraphFacotry.createEdge(GraphFacotry.END_SEGMENT++ + "", e
					.data().cellX(), e.data().cellY(), e.data().info(), vv, v2,
					w2));

			if (oneway == 0) {

				addEdge(GraphFacotry.createEdge(
						GraphFacotry.END_SEGMENT++ + "", e.data().cellX(), e
								.data().cellY(), e.data().info(), vv, v1, w1));

				addEdge(GraphFacotry.createEdge(
						GraphFacotry.END_SEGMENT++ + "", e.data().cellX(), e
								.data().cellY(), e.data().info(), v2, vv, w2));
			}

		}

		return v;
	}

	@Override
	public List<Edge<Node, Segment, Weight>> getSegmentsInCell(int x, int y) {
		List<Edge<Node, Segment, Weight>> result = new ArrayList<Edge<Node, Segment, Weight>>();
		for (Edge<Node, Segment, Weight> e : edges())
			if (e.data().cellX() == x && e.data().cellY() == y)
				result.add(e);
		return result;
	}

	@Override
	public String getSegment(double lat, double lng) {

		double[] box = MapUtils.getBox(lat, lng);

		double dMin = 1000;
		String idMin = "";
		double dTemp;

		for (Edge<Node, Segment, Weight> e : edges()) {
			if (MapUtils.inBox(e, box)) {
				double sLat = e.source().data().lat();
				double sLng = e.source().data().lng();
				double dLat = e.target().data().lat();
				double dLng = e.target().data().lng();

				String segmentId = e.data().id();
				String streetType = e.data().info().type();

				dTemp = MapUtils.nodeMatchSegment(sLat, sLng, dLat, dLng, lat,
						lng, streetType);

				if (dTemp >= 0) {
					System.out.println("Segment Id:" + segmentId + " d = "
							+ dTemp);
					if (dTemp < dMin) {
						dMin = dTemp;
						idMin = segmentId;
					}
				}
			}
		}

		int id = Integer.parseInt(idMin);
		id = id < GraphFacotry.SEGMENT_COUNT ? id : id
				- GraphFacotry.SEGMENT_COUNT;
		return id + "";
	}

}

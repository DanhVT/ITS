package hcmut.its.roadmap;

import java.util.List;

import hcmut.its.filter.Filter;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;

public interface RoadMap extends Graph<Node, Segment, Weight> {

	public void build();

	public RoadMap clone(Filter f, int time);

	public Vertex<Node> getVertexById(String id);

	public Edge<Node, Segment, Weight> getEdgeById(String id);

	public Vertex<Node> edgePartition(Edge<Node, Segment, Weight> e,
			double lat, double lng, int oneway);

	public List<Edge<Node, Segment, Weight>> getSegmentsInCell(int x, int y);

	public String getSegment(double lat, double lng);

}

package hcmut.its.algorithm;

import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public interface Label<L> extends Comparable<Label<L>> {

	public L data();

	public Vertex<Node> vertex();

	public void setVertex(Vertex<Node> v);

	public Label<L> predecessor();

	/**
	 * the edge from its vertex to the vertex of the predecessor
	 */
	public Edge<Node, Segment, Weight> edge();

	public void setPredecessor(Label<L> l);

	public void setEdge(Edge<Node, Segment, Weight> e);

	public boolean isSource();

}

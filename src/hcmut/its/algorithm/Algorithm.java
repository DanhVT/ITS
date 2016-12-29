package hcmut.its.algorithm;

import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public interface Algorithm<L> {

	public void setQueue(Queue<Label<L>> q);

	public int queueOperationCount();

	public ITSPath<L> search(Graph<Node, Segment, Weight> g, Vertex<Node> s,
			Vertex<Node> d, double t);
}

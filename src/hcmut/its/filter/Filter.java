package hcmut.its.filter;

import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public interface Filter {

	public boolean filter(Edge<Node, Segment, Weight> e, int time);

}

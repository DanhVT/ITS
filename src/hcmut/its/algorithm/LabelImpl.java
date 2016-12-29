package hcmut.its.algorithm;

import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public class LabelImpl<L extends Comparable<L>> implements Label<L> {

	private L data;

	private Vertex<Node> vertex;

	private Label<L> predecessor = null;

	private Edge<Node, Segment, Weight> edge = null;

	public LabelImpl(Vertex<Node> v) {
		this.vertex = v;
	}

	public LabelImpl(Vertex<Node> v, L d) {
		this(v);
		data = d;
	}

	@Override
	public L data() {
		return data;
	}

	@Override
	public Vertex<Node> vertex() {
		return vertex;
	}

	@Override
	public void setVertex(Vertex<Node> v) {
		this.vertex = v;
	}

	@Override
	public Label<L> predecessor() {
		return predecessor;
	}

	@Override
	public Edge<Node, Segment, Weight> edge() {
		return edge;
	}

	@Override
	public void setPredecessor(Label<L> l) {
		predecessor = l;
	}

	@Override
	public void setEdge(Edge<Node, Segment, Weight> e) {
		edge = e;
	}

	@Override
	public boolean isSource() {
		return predecessor == null ? true : false;
	}

	@Override
	public int compareTo(Label<L> o) {
		if (getClass() != o.getClass())
			throw new IllegalArgumentException();
		return data.compareTo(o.data());
	}
}

package hcmut.its.algorithm;

import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

import java.util.HashMap;
import java.util.Map;

public abstract class AlgorithmAbstract<L extends Comparable<L>> implements
		Algorithm<L> {

	protected Map<Vertex<Node>, Label<L>> labelMap;

	protected Queue<Label<L>> queue;

	protected int queueNum = 0;

	public AlgorithmAbstract() {
		queue = new BinaryHeap<Label<L>>();
		labelMap = new HashMap<Vertex<Node>, Label<L>>();
	}

	@Override
	public void setQueue(Queue<Label<L>> q) {
		queue = q;
	}

	@Override
	public int queueOperationCount() {
		return queueNum;
	}

	protected abstract Label<L> getLabelOf(Vertex<Node> v);

	@Override
	public abstract ITSPath<L> search(Graph<Node, Segment, Weight> g,
			Vertex<Node> s, Vertex<Node> d, double t);
}

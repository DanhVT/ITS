package hcmut.its.algorithm;

import hcmut.its.graph.Edge;
import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public class DistanceDijkstra extends AlgorithmAbstract<DistanceLabel> {

	public Label<DistanceLabel> getLabelOf(Vertex<Node> v) {
		if (!labelMap.keySet().contains(v)) {
			DistanceLabel dl = new DistanceLabel(Double.MAX_VALUE);
			return new LabelImpl<DistanceLabel>(v, dl);
		} else
			return labelMap.get(v);
	}

	@Override
	public ITSPath<DistanceLabel> search(Graph<Node, Segment, Weight> g,
			Vertex<Node> s, Vertex<Node> d, double t) {
		ITSPath<DistanceLabel> path = new ITSPath<DistanceLabel>();
		Label<DistanceLabel> initialLabel = new LabelImpl<DistanceLabel>(s,
				new DistanceLabel(0));
		labelMap.put(s, initialLabel);
		queue.add(initialLabel);
		while (!queue.isEmpty()) {
			Label<DistanceLabel> label = queue.remove();
			Vertex<Node> u = label.vertex();
			if (u.equals(d)) {
				path.setLabel(getLabelOf(u));
				return path;
			}
			for (Edge<Node, Segment, Weight> e : g.outgoingEdgesOf(u)) {
				Weight w = e.weight();
				Vertex<Node> v = e.target();
				double oldWeight = getLabelOf(v).data().distance();
				double newWeight = getLabelOf(u).data().distance()
						+ w.distance();
				if (newWeight < oldWeight) {
					Label<DistanceLabel> newLabel = getLabelOf(v);
					newLabel.data().setDistance(newWeight);
					newLabel.setPredecessor(label);
					newLabel.setEdge(e);
					labelMap.put(v, newLabel);
					queue.add(newLabel);
					queueNum++;
				}
			}
		}
		return path;
	}
}

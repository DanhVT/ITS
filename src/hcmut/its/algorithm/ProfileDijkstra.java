package hcmut.its.algorithm;

import hcmut.its.graph.Edge;
import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.MapUtils;

public class ProfileDijkstra extends StaticTimeDijkstra {

	@Override
	public ITSPath<TimeLabel> search(Graph<Node, Segment, Weight> g,
			Vertex<Node> s, Vertex<Node> d, double t) {
		ITSPath<TimeLabel> path = new ITSPath<TimeLabel>();
		Label<TimeLabel> initialLabel = new LabelImpl<TimeLabel>(s,
				new TimeLabel(0, t));
		labelMap.put(s, initialLabel);
		queue.add(initialLabel);
		while (!queue.isEmpty()) {
			Label<TimeLabel> label = queue.remove();
			Vertex<Node> u = label.vertex();
			if (u.equals(d)) {
				path.setLabel(getLabelOf(u));
				return path;
			}
			for (Edge<Node, Segment, Weight> e : g.outgoingEdgesOf(u)) {
				Weight w = e.weight();
				Vertex<Node> v = e.target();

				double oldTime = getLabelOf(v).data().time();
				double newTime = getLabelOf(u).data().time()
						+ w.distance()
						/ (MapUtils.kph2Mpm(w.profileSpeedAt(getLabelOf(u)
								.data().time()))*2); //note here!
				if (newTime < oldTime) {
					Label<TimeLabel> newLabel = getLabelOf(v);
					double newDistance = getLabelOf(u).data().distance()
							+ w.distance();
					newLabel.data().setDistance(newDistance);
					newLabel.data().setTime(newTime);
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

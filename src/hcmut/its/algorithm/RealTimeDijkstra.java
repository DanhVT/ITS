package hcmut.its.algorithm;

import hcmut.its.cache.RealTimeSpeedCache;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.MapUtils;
import hcmut.its.util.SpeedUtils;

public class RealTimeDijkstra extends StaticTimeDijkstra {

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
						/ MapUtils.kph2Mpm(getSpeedOf(e, getLabelOf(u).data()
								.time()));
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

	public static double getSpeedOf(Edge<Node, Segment, Weight> e, double time) {

		int id = Integer.parseInt(e.data().id()) > GraphFacotry.SEGMENT_COUNT ? Integer
				.parseInt(e.data().id()) - GraphFacotry.SEGMENT_COUNT
				: Integer.parseInt(e.data().id());

		double speed = RealTimeSpeedCache.getInstance().getRealTimeSpeedOf(
				id + "");
		double speed1 = speed;

		if (speed == RealTimeSpeedCache.CACHE_NOT_FOUND) {

			if (!((Weight) e.weight()).hasProfile()) {
				double staticSpeed = ((Weight) e.weight()).staticSpeed();
				speed1 = staticSpeed;
			} else {
				speed1 = ((Weight) e.weight()).profileSpeedAt(time);
			}

		} else {
			if (((Weight) e.weight()).hasProfile()) {
				speed1 = ((Weight) e.weight()).profileSpeedAt(time)
						* SpeedUtils.PROFILE_RATIO
						+ (1 - SpeedUtils.PROFILE_RATIO) * speed;
			}

		}
		return speed1;
	}

	@SuppressWarnings("unused")
	private double getRatio(double time) {
		double ratio = 1;
		double t1 = 7 * 60;
		double t2 = 11 * 60;
		double t3 = 14 * 60;
		double t4 = 18 * 60;
		if ((time > t1 && time < t2) || (time > t3 && time < t4))
			ratio = 0.7;
		return ratio;
	}
}

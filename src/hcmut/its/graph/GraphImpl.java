package hcmut.its.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GraphImpl<V, E, W extends Cloneable> implements Graph<V, E, W> {

	private final Map<Vertex<V>, Map<Vertex<V>, Edge<V, E, W>>> edgeMap;

	public GraphImpl() {
		this.edgeMap = new LinkedHashMap<Vertex<V>, Map<Vertex<V>, Edge<V, E, W>>>();
	}

	@Override
	public void addEdge(final Edge<V, E, W> e) {
		Vertex<V> v1 = e.source();
		Vertex<V> v2 = e.target();
		addVertex(v1);
		addVertex(v2);
		Map<Vertex<V>, Edge<V, E, W>> map = this.edgeMap.get(v1);
		if (!map.containsKey(v2)) {
			map.put(v2, e);
		}
	}

	@Override
	public void addVertex(final Vertex<V> v) {
		if (!edgeMap.containsKey(v)) {
			edgeMap.put(v, new LinkedHashMap<Vertex<V>, Edge<V, E, W>>());
		}
	}

	@Override
	public Set<Vertex<V>> vertices() {
		return edgeMap.keySet();
	}

	@Override
	public Set<Edge<V, E, W>> edges() {
		Set<Edge<V, E, W>> edges = new HashSet<Edge<V, E, W>>();
		for (Map<Vertex<V>, Edge<V, E, W>> map : edgeMap.values()) {
			for (Edge<V, E, W> e : map.values())
				edges.add(e);
		}
		return edges;
	}

	@Override
	public Set<Edge<V, E, W>> outgoingEdgesOf(final Vertex<V> v) {
		return new HashSet<Edge<V, E, W>>(edgeMap.get(v).values());
	}

	@Override
	public Set<Vertex<V>> neighborsOf(Vertex<V> v) {
		return edgeMap.get(v).keySet();
	}

	@Override
	public Edge<V, E, W> getEdge(final Vertex<V> v1, final Vertex<V> v2) {
		return edgeMap.get(v1).get(v2);
	}

	@Override
	public W getEdgeWeight(final Edge<V, E, W> e) {
		return edgeMap.get(e.source()).get(e.target()).weight();
	}

	@Override
	public W getEdgeWeight(final Vertex<V> v1, final Vertex<V> v2) {
		return edgeMap.get(v1).get(v2).weight();
	}

	@Override
	public void setEdgeWeight(final Edge<V, E, W> e, W w) {
		edgeMap.get(e.source()).get(e.target()).setWeight(w);
	}

	@Override
	public void setEdgeWeight(final Vertex<V> v1, final Vertex<V> v2, W w) {
		edgeMap.get(v1).get(v2).setWeight(w);
	}

	@Override
	public boolean containsVertex(final Vertex<V> v) {
		return edgeMap.containsKey(v);
	}

	@Override
	public boolean containsEdge(final Vertex<V> n1, final Vertex<V> n2) {
		if (edgeMap.containsKey(n1))
			return edgeMap.get(n1).containsKey(n2);
		else
			return false;
	}

	@Override
	public boolean containsEdge(final Edge<V, E, W> e) {
		return containsEdge(e.source(), e.target());
	}

	public String printVertices() {
		StringBuffer s = new StringBuffer();
		s.append("Nodes:");
		for (Vertex<V> v : vertices())
			s = s.append(" ").append(v.toString());
		return s.toString();

	}

	public String printEdges() {
		StringBuffer s = new StringBuffer();
		s.append("Edges:");
		for (Edge<V, E, W> e : edges())
			s = s.append(" ").append(e.toString());
		return s.toString();

	}

	@Override
	public String toString() {
		return printVertices() + "\n" + printEdges() + "\n";
	}

}

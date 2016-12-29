package hcmut.its.graph;

import java.util.Set;

public interface Graph<V, E, W> {

	public void addEdge(final Edge<V, E, W> e);

	public void addVertex(final Vertex<V> v);

	public Set<Vertex<V>> vertices();

	public Set<Edge<V, E, W>> edges();

	public Set<Edge<V, E, W>> outgoingEdgesOf(final Vertex<V> v);

	public Set<Vertex<V>> neighborsOf(final Vertex<V> v);

	public Edge<V, E, W> getEdge(final Vertex<V> v1, final Vertex<V> v2);

	public W getEdgeWeight(final Vertex<V> v1, final Vertex<V> v2);

	public W getEdgeWeight(final Edge<V, E, W> e);

	public void setEdgeWeight(final Edge<V, E, W> e, W w);

	public void setEdgeWeight(final Vertex<V> v1, final Vertex<V> v2, W w);

	public boolean containsVertex(final Vertex<V> v);

	public boolean containsEdge(final Vertex<V> v1, final Vertex<V> v2);

	public boolean containsEdge(final Edge<V, E, W> e);

}

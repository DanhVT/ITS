package hcmut.its.graph;

public interface Edge<V, E, W> extends Cloneable {

	public E data();

	public Vertex<V> source();

	public Vertex<V> target();

	public W weight();

	public void setWeight(W w);

	public String toString();

	public boolean equals(Object obj);

	public Edge<V, E, W> clone();

}

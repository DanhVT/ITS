package hcmut.its.graph;

import java.lang.reflect.InvocationTargetException;

public class EdgeImpl<V, E, W extends Cloneable> implements Edge<V, E, W> {

	protected final E data;

	protected final Vertex<V> source;

	protected final Vertex<V> target;

	protected W weight;

	public EdgeImpl(final E data, final Vertex<V> source, final Vertex<V> target) {
		this.data = data;
		this.source = source;
		this.target = target;
	}

	public EdgeImpl(final E data, final Vertex<V> source,
			final Vertex<V> target, W weight) {
		this(data, source, target);
		this.weight = weight;
	}

	@Override
	public E data() {
		return data;
	}

	@Override
	public Vertex<V> target() {
		return target;
	}

	@Override
	public Vertex<V> source() {
		return source;
	}

	@Override
	public W weight() {
		return weight;
	}

	@Override
	public void setWeight(W w) {
		weight = w;
	}

	@Override
	public String toString() {
		return "[" + source + "->" + target + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeImpl<?, ?, ?> other = (EdgeImpl<?, ?, ?>) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public Edge<V, E, W> clone() {
		return new EdgeImpl<V, E, W>(data, source, target, W_clone(weight));
	}

	@SuppressWarnings("unchecked")
	private W W_clone(W w) {
		try {
			for (Class<?> i : w.getClass().getInterfaces())
				if (i.getName().equals("java.lang.Cloneable"))
					for (java.lang.reflect.Method m : w.getClass().getMethods())
						if (m.getName().equals("clone")
								&& m.getParameterTypes().length == 0
								&& m.getReturnType().getName()
										.equals("java.lang.Object"))
							return (W) m.invoke(w);
		} catch (IllegalAccessException ex) {
		} // This should never happen.
		catch (InvocationTargetException ex) {
		} // This should never happen.
		return w;
	}

}

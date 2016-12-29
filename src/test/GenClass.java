package test;

import java.lang.reflect.InvocationTargetException;

public class GenClass<T extends Cloneable> implements Cloneable {

	T v;

	public GenClass(T v) {
		super();
		this.v = v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v == null) ? 0 : v.hashCode());
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
		GenClass<?> other = (GenClass<?>) obj;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		return true;
	}

	@Override
	public GenClass<T> clone() {
		return new GenClass<T>(T_clone(v));
	}

	@SuppressWarnings("unchecked")
	private T T_clone(T d) {
		try {
			for (Class<?> i : d.getClass().getInterfaces())
				if (i.getName().equals("java.lang.Cloneable"))
					for (java.lang.reflect.Method m : d.getClass().getMethods())
						if (m.getName().equals("clone")
								&& m.getParameterTypes().length == 0
								&& m.getReturnType().getName()
										.equals("java.lang.Object"))
							return (T) m.invoke(d);
		} catch (IllegalAccessException ex) {
		} // This should never happen.
		catch (InvocationTargetException ex) {
		} // This should never happen.
		return d;
	}

}

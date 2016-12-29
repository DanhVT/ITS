package hcmut.its.roadmap;

public class NodeImpl implements Node {

	private final String id;

	private final double lat;

	private final double lng;

	public NodeImpl(final String id, final double lat, final double lng) {
		this.id = id;
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public double lat() {
		return lat;
	}

	@Override
	public double lng() {
		return lng;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		NodeImpl other = (NodeImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public Node clone() {
		return new NodeImpl(id, lat, lng);
	}

	public String toString() {
		return id;
	}

}

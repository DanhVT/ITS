package hcmut.its.roadmap;

public class SegmentImpl implements Segment {

	private final String id;

	private final int cellX;

	private final int cellY;

	private final SegmentInfo info;

	public SegmentImpl(String idd, int x, int y, final SegmentInfo inf) {
		id = idd;
		cellX = x;
		cellY = y;
		info = inf;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public int cellX() {
		return cellX;
	}

	@Override
	public int cellY() {
		return cellY;
	}

	@Override
	public SegmentInfo info() {
		return info;
	}

	@Override
	public SegmentImpl clone() {
		return new SegmentImpl(id, cellX, cellY, info);
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
		SegmentImpl other = (SegmentImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

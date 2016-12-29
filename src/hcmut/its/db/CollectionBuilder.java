package hcmut.its.db;

import java.util.Date;

import com.mongodb.DBCollection;

public abstract class CollectionBuilder {

	protected String name;

	public CollectionBuilder(String name) {
		this.name = name;
	}
	
	public void drop(String name) {
		DBCollection coll = ITSDB.getCollection(name);
		coll.drop();
		System.out.println("Collection " + name + " has been droped.");
	}

	public abstract void build(String name);

	public abstract void index(String name);

	public void build() {
		long lStartTime = new Date().getTime();
		drop(name);
		index(name);
		build(name);
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		System.out.println("Time elapsed: " + difference + " s");
	}

}

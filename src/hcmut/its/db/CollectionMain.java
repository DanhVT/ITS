package hcmut.its.db;

import hcmut.its.util.DBUtils;


public class CollectionMain {

	public static void main(String[] args) {

		//NodeBuilder nodeCollection = new NodeBuilder();
		//nodeCollection.build();
		
		//StreetBuilder street = new StreetBuilder();
			//	street.build();

		SegmentBuilder segmentCollection = new SegmentBuilder();
		//segmentCollection.build();
		//segmentCollection.updateId();
		segmentCollection.updateProfile();

		//RefinedSegmentBuilder refinedSegmentCollection = new RefinedSegmentBuilder();
		//refinedSegmentCollection.build();

		//SpeedProfileBuilder speedProfileCollection = new SpeedProfileBuilder(
			//	DBUtils.getPreviousDaysOf("2014-01-27", 7));
		//speedProfileCollection.build();
		
		//RefinedSpeedProfileBuilder speedProfileCollection = new RefinedSpeedProfileBuilder();
		//speedProfileCollection.build();
		
		
		
	}

}

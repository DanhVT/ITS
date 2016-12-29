package hcmut.its.util;

import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class MapUtils {

	public static double ORIGIN_LAT = 10.609309;
	public static double ORIGIN_LNG = 106.493811;
	public static int MAX_CELL_X = 39;
	public static int MAX_CELL_Y = 39;
	public static int SAFE_FACTOR = 2;

	// in meters
	public static double MAXIMUM_DISTANCE = 1200;

	// public static double DEFAULT_BORDER_RAITO = 1;

	public static double distance(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371 * 1000;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	/**
	 * 
	 * time in minutes
	 */
	@SuppressWarnings("deprecation")
	public static int getCurrentTime() {
		int hour, minute;
		Date today = new Date();
		hour = today.getHours();
		minute = today.getMinutes();
		return hour * 60 + minute;

	}

	public static String getCurrentDate() {
		Date today = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(today);
	}

	// time in minutes
	public static int time2Frame(double time) {
		return (int) (getHour(time) * 4 + Math.floor(getMinute(time) / 15));
	}

	public static int getHour(double time) {
		return (int) Math.floor(time / 60);
	}

	public static int getMinute(double time) {
		return (int) Math.floor(time - getHour(time) * 60);
	}

	public static int getCellX(double lat, double lng) {
		Double d = (lat - ORIGIN_LAT) / 0.01;
		return d.intValue();
	}

	public static int getCellY(double lat, double lng) {
		Double d = (lng - ORIGIN_LNG) / 0.01;
		return d.intValue();
	}

	/*
	 * return a[4] in which (a[0], a[1]) is "begin" cell, (a[2], a[3]) is "end"
	 * cell
	 */
	public static int[] getSearchingBox(double lat1, double lng1, double lat2,
			double lng2) {
		Double a = (lat1 - ORIGIN_LAT) / 0.01;
		int x1 = a.intValue();
		Double b = (lng1 - ORIGIN_LNG) / 0.01;
		int y1 = b.intValue();
		Double c = (lat2 - ORIGIN_LAT) / 0.01;
		int x2 = c.intValue();
		Double d = (lng2 - ORIGIN_LNG) / 0.01;
		int y2 = d.intValue();

		return getSearchingBox1(x1, y1, x2, y2);
	}

	public static double[] getBox(double lat, double lng) {
		double radius = 0.6 / 111.0; // sqrt(2)
		double[] box = new double[4];
		box[0] = lat - radius;
		box[1] = lng - radius;
		box[2] = lat + radius;
		box[3] = lng + radius;
		return box;
	}

	// safe array size
	public static boolean inBox(Vertex<Node> v, double[] box) {
		return v.data().lat() >= box[0] && v.data().lat() <= box[2]
				&& v.data().lng() >= box[1] && v.data().lng() <= box[3];
	}

	public static boolean inBox(Edge<Node, Segment, Weight> e, double[] box) {
		return inBox(e.source(), box) || inBox(e.target(), box);
	}

	private static int[] getSearchingBox1(int x1, int y1, int x2, int y2) {
		int[] rs = new int[4];
		int dx = Math.abs(x1 - x2);
		int dy = Math.abs(y1 - y2);
		double raito = (double) Math.max(dx, 1) / (double) Math.max(dy, 1);
		int safe = (int) Math.ceil((double) Math.abs(dx - dy)
				/ (double) SAFE_FACTOR);

		rs[0] = Math.max(Math.min(x1, x2) - 1, 0);
		rs[1] = Math.max(Math.min(y1, y2) - 1, 0);
		rs[2] = Math.min(Math.max(x1, x2) + 1, MAX_CELL_X);
		rs[3] = Math.min(Math.max(y1, y2) + 1, MAX_CELL_Y);

		if (raito > 2) {
			rs[1] = Math.max(rs[1] - safe - 1, 0);
			rs[3] = Math.min(rs[3] + safe + 1, MAX_CELL_Y);
		} else if (raito < 0.5) {
			rs[0] = Math.max(rs[0] - safe - 1, 0);
			rs[2] = Math.min(rs[2] + safe + 1, MAX_CELL_X);
		}
		return rs;
		// return new int[]{0,0,39,39};
	}

	public static double kph2Mpm(double v) {
		return v * 1000 / 60;
	}

	public static boolean isEdgeInSearchingBox(int[] box,
			Edge<Node, Segment, Weight> e) {
		int beginCellX = box[0];
		int beginCellY = box[1];
		int endCellX = box[2];
		int endCellY = box[3];
		return e.data().cellX() >= beginCellX && e.data().cellX() <= endCellX
				&& e.data().cellY() >= beginCellY
				&& e.data().cellY() <= endCellY;
	}

	public static boolean isVertexInSearchingBox(int[] box, Vertex<Node> v) {
		Double a = (v.data().lat() - ORIGIN_LAT) / 0.01;
		int x = a.intValue();
		Double b = (v.data().lng() - ORIGIN_LNG) / 0.01;
		int y = b.intValue();
		int beginCellX = box[0];
		int beginCellY = box[1];
		int endCellX = box[2];
		int endCellY = box[3];
		return x >= beginCellX && x <= endCellX && y >= beginCellY
				&& y <= endCellY;
	}

	public static double distanceFromLine(double x1, double y1, double x2,
			double y2, double x, double y) {
		double a = y2 - y1;
		double b = -(x2 - x1);
		double c = -a * x1 - b * y1;
		// (x2 - x1)(y - y1) = (y2- y1)(x - x1)
		return Math.abs(a * x + b * y + c)
				/ Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}

	public static double[] pointFromLine(double x1, double y1, double x2,
			double y2, double x, double y) {
		double[] point = new double[2];
		double d = distanceFromLine(x1, y1, x2, y2, x, y);
		double a = y2 - y1;
		double b = -(x2 - x1);
		double c = -a * x1 - b * y1;
		double tanx = Math.abs((x2 - x1) / (y2 - y1));
		double tany = Math.abs((y2 - y1) / (x2 - x1));
		int flagX = x <= -c / a ? 1 : -1;
		int flagY = y <= -c / b ? 1 : -1;
		double dx = d * Math.cos(Math.atan(tanx)) * flagX;
		double dy = d * Math.cos(Math.atan(tany)) * flagY;
		point[0] = x + dx;
		point[1] = y + dy;
		return point;
	}

	public static double[] intersectionPointFromLine(double x1, double y1,
			double x2, double y2, double x, double y) {
		double[] point = new double[2];
		double dd = 0.2 / 111.0; // sqrt(2)

		// (y2- y1)(y - y1) = (x1 - x2)(x - x1)
		// Qua A vtpt AB
		double a = x1 - x2;
		double b = y1 - y2;
		double c = -a * x - b * y;

		double x3 = x - dd;
		double x4 = x + dd;
		double y3 = (-c - a * x3) / b;
		double y4 = (-c - a * x4) / b;

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		// always safe
		if (d != 0) {

			double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2)
					* (x3 * y4 - y3 * x4))
					/ d;
			double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2)
					* (x3 * y4 - y3 * x4))
					/ d;

			point[0] = xi;
			point[1] = yi;
		}
		return point;
	}

	public static double nodeMatchSegment(double sLat, double sLng,
			double dLat, double dLng, double lat, double lng, String streetType) {

		double width;
		double[] width_of = new double[] { 15.0, 10.0, 10.0, 10.0 };
		double DISTANCE_UNIT = 111000.0;

		switch (streetType) {
		case "primary":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "secondary":
			width = (width_of[1] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "tertiary":
			width = (width_of[2] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "motorway":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "motorway_link":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "trunk":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "trunk_link":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		case "primary_link":
			width = (width_of[0] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
			break;
		default:
			width = (width_of[3] + MAXIMUM_DISTANCE) / DISTANCE_UNIT;
		}

		double d_node_AB = distanceFromLine(sLat, sLng, dLat, dLng, lat, lng);

		// (y2- y1)(y - y1) = (x1 - x2)(x - x1)
		// Qua A vtpt AB
		double a1 = sLat - dLat;
		double b1 = sLng - dLng;
		double c1 = -a1 * sLat - b1 * sLng;

		double a2 = sLat - dLat;
		double b2 = sLng - dLng;
		double c2 = -a2 * dLat - b2 * dLng;

		if (Double.compare(d_node_AB, width / 2) > 0)
			return -1;

		if ((a1 * lat + b1 * lng + c1) * (a2 * lat + b2 * lng + c2) > 0)
			return -1;

		return d_node_AB;
	}

	public static void main(String[] args) {
		// System.out.println(getHour(610)); System.out.println(getMinute(610));
		// System.out.println(time2Frame(610));

		// System.out.print(distanceFromLine(10.8629199, 106.7838492,
		// 10.8629595,
		// 106.7837372, 10.8629595, 106.7837372));
		// System.out.println(nodeMatchSegment(10.8629199, 106.7838492,
		// 10.8629595, 106.7837372, 10.8629595, 106.7837372, ""));

		// int[] box = getSearchingBox1(10, 30, 11, 10);
		// System.out.println(box[0] + " " + box[1] + " " + box[2] + " " +
		// box[3]);

		double[] point = intersectionPointFromLine(10.7654638, 106.6629789,
				10.7657489, 106.6635132, 10.765699481130758, 106.66315913200378);
		System.out.println(point[0]);
		System.out.println(point[1]);
		int q = 2;
		int k = 3;
		System.out.println(Math.ceil((double) q / (double) k));
	}

}

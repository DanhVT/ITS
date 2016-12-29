package hcmut.its.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBUtils {

	//private static int NUMBER_OF_DAYS = 5;

	public static List<String> getPreviousWeekDaysOf(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> dateList;
		List<String> result = new ArrayList<String>();
		try {
			dateList = getPreviousWeekDaysOf(sdf.parse(date), n);
			for (Date d : dateList)
				result.add(sdf.format(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static List<Date> getPreviousWeekDaysOf(Date date, int n) {
		Calendar originalDate = Calendar.getInstance();
		originalDate.setTime(date);
		List<Date> list = new ArrayList<Date>();
		for (int i = 1; i <= n; i++) {
			Calendar previousWeekDay = (Calendar) originalDate.clone();
			previousWeekDay.add(Calendar.WEEK_OF_YEAR, -i);
			list.add(previousWeekDay.getTime());
		}
		return list;

	}
	
	public static List<String> getPreviousDaysOf(String date, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> dateList;
		List<String> result = new ArrayList<String>();
		try {
			dateList = getPreviousDaysOf(sdf.parse(date), n);
			for (Date d : dateList)
				result.add(sdf.format(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static List<Date> getPreviousDaysOf(Date date, int n) {
		Calendar originalDate = Calendar.getInstance();
		originalDate.setTime(date);
		List<Date> list = new ArrayList<Date>();
		for (int i = 1; i <= n; i++) {
			Calendar previousWeekDay = (Calendar) originalDate.clone();
			previousWeekDay.add(Calendar.DAY_OF_YEAR, -i);
			list.add(previousWeekDay.getTime());
		}
		return list;

	}
	
	public static void main(String[] args) {
		for(String a: getPreviousDaysOf("2014-02-07", 5))
			System.out.println(a);
	}

}

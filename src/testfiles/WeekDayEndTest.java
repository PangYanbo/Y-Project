package testfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.ut.csis.pflow.geom.STPoint;

public class WeekDayEndTest {

	/*
	 * TEST for weekend/end decider
	 * TESTED POSITIVE!
	 * 
	 */
	
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	
	public static void main(String args[]) throws ParseException{
		Date dt = SDF_TS2.parse("2014-07-27 20:31:00");
		STPoint stp = new STPoint(dt,100.0,90.0);
		String youbi = (new SimpleDateFormat("u")).format(stp.getTimeStamp());
		System.out.println(youbi);
	}
	
}

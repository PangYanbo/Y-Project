package testfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.ut.csis.pflow.geom.STPoint;

public class STPointtest {

	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static void main(String args[]) throws ParseException{
		Double lon = 100.01;
		Double lat = 99.9;
		Date date = SDF_TS2.parse("2015-05-01 18:08:01");
		STPoint p = new STPoint (date,lon,lat);
		System.out.println(p);
		
	}
	
}

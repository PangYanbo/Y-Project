package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class SPFinder {

	/*
	 * class for finding number of staypoints other than home/office
	 */
	
	public static void main(String args[]){
		
	}
	
	/*
	 * planning... 
	 * 1. sort all logs into map [id | day+time | lonlat]
	 * 2. for each id, 
	 * 	3. for each day, 
	 * 	 4. identify "stay points"(-->5)
	 * 
	 * 5. sort all logs by time
	 * 6. check if current point is <2km compared to point before
	 * 	7. if so, put the time and loc. of the point before
	 *  8. if the point is same for over 30 mins., its a staypoint
	 * 
	 */
	
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	
	public static HashMap<String, HashMap<Date, LonLat>> intomap(String in) throws IOException, ParseException{
		File infile = new File("in");
		BufferedReader br = new BufferedReader(new FileReader(infile));
		HashMap<String, HashMap<Date, LonLat>> res = new HashMap<String, HashMap<Date, LonLat>>();
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			String dt = tokens[3];
			Date date = SDF_TS2.parse(dt);
			Double lon = Double.parseDouble(tokens[2]);
			Double lat = Double.parseDouble(tokens[1]);
			if(res.containsKey(id)){
				res.get(id).put(date, new LonLat(lon,lat));
			}
			else{
				HashMap<Date, LonLat> map = new HashMap<Date, LonLat>();
				map.put(date, new LonLat(lon,lat));
				res.put(id, map);
			}
		}
		br.close();
		return res;
	}

	public static ArrayList<LonLat> staypoints(HashMap<Date, LonLat> map){
		ArrayList<LonLat> res = new ArrayList<LonLat>();
		
		return res;
	}
}

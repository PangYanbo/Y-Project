package Motif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import DataModify.Over8TimeSlots;
import jp.ac.ut.csis.pflow.geom.STPoint;

public class MotifFinder3 {

	/**
	 * for motifs 
	 * @10.07.2015
	 * by Taka Yabe
	 * 
	 * 1. for each ID-day, delete if it is less than 7 timeslots
	 * 2. delete points where distance from previous point is >300m
	 * 3. delete points where start and end points in same staypoint is <10mins@
	 * 4. get staypoints 
	 * 5. get stay regions by clustering staypoints with r=300m
	 * 6. check if any stayregions are the same --> change the chain number
	 *  
	 */
	
	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS3 = new SimpleDateFormat("dd");//change time format
	
	public static void getMotif(String in) throws NumberFormatException, ParseException, IOException{
		File infile = new File(in);
		HashMap<String, ArrayList<STPoint>> alldatamap = sortintoMapY(infile);
		HashMap<String, ArrayList<String>> id_days = Over8TimeSlots.OKAY_id_days(in);
		
		
		
	}
	
	//infile = data for exp (contains data for all days)
	public static HashMap<String, ArrayList<STPoint>> sortintoMapY(File infile) throws ParseException, NumberFormatException, IOException{
		HashMap<String, ArrayList<STPoint>> id_count = new HashMap<String, ArrayList<STPoint>>();
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = null;
		while ((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			Date dt = SDF_TS2.parse(tokens[3]);
			STPoint point = new STPoint(dt,Double.parseDouble(tokens[2]),Double.parseDouble(tokens[1]));
			if(id_count.containsKey(id)){
				id_count.get(id).add(point);
			}
			else{
				ArrayList<STPoint> list = new ArrayList<STPoint>();
				list.add(point);
				id_count.put(id, list);
			}
		}
		br.close();	
		return id_count;
	}
	
}

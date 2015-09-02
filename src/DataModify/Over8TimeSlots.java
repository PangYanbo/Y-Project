package DataModify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import StayPointDetection.StayPointTools;

public class Over8TimeSlots {
	
	public static void main(String args[]) throws IOException, ParseException{
//		String in = "c:/users/yabetaka/Desktop/dataforExp.csv";
//		HashMap<String, ArrayList<String>> map = OKAY_id_days(in);
	}

	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS3 = new SimpleDateFormat("dd");//change time format

	public static HashMap<String, HashMap<String, ArrayList<Integer>>> id_day_arrayTime(String in) throws IOException, ParseException{
		File infile = new File(in);
		HashMap<String, HashMap<String, ArrayList<Integer>>> id_day_points = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = null;
		while ((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			Date dt = SDF_TS2.parse(tokens[3]);
			String day = SDF_TS3.format(dt);
			String time = SDF_TS.format(dt);
			Integer t = StayPointTools.converttoSecs(time);
			if(id_day_points.containsKey(id)){
				if(id_day_points.get(id).containsKey(day)){
					id_day_points.get(id).get(day).add(t);
				}
				else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					list.add(t);
					id_day_points.get(id).put(day, list);
				}
			}
			else{
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(t);
				HashMap<String, ArrayList<Integer>> tempmap = new HashMap<String, ArrayList<Integer>>();
				tempmap.put(day, list);
				id_day_points.put(id, tempmap);
			}
		}
		br.close();			
		return id_day_points;
	}

	public static boolean numberofSlots(ArrayList<Integer> list){
		HashSet<Integer> set = new HashSet<Integer>();
		Integer sho = 0;
		for(Integer t : list){
			sho = t/1800;
			set.add(sho);
		}
		if(set.size()>=8){
			return true;
		}
		else{
			return false;
		}
	}

	public static HashMap<String, ArrayList<String>> OKAY_id_days(String in) throws IOException, ParseException{
		HashMap<String, ArrayList<String>> id_days = new HashMap<String, ArrayList<String>>();
		HashMap<String, HashMap<String, ArrayList<Integer>>> id_day_array =  id_day_arrayTime(in);
		for(String id : id_day_array.keySet()){
			for(String day : id_day_array.get(id).keySet()){
				if(numberofSlots(id_day_array.get(id).get(day))==true){
					if(id_days.containsKey(id)){
						id_days.get(id).add(day);
					}
					else{
						ArrayList<String> list = new ArrayList<String>();
						list.add(day);
						id_days.put(id, list);
					}
				}
			}
		}
		return id_days;
	}


}

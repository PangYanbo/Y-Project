package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class SPFinder {

	/*
	 * class for finding number of staypoints other than home/office
	 * 
	 * param:
	 * args[0] : file for all data
	 * args[1] : file for output
	 * 
	 */

	public static void main(String args[]) throws IOException, ParseException{
		HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> map = intomap(args[0]);
		HashMap<String,HashMap<String,Integer>> res = new HashMap<String,HashMap<String, Integer>>();
		for(String id : map.keySet()){
			HashMap<String,Integer> temp = new HashMap<String,Integer>();
			for(String day : map.get(id).keySet()){
				Integer sps = staypoints(map.get(id).get(day));
				temp.put(day, sps);
				res.put(id,temp);
			}
		}
		writeout(res,args[1]);
	}

	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> intomap(String in) throws IOException, ParseException{
		File infile = new File("in");
		BufferedReader br = new BufferedReader(new FileReader(infile));
		HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> res = new HashMap<String, HashMap<String, HashMap<Integer, LonLat>>>();
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			String dt = tokens[3];
			String day = dt.substring(8,10);
			Integer time = converttoSecs(dt.substring(11,19));
			Double lon = Double.parseDouble(tokens[2]);
			Double lat = Double.parseDouble(tokens[1]);
			if(res.containsKey(id)){
				if(res.get(id).containsKey(day)){
					res.get(id).get(day).put(time, new LonLat(lon,lat));
				}
				else{
					HashMap<Integer, LonLat> smap = new HashMap<Integer, LonLat>();
					smap.put(time, new LonLat(lon,lat));
					res.get(id).put(day, smap);
				}
			}
			else{
				HashMap<Integer, LonLat> map2 = new HashMap<Integer, LonLat>();
				map2.put(time, new LonLat(lon,lat));
				HashMap<String, HashMap<Integer, LonLat>> map3 = new HashMap<String, HashMap<Integer, LonLat>>();
				map3.put(day, map2);
				res.put(id, map3);
			}
		}
		br.close();
		return res;
	}

	public static Integer staypoints(HashMap<Integer, LonLat> map){
		int count = 0;
		HashMap<Integer, LonLat> sortedmap = sortbyTime(map);
		LonLat startpoint = new LonLat(0,0);
		Integer starttime = 0;
		Integer endtime = 0;
		for(Integer t : sortedmap.keySet()){
			if(sortedmap.get(t).distance(startpoint)<2000){
				endtime = t;
			}
			else{
				if(endtime-starttime>1800){
					count++;
				}
				startpoint = sortedmap.get(t);
				starttime = t;
			}
		}
		return count;
	}

	public static HashMap<Integer, LonLat> sortbyTime(HashMap<Integer, LonLat> map){
		HashMap<Integer, LonLat> res = new HashMap<Integer, LonLat>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(Integer d : map.keySet()){
			list.add(d);
		}
		Collections.sort(list);
		for(Integer d2 : map.keySet()){
			res.put(d2, map.get(d2));
		}
		return res;
	}

	public static int converttoSecs(String time){
		String[] tokens = time.split(":");
		int hour = Integer.parseInt(tokens[0]);
		int min  = Integer.parseInt(tokens[1]);
		int sec  = Integer.parseInt(tokens[2]);

		int totalsec = hour*3600+min*60+sec;		
		return totalsec;
	}
	
	public static File writeout(HashMap<String, HashMap<String, Integer>> map, String outpath) throws IOException{
		File out = new File(outpath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			for(String day : map.get(id).keySet()){
				bw.write(id + "\t" + day + "\t" + map.get(id).get(day));
				bw.newLine();
			}
		}
		bw.close();
		return out;
	}
	
}

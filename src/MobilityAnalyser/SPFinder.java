package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
		HashMap<String,HashMap<String,ArrayList<LonLat>>> res = getAllIDsSP(map);

		Tools.writeout(res,args[1]);
	}

	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> intomap(String in) throws IOException, ParseException{
		int count=0;
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> res = new HashMap<String, HashMap<String, HashMap<Integer, LonLat>>>();
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String dt = tokens[1];
			String day = dt.substring(8,10);
			Integer time = Tools.converttoSecs(dt.substring(11,19));
			Double lon = Double.parseDouble(tokens[2]);
			Double lat = Double.parseDouble(tokens[3]);
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
			count++;
			if(count%1000000==0){
				System.out.println("#done sorting " + count);
			}
		}
		br.close();
		return res;
	}

	//for all IDs [time | staypoint]
	public static ArrayList<LonLat> staypoints(HashMap<Integer, LonLat> map){
		ArrayList<LonLat> res = new ArrayList<LonLat>();
		ArrayList<Integer> sortedtime = sortbyTime(map);
		LonLat startpoint = new LonLat(130.0,45.0);
		Integer starttime = 0;
		Integer endtime = 0;
		ArrayList<LonLat> temp = new ArrayList<LonLat>();
		for(Integer t : sortedtime){
			if(map.get(t).distance(startpoint)<2000){
				endtime = t;
				temp.add(map.get(t));
				LonLat sp = Tools.getave(temp);
				startpoint = sp;
			}
			else if ((map.get(t).distance(startpoint)>=2000)&&(temp.size()>1)){
				if(endtime-starttime>600){
					LonLat sp = Tools.getave(temp);
					res.add(sp);
					temp.clear();
				}
				else{
					temp.clear();
				}
			}
			else{
				temp.clear();
				startpoint = map.get(t);
				starttime = t;
				temp.add(startpoint);
			}
		}
		LonLat sp = Tools.getave(temp);
		res.add(sp);
		return res;
	}

	public static ArrayList<Integer> sortbyTime(HashMap<Integer, LonLat> map){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(Integer d : map.keySet()){
			list.add(d);
		}
		Collections.sort(list);
		return list;
	}
	public static HashMap<String,HashMap<String,ArrayList<LonLat>>> getAllIDsSP
	(HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> map){
		System.out.println("#start getting SPs for " + map.size() + " IDs");
		int allday_id = 0;
		int none = 0;
		ArrayList<Integer> bugcheck = new ArrayList<Integer>();
		HashMap<String,HashMap<String,ArrayList<LonLat>>> res = new HashMap<String,HashMap<String,ArrayList<LonLat>>>();
		for(String id : map.keySet()){
			HashMap<String,ArrayList<LonLat>> temp = new HashMap<String,ArrayList<LonLat>>();
			for(String day : map.get(id).keySet()){
				if(map.get(id).get(day).size()>10){
					ArrayList<LonLat> sps = staypoints(map.get(id).get(day));
					if(sps.size()>0){
						temp.put(day, sps);
						res.put(id,temp);
					}
					allday_id++;
				}
			}
		}
		System.out.println("#finished getting SPs for " + map.size() + " IDs");
		System.out.println("#out of " + allday_id + ", " + none + " had no SPs");
		int bugsum = 0;
		for(Integer z : bugcheck){
			bugsum = bugsum+z;
		}
		//		double buglogs = (double)bugsum/(double)bugcheck.size();
		//		System.out.println("#average logs of none SPs were " + buglogs);
		return res;
	}

}

package MobilityAnalyser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Analyzer {

	public static void main(String args[]) throws NumberFormatException, IOException, ParseException{
		
		/*
		 * param 
		 * args[0] : file for all logs
		 * args[1] : file for id_home
		 * args[2] : file for id_office
		 * args[3] : filepath for output directory
		 *  
		 */
		
		File in = new File(args[0]);
		File Home = new File(args[1]);
		File Office = new File(args[2]);
		String outputpath = args[3];
		
		HashMap<String,HashMap<String,ArrayList<Integer>>> hmap = HomeOfficeMaps.getLogsnearX(in,Home);
		HashMap<String,HashMap<String,ArrayList<Integer>>> omap = HomeOfficeMaps.getLogsnearX(in,Office);
		
		HashMap<String, HashMap<String, Integer>> officeenter = officeEnterTime(omap);
		HashMap<String, HashMap<String, Integer>> officeexit  = officeExitTime(omap);
		HashMap<String, HashMap<String, Integer>> homeexit    = homeExitTimes(hmap, officeenter);
		HashMap<String, HashMap<String, Integer>> homereturn  = homeReturnTimes(hmap,officeexit);
		HashMap<String, HashMap<String, Integer>> tsukintime  = tsuukinTimes(homeexit, officeenter);
		HashMap<String, HashMap<String, Integer>> kitakutime  = kitakujikanTimes(officeexit, homereturn);
		HashMap<String, HashMap<String, Integer>> officetime  = officeStayTimes(officeenter, officeexit);
		
		writeout(officeenter, outputpath, "office_enter.txt");
		writeout(officeexit,  outputpath, "office_exit.txt");
		writeout(homeexit,    outputpath, "home_exit.txt");
		writeout(homereturn,  outputpath, "home_return.txt");
		writeout(tsukintime,  outputpath, "tsukin_time.txt");
		writeout(kitakutime,  outputpath, "kitaku_time.txt");
		writeout(officetime,  outputpath, "office_time.txt");
		
	}
	
	public static HashMap<String, HashMap<String, Integer>> officeEnterTime(HashMap<String,HashMap<String,ArrayList<Integer>>> omap){
		HashMap<String, HashMap<String, Integer>> OEntertimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : omap.keySet()){
			HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
			for(String day : omap.get(id).keySet()){
				ArrayList<Integer> list = omap.get(id).get(day);
				Collections.sort(list);
				day_lastlog.put(day, list.get(0));
			}
			OEntertimes.put(id, day_lastlog);
		}
		return OEntertimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> officeExitTime(HashMap<String,HashMap<String,ArrayList<Integer>>> omap){
		HashMap<String, HashMap<String, Integer>> OExittimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : omap.keySet()){
			HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
			for(String day : omap.get(id).keySet()){
				ArrayList<Integer> list = omap.get(id).get(day);
				Collections.sort(list);
				Collections.reverse(list);
				day_lastlog.put(day, list.get(0));
			}
			OExittimes.put(id, day_lastlog);
		}
		return OExittimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> homeExitTimes
	(HashMap<String,HashMap<String,ArrayList<Integer>>> hmap, HashMap<String, HashMap<String, Integer>> officeEnterTime){
		HashMap<String, HashMap<String, Integer>> shukkintimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : hmap.keySet()){
			HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
			for(String day : hmap.get(id).keySet()){
				Integer shukkintime = officeEnterTime.get(id).get(day);
				ArrayList<Integer> list = hmap.get(id).get(day);
				ArrayList<Integer> newlist = new ArrayList<Integer>();
				for (Integer log : list){
					if(log < shukkintime){
						newlist.add(log);
					}
				}
				Collections.sort(newlist);
				Collections.reverse(newlist);
				day_lastlog.put(day, list.get(0));
			}
			shukkintimes.put(id, day_lastlog);
		}
		return shukkintimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> homeReturnTimes
	(HashMap<String,HashMap<String,ArrayList<Integer>>> hmap, HashMap<String, HashMap<String, Integer>> officeExitTime){
		HashMap<String, HashMap<String, Integer>> kitakutimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : hmap.keySet()){
			HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
			for(String day : hmap.get(id).keySet()){
				Integer exittime = officeExitTime.get(id).get(day);
				ArrayList<Integer> list = hmap.get(id).get(day);
				ArrayList<Integer> newlist = new ArrayList<Integer>();
				for (Integer log : list){
					if(log > exittime){
						newlist.add(log);
					}
				}
				Collections.sort(newlist);
				day_lastlog.put(day, list.get(0));
			}
			kitakutimes.put(id, day_lastlog);
		}
		return kitakutimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> tsuukinTimes
	(HashMap<String, HashMap<String, Integer>> homeexittime, HashMap<String, HashMap<String, Integer>> officeentertime){
		HashMap<String, HashMap<String, Integer>> tsuukintimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : homeexittime.keySet()){
			for(String day : homeexittime.get(id).keySet()){
				int time = officeentertime.get(id).get(day) - homeexittime.get(id).get(day);
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put(day, time);
				tsuukintimes.put(id, map);
			}
		}
		return tsuukintimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> kitakujikanTimes
	(HashMap<String, HashMap<String, Integer>> officeexittime, HashMap<String, HashMap<String, Integer>> homereturntime){
		HashMap<String, HashMap<String, Integer>> kitakujikantimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : officeexittime.keySet()){
			for(String day : officeexittime.get(id).keySet()){
				int time = homereturntime.get(id).get(day) - officeexittime.get(id).get(day);
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put(day, time);
				kitakujikantimes.put(id, map);
			}
		}
		return kitakujikantimes;
	}
	
	public static HashMap<String, HashMap<String, Integer>> officeStayTimes
	(HashMap<String, HashMap<String, Integer>> officeentertime, HashMap<String, HashMap<String, Integer>> officeexittime){
		HashMap<String, HashMap<String, Integer>> officestaytimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : officeexittime.keySet()){
			for(String day : officeexittime.get(id).keySet()){
				int time = officeexittime.get(id).get(day) - officeentertime.get(id).get(day);
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put(day, time);
				officestaytimes.put(id, map);
			}
		}
		return officestaytimes;
	}
	
	public static File writeout(HashMap<String, HashMap<String, Integer>> map, String path, String name) throws IOException{
		File out = new File(path+name);
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

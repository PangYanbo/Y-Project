package MobilityAnalyser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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

		//		File in = new File(args[0]);
		//		File Home = new File(args[1]);
		//		File Office = new File(args[2]);
		//		String outputpath = args[3];

		File in = new File("c:/users/yabetaka/desktop/dataforexp.csv");
		File Home = new File("c:/users/yabetaka/desktop/id_home.csv");
		File Office = new File("c:/users/yabetaka/desktop/id_office.csv");
		String outputpath = "c:/users/yabetaka/desktop/TestExp0808/";

		HashMap<String,HashMap<String,ArrayList<Integer>>> hmap = HomeOfficeMaps.getLogsnearX(in,Home);
		System.out.println("#done getting logs near home");
		HashMap<String,HashMap<String,ArrayList<Integer>>> omap = HomeOfficeMaps.getLogsnearX(in,Office);
		System.out.println("#done getting logs near office");

		HashMap<String, HashMap<String, Integer>> officeenter = officeEnterTime(omap); System.out.println("#done getting office enter time");
		HashMap<String, HashMap<String, Integer>> officeexit  = officeExitTime(omap); System.out.println("#done getting office exit time");
		HashMap<String, HashMap<String, Integer>> homeexit    = homeExitTimes(hmap, officeenter); System.out.println("#done getting home exit time");
		HashMap<String, HashMap<String, Integer>> homereturn  = homeReturnTimes(hmap,officeexit); System.out.println("#done getting home return time");
		HashMap<String, HashMap<String, Integer>> tsukintime  = tsuukinTimes(homeexit, officeenter); System.out.println("#done getting tsuukin time");
		HashMap<String, HashMap<String, Integer>> kitakutime  = kitakujikanTimes(officeexit, homereturn); System.out.println("#done getting kitaku times");
		HashMap<String, HashMap<String, Integer>> officetime  = officeStayTimes(officeenter, officeexit); System.out.println("#done getting time at office");

		System.out.println("#writing everything out...");
		writeout(officeenter, outputpath, "office_enter.csv");
		writeout(officeexit,  outputpath, "office_exit.csv");
		writeout(homeexit,    outputpath, "home_exit.csv");
		writeout(homereturn,  outputpath, "home_return.csv");
		writeout(tsukintime,  outputpath, "tsukin_time.csv");
		writeout(kitakutime,  outputpath, "kitaku_time.csv");
		writeout(officetime,  outputpath, "office_time.csv");
		System.out.println("#done everything");

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
			if(officeEnterTime.containsKey(id)){
				HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
				for(String day : hmap.get(id).keySet()){
					if(officeEnterTime.get(id).containsKey(day)){
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
				}
				shukkintimes.put(id, day_lastlog);
			}
		}
		return shukkintimes;
	}

	public static HashMap<String, HashMap<String, Integer>> homeReturnTimes
	(HashMap<String,HashMap<String,ArrayList<Integer>>> hmap, HashMap<String, HashMap<String, Integer>> officeExitTime){
		HashMap<String, HashMap<String, Integer>> kitakutimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : hmap.keySet()){
			if(officeExitTime.containsKey(id)){
				HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
				for(String day : hmap.get(id).keySet()){
					if(officeExitTime.get(id).containsKey(day)){
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
				}
				kitakutimes.put(id, day_lastlog);
			}
		}
		return kitakutimes;
	}

	public static HashMap<String, HashMap<String, Integer>> tsuukinTimes
	(HashMap<String, HashMap<String, Integer>> homeexittime, HashMap<String, HashMap<String, Integer>> officeentertime){
		HashMap<String, HashMap<String, Integer>> tsuukintimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : homeexittime.keySet()){
			if(officeentertime.containsKey(id)){
				for(String day : homeexittime.get(id).keySet()){
					if(officeentertime.get(id).containsKey(day)){
						int time = officeentertime.get(id).get(day) - homeexittime.get(id).get(day);
						if(tsuukintimes.containsKey(id)){
							tsuukintimes.get(id).put(day, time);
						}
						else{
							HashMap<String, Integer> map = new HashMap<String, Integer>();
							map.put(day, time);
							tsuukintimes.put(id, map);
						}
					}
				}
			}
		}
		return tsuukintimes;
	}

	public static HashMap<String, HashMap<String, Integer>> kitakujikanTimes
	(HashMap<String, HashMap<String, Integer>> officeexittime, HashMap<String, HashMap<String, Integer>> homereturntime){
		HashMap<String, HashMap<String, Integer>> kitakujikantimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : officeexittime.keySet()){
			if(homereturntime.containsKey(id)){
				for(String day : officeexittime.get(id).keySet()){
					if(homereturntime.get(id).containsKey(day)){
						int time = homereturntime.get(id).get(day) - officeexittime.get(id).get(day);
						if(kitakujikantimes.containsKey(id)){
							kitakujikantimes.get(id).put(day, time);
						}
						else{
							HashMap<String, Integer> map = new HashMap<String, Integer>();
							map.put(day, time);
							kitakujikantimes.put(id, map);
						}
					}
				}
			}
		}
		return kitakujikantimes;
	}

	public static HashMap<String, HashMap<String, Integer>> officeStayTimes
	(HashMap<String, HashMap<String, Integer>> officeentertime, HashMap<String, HashMap<String, Integer>> officeexittime){
		HashMap<String, HashMap<String, Integer>> officestaytimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : officeexittime.keySet()){
			if(officeentertime.containsKey(id)){
				for(String day : officeexittime.get(id).keySet()){
					if(officeentertime.get(id).containsKey(day)){
						int time = officeexittime.get(id).get(day) - officeentertime.get(id).get(day);
						if(officestaytimes.containsKey(id)){
							officestaytimes.get(id).put(day, time);
						}
						else{
							HashMap<String, Integer> map = new HashMap<String, Integer>();
							map.put(day, time);
							officestaytimes.put(id, map);
						}
					}
				}
			}
		}
		return officestaytimes;
	}

	public static File writeout(HashMap<String, HashMap<String, Integer>> map, String path, String name) throws IOException{
		File out = new File(path+name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			for(String day : map.get(id).keySet()){
				double time = (double)map.get(id).get(day)/(double)3600;
				BigDecimal x = new BigDecimal(time);
				x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
				bw.write(id + "," + day + "," + x);
				bw.newLine();
			}
		}
		bw.close();
		return out;
	}

}

package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AlertsbyAreaCode {

	/**
	 * @param args
	 */

	public static String type = "rain";
//	public static String ymd = "2015/5/12";
//	public static String targetcode = "9999";

	public static void main(String args[]) throws IOException{

		File in = new File("c:/users/yabetaka/Desktop/bbb.csv");
		HashMap<String, HashMap<String, HashMap<String, String>>> alertsbyarea = alertsineacharea(in , type);
		System.out.println("done getting map " + alertsbyarea.size());

//		String yyyymmdd = ymd.split("/")[0]+ymd.split("/")[1]+ymd.split("/")[2];
//		File out = new File("c:/users/yabetaka/desktop/alerttest"+type+"_"+yyyymmdd+"_"+targetcode+".csv");
		File out = new File("c:/users/yabetaka/desktop/onlylevel4.csv");
		writeout(alertsbyarea, out);
	}


	public static HashMap<String, HashMap<String, HashMap<String, String>>> alertsineacharea(File in, String tp) throws IOException{
		HashMap<String, HashMap<String, HashMap<String, String>>> res = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		// Date, Code, Time, Level
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String date = tokens[0].split(" ")[0]; // yyyy/mm/dd
			String time = tokens[0].split(" ")[1];
			Double hour = Double.parseDouble(time.split(":")[0]);
			Double mins = Double.parseDouble(time.split(":")[1]);
//			String doubletime = String.valueOf(hour+mins/60).substring(0,4);
			String doubletime = String.valueOf(hour+mins/60);

			String type = tokens[1];
			String level = tokens[2];
			String[] codes = tokens[3].split(" ");
			if(type.equals(tp)){
				for(String code : codes){
					if(res.containsKey(date)){
						if(res.get(date).containsKey(code)){
							res.get(date).get(code).put(doubletime, level);
						}
						else{
							HashMap<String, String> temp = new HashMap<String, String>();
							temp.put(doubletime, level);
							res.get(date).put(code, temp);
						}
					}
					else{
						HashMap<String, String> temp = new HashMap<String, String>();
						temp.put(doubletime, level);
						HashMap<String, HashMap<String, String>> tmp2 = new HashMap<String, HashMap<String, String>>();
						tmp2.put(date,temp);
						res.put(date, tmp2);
					}
				}
			}
		}
		br.close();
		return res;
	}	

	public static void alertsinday(File in, String tp) throws IOException{
		HashMap<String, HashMap<String, HashMap<String, String>>> res = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		// Date, Time, Level, numberofAreacodes
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String date = tokens[0].split(" ")[0];
			String time = tokens[0].split(" ")[1];
			Double hour = Double.parseDouble(time.split(":")[0]);
			Double mins = Double.parseDouble(time.split(":")[1]);
			String doubletime = String.valueOf(hour+mins/60).substring(0,4);
			String type = tokens[1];
			String level = tokens[2];
			String[] codes = tokens[3].split(" ");
			if(type.equals(tp)){
				for(String code : codes){
					if(res.containsKey(date)){
						if(res.get(date).containsKey(doubletime)){
							res.get(date).get(doubletime).put(level, code);
						}
						else{
							HashMap<String, String> temp = new HashMap<String, String>();
							temp.put(level, code);
							res.get(date).put(doubletime, temp);
						}
					}
					else{
						HashMap<String, String> temp = new HashMap<String, String>();
						temp.put(level, code);
						HashMap<String, HashMap<String, String>> tmp2 = new HashMap<String, HashMap<String, String>>();
						tmp2.put(doubletime,temp);
						res.put(date, tmp2);
					}
				}
			}
		}
		br.close();
	}	

	public static void writeout(HashMap<String, HashMap<String, HashMap<String, String>>> res, File out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
		//		 HashMap<String, String> time_level = res.get(ymd).get(targetcode);
		//		 for(String time : time_level.keySet()){
		//			 bw.write(time + "," + time_level.get(time));
		//			 bw.newLine();
		//		 }

		HashMap<String, ArrayList<String>> codeswithlevel4 = new HashMap<String, ArrayList<String>>();
		//Date, code
		
		for(String d : res.keySet()){
			for(String c : res.get(d).keySet()){
				if(res.get(d).get(c).containsValue("4")){
					if(codeswithlevel4.containsKey(d)){
						codeswithlevel4.get(d).add(c);
					}
					else{
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(c);
						codeswithlevel4.put(d, temp);
					}
				}
			}
		}
		System.out.println("done getting data with level 4 "+codeswithlevel4.size());
		
		for(String date : res.keySet()){
			if(codeswithlevel4.containsKey(date)){
				for(String code : res.get(date).keySet()){
					if(codeswithlevel4.get(date).contains(code)){
						for(String time : res.get(date).get(code).keySet()){
							String level = res.get(date).get(code).get(time);
							bw.write(date+","+code+","+time+","+level);
							bw.newLine();
						}
					}
				}
			}
		}
		
		bw.close();
	}

}

package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DisasterLogs {

	/*
	 * TODO: Identify if the log is in the disaster area, using gchecker from knsg plow
	 * Base file : time , type , level , jis code
	 *   
	 */

	/**
	 * @param 
	 * args[0] : disaster log file 
	 * args[1] : output path 
	 * args[2] : disaster type
	 * args[3] : disaster level
	 * 
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */

//	public static void main(String args[]) throws IOException, ParseException{
//		//		writeout(sortLogs(args[0]),args[1],args[2],args[3]);
//		String logfile = "c:/Users/c-tyabe/desktop/DisasterAlertData_toshiken.csv";
//		String out = "c:/Users/c-tyabe/desktop/";
//		//		String type = "dosha";
//		//		String level = "10";
//		//		writeout(sortLogs(logfile),out,"dosha",level);
//		//		writeout(sortLogs(logfile),out,"flood",level);
//		//		writeout(sortLogs(logfile),out,"heats","3");
//		//		writeout(sortLogs(logfile),out,"volc",level);
//		//		writeout(sortLogs(logfile),out,"eew","3");
//	//	writeout(sortLogs(logfile),out,"rain","1");
//		//		writeout(sortLogs(logfile),out,"earthquake","3");
//	}

	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS3 = new SimpleDateFormat("dd");//change time format

	//day - time - <level-jis>
	public static HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> sortLogs(String in) throws IOException, ParseException{
		HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> res = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = br.readLine();

		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");
			String[] date = tokens[0].split(" ");
			String ymdslash = date[0];
			String ymd = yyyymmdd(ymdslash);
			String time = date[1];
			String hour = converttime(time);
			String level = tokens[2];
			String codes = tokens[3];

			if(res.containsKey(ymd)){
				if(res.get(ymd).containsKey(hour)){
					if(res.get(ymd).get(hour).containsKey(level)){
						res.get(ymd).get(hour).get(level).add(codes);
					}
					else{
						ArrayList<String> list = new ArrayList<String>();
						list.add(codes);
						res.get(ymd).get(hour).put(level, list);
					}
				}
				else{
					ArrayList<String> list = new ArrayList<String>();
					list.add(codes);
					HashMap<String,ArrayList<String>> leveljismap = new HashMap<String,ArrayList<String>>();
					leveljismap.put(level, list);
					res.get(ymd).put(hour, leveljismap);				
				}
			}
			else{
				ArrayList<String> list = new ArrayList<String>();
				list.add(codes);
				HashMap<String,ArrayList<String>> leveljismap = new HashMap<String,ArrayList<String>>();
				leveljismap.put(level, list);
				HashMap<String, HashMap<String, ArrayList<String>>> map = new HashMap<String, HashMap<String, ArrayList<String>>>();
				map.put(hour, leveljismap);
				res.put(ymd, map);
			}
		}
		br.close();
		return res;
	}

	public static void writeout
	(HashMap<String, HashMap<Integer, HashMap<String,String>>> map, String path, String type, String level) throws IOException{
		String out = path + type+ ".csv";
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		for(String ty : map.keySet()){
			if(ty.equals(type)){
				for(Integer le : map.get(ty).keySet()){
					if(le>=Integer.valueOf(level)){
						for(String d : map.get(ty).get(le).keySet()){
							bw.write(ty + "," + le + "," + d + "," + map.get(ty).get(le).get(d));
							bw.newLine();
						}
					}
				}
			}
		}
		bw.close();
	}

//	public static HashMap<String, ArrayList<String>> date_codemap(String disasterlogs) throws IOException{
//		HashMap<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
//		File dislogs = new File(disasterlogs);
//		BufferedReader br = new BufferedReader(new FileReader(dislogs));
//		String line = null;
//		while((line=br.readLine())!=null){
//			String[] tokens = line.split(",");
//			String[] datetime = tokens[2].split(" ");
//			String date = datetime[0];
//			String code = tokens[3];
//			if(res.containsKey(date)){
//				res.get(date).add(code);
//			}
//			else{
//				ArrayList<String> list = new ArrayList<String>();
//				list.add(code);
//				res.put(date, list);
//			}
//		}
//		br.close();
//		return res;
//	}

	public static String converttime(String time){
		String[] xx = time.split(":");
		Double hour = Double.parseDouble(xx[0]);
		Double mins = Double.parseDouble(xx[1]);
		if(mins>30){
			hour = hour + 0.5;
		}
		String h = String.valueOf(hour);
		return h;
	}
	
	public static String slashtodash(String ymdslash){
		String[] yy = ymdslash.split("/");
		String ymd = yy[0]+"-"+yy[1]+"-"+yy[2];
		return ymd;
	}
	
	public static String yyyymmdd(String ymdslash){
		String[] yy = ymdslash.split("/");
		String y = yy[0];
		String m = String.format("%02d", yy[1]);
		String d = String.format("%02d", yy[2]);
		String res = y+m+d;
		return res;
	}

}

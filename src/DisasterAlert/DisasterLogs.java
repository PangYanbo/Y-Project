package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	public static void main(String args[]) throws IOException, ParseException{
//		writeout(sortLogs(args[0]),args[1],args[2],args[3]);
		String logfile = "c:/Users/c-tyabe/desktop/DisasterAlertData.csv";
		String out = "c:/Users/c-tyabe/desktop/";
		String type = "emg2";
		String level = "10";
		writeout(sortLogs(logfile),out,type,level);
	}
	
	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS3 = new SimpleDateFormat("dd");//change time format

	//type - level - <time-jis>
	public static HashMap<String, HashMap<Integer, HashMap<String,String>>> sortLogs(String in) throws IOException, ParseException{
		HashMap<String, HashMap<Integer, HashMap<String,String>>> res = new HashMap<String, HashMap<Integer, HashMap<String,String>>>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = br.readLine();
		
//		Date d = SDF_TS2.parse("2014-10-21 00:00:00");
		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");
			String[] ymd = tokens[0].split("/");
			Integer year = Integer.valueOf(ymd[0]);
			Integer month = Integer.valueOf(ymd[1]);
			String daytime = ymd[2];
			String[] d_t = daytime.split(" ");
			Integer day = Integer.valueOf(d_t[0]);
			String[] hourmin = d_t[1].split(":");
			Integer hour = Integer.valueOf(hourmin[0]);
			Integer min = Integer.valueOf(hourmin[1]);
			
			if((year>2014)||((year==2014)&&(month>11))){
				String type = tokens[1];
				Integer level = Integer.valueOf(tokens[2]);
				String jiscode = tokens[3];
				if(res.containsKey(type)){
					if(res.get(type).containsKey(level)){
						res.get(type).get(level).put(tokens[0], jiscode);
					}
					else{
						HashMap<String,String> timejismap = new HashMap<String,String>();
						timejismap.put(tokens[0], jiscode);
						res.get(type).put(level, timejismap);				
					}
				}
				else{
					HashMap<String,String> timejismap = new HashMap<String,String>();
					timejismap.put(tokens[0], jiscode);
					HashMap<Integer, HashMap<String,String>> ltj = new HashMap<Integer, HashMap<String,String>>();
					ltj.put(level, timejismap);
					res.put(type, ltj);
				}
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


}

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
		String type = "rain";
		String level = "4";
		writeout(sortLogs(logfile),out,type,level);
	}
	
	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS3 = new SimpleDateFormat("dd");//change time format

	//type - level - <time-jis>
	public static HashMap<String, HashMap<Integer, HashMap<Date,String>>> sortLogs(String in) throws IOException, ParseException{
		HashMap<String, HashMap<Integer, HashMap<Date,String>>> res = new HashMap<String, HashMap<Integer, HashMap<Date,String>>>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = null;
		for(int i=1; i<=4; i++){
			line = br.readLine();
		}
		Date d = SDF_TS2.parse("2014-10-21 00:00:00");
		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");
			Date date = SDF_TS2.parse(tokens[0]);
			if(date.after(d)){
				String type = tokens[1];
				Integer level = Integer.valueOf(tokens[2]);
				String jiscode = tokens[3];
				if(res.containsKey(type)){
					if(res.get(type).containsKey(level)){
						res.get(type).get(level).put(date, jiscode);
					}
					else{
						HashMap<Date,String> timejismap = new HashMap<Date,String>();
						timejismap.put(date, jiscode);
						res.get(type).put(level, timejismap);				
					}
				}
				else{
					HashMap<Date,String> timejismap = new HashMap<Date,String>();
					timejismap.put(date, jiscode);
					HashMap<Integer, HashMap<Date,String>> ltj = new HashMap<Integer, HashMap<Date,String>>();
					ltj.put(level, timejismap);
					res.put(type, ltj);
				}
			}
		}
		br.close();
		return res;
	}

	public static void writeout
	(HashMap<String, HashMap<Integer, HashMap<Date,String>>> map, String path, String type, String level) throws IOException{
		String out = path + type;
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		for(String ty : map.keySet()){
			if(ty.equals(type)){
				for(Integer le : map.get(ty).keySet()){
					if(le>=Integer.valueOf(level)){
						for(Date d : map.get(ty).get(le).keySet()){
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

package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class HomeOfficeMaps {

	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_MDS = new SimpleDateFormat("HH:mm:ss");//change time format
	
	/*
	 * Gets map of id | day | logs(array) ... just change the X to Home or Offices!
	 */
	
	public static HashMap<String,HashMap<String,ArrayList<Integer>>> getLogsnearX(File in, File X) throws IOException, NumberFormatException, ParseException{
		HashMap<String,LonLat> id_X = getXMap(X);
		HashMap<String,HashMap<String,ArrayList<Integer>>> res = new HashMap<String,HashMap<String,ArrayList<Integer>>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = (tokens[0]);
			
			if(id_X.containsKey(id)){
				Double lon = Double.parseDouble(tokens[2]);
				Double lat = Double.parseDouble(tokens[1]);
				LonLat point = new LonLat(lon,lat);
				String date = tokens[3];
				
				String[] youso = date.split(" ");
				String ymd = youso[0];
				String[] youso2 = ymd.split("-");
				String hiniti = youso2[2];
				String hms = youso[1];
				String hour = hms.substring(0,2);

				if(point.distance(id_X.get(id))<500){
					Integer time = converttoSecs(SDF_MDS.format(SDF_TS.parse(tokens[3])));
					if(Integer.valueOf(hour)<3){
						time = time + 86400;
					}

					if(res.containsKey(id)){
						if(res.get(id).containsKey(hiniti)){
							res.get(id).get(hiniti).add(time);
						}
						else{
							ArrayList<Integer> list = new ArrayList<Integer>();
							list.add(time);
							res.get(id).put(hiniti, list);
						}
					}
					else{
						HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
						ArrayList<Integer> list = new ArrayList<Integer>();
						list.add(time);
						map.put(hiniti, list);
						res.put(id, map);
					}
				}
			}
		}		
		br.close();
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

	public static HashMap<String,LonLat> getXMap(File in) throws IOException{
		HashMap<String,LonLat> res = new HashMap<String,LonLat>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line=br.readLine()) != null){
			String[] tokens = line.split("\t");
			String id = (tokens[0]);
			LonLat point = new LonLat(Double.parseDouble(tokens[2]),Double.parseDouble(tokens[1]));
			res.put(id, point);
		}
		br.close();
		return res;
	}
	
}

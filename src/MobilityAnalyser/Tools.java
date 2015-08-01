package MobilityAnalyser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class Tools {

	public static LonLat getave(ArrayList<LonLat> list){
		double lonsum = 0;
		double latsum = 0;
		for(LonLat p : list){
			lonsum = lonsum + p.getLon();
			latsum = latsum + p.getLat();
		}
		return new LonLat(lonsum/(double)list.size(),latsum/(double)list.size());
	}

	public static int converttoSecs(String time){
		String[] tokens = time.split(":");
		int hour = Integer.parseInt(tokens[0]);
		int min  = Integer.parseInt(tokens[1]);
		int sec  = Integer.parseInt(tokens[2]);

		int totalsec = hour*3600+min*60+sec;		
		return totalsec;
	}

	public static File writeout(HashMap<String, HashMap<String,ArrayList<LonLat>>> map, String outpath) throws IOException{
		File out = new File(outpath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			for(String day : map.get(id).keySet()){
				for(LonLat p: map.get(id).get(day)){
					bw.write(id + "\t" + day + "\t" + p);
					bw.newLine();
				}
			}
		}
		bw.close();
		return out;
	}

}

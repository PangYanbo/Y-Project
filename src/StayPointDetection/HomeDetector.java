package StayPointDetection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.STPoint;

public class HomeDetector {

	/*
	 * param 
	 * args[0] : infile for all data
	 * args[1] : outfile for 'id \t homepoint'
	 * 	
	 */
	
	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{

		File in = new File(args[0]);

		HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> SPmap = 
				StayPointGetter.getSPs(in, "00:00:00", "10:00:00", 5, 2000, 1000);

		HashMap<String, ArrayList<STPoint>> alldatamap = StayPointGetter.sortintoMap(in);
		HashMap<String, ArrayList<STPoint>> targetmap = StayPointGetter.getTargetMap(alldatamap,"00:00:00","10:00:00");
		HashMap<String,Integer> numberofLogs = new HashMap<String,Integer>();
		for(String id : targetmap.keySet()){
			int days = StayPointTools.NumberofDays(targetmap.get(id));
			numberofLogs.put(id, days);
		}

		HashMap<String,HashMap<LonLat,Integer>> id_SP_visitcount = 
				StayPointTools.ExcludeLowFrequentSPsbyNumberofPoints(SPmap,numberofLogs,0.5);
		HashMap<String,LonLat> resmap = StayPointTools.getHomePointsbyNumberofPoints(id_SP_visitcount);

		File res = new File (args[1]);
		writeOut(resmap, res);
	}



	public static HashMap<Integer,HashMap<LonLat,Integer>> FrequentStayPointsintoMap(File in) throws IOException{
		HashMap<Integer,HashMap<LonLat,Integer>> res = new HashMap<Integer,HashMap<LonLat,Integer>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line=br.readLine()) != null){
			String[] tokens = line.split(",");
			int id = Integer.parseInt(tokens[0]);
			LonLat point = new LonLat(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
			int count = Integer.parseInt(tokens[3]);
			if(res.containsKey(id)){
				res.get(id).put(point, count);
			}
			else{
				HashMap<LonLat,Integer> map = new HashMap<LonLat,Integer>();
				map.put(point, count);
				res.put(id, map);
			}
		}
		br.close();
		return res;
	}

	public static LonLat sortbyOrder(HashMap<LonLat,Integer> map, int rank){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(LonLat p:map.keySet()){
			list.add(map.get(p));
		}
		Collections.sort(list);
		Collections.reverse(list);
		if(list.size()>=rank){
			int count = list.get(rank-1);
			LonLat point = null;
			for(LonLat p :map.keySet()){
				if(map.get(p)==count){
					point = p;
				}
			}
			return point;
		}
		else{
			return null;
		}
	}

	public static File writeOut(HashMap<String,LonLat> map, File out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		int count = 0;
		for(String id:map.keySet()){
			bw.write(id + "\t" + map.get(id).getLon() + "\t" + map.get(id).getLat());
			bw.newLine();
			count++;
		}
		bw.close();
		System.out.println("IDs with Home: " + count);
		return out;
	}

}

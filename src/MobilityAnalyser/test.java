package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class test {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/test3.txt");
		BufferedReader br = new BufferedReader(new FileReader(in));
		HashMap<Integer, LonLat> map = new HashMap<Integer, LonLat>();
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			Integer time = Integer.valueOf(tokens[0]);
			LonLat p = new LonLat(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
			map.put(time, p);
		}
		br.close();
		ArrayList<LonLat> res = staypoints(map);
		System.out.println("#res " + res);
	}

	public static ArrayList<LonLat> staypoints(HashMap<Integer, LonLat> map){
		ArrayList<LonLat> res = new ArrayList<LonLat>();
		ArrayList<Integer> sortedtime = SPFinder.sortbyTime(map);
		LonLat startpoint = new LonLat(130.0,45.0);
		Integer starttime = 0;
		Integer endtime = 0;
		ArrayList<LonLat> temp = new ArrayList<LonLat>();
		for(Integer t : sortedtime){
			System.out.println("#t:" + t);
			if(map.get(t).distance(startpoint)<2000){
				System.out.println("under 2000!");
				endtime = t;
				temp.add(map.get(t));
				System.out.println("#temp" + temp);
				LonLat sp = Tools.getave(temp);
				startpoint = sp;
			}
			else if ((map.get(t).distance(startpoint)>=2000)&&(temp.size()>1)){
				System.out.println("over 2000! & tempsize>1");
				if(endtime-starttime>600){
					//					System.out.println("#temp: " + temp);
					LonLat sp = Tools.getave(temp);
					res.add(sp);
					temp.clear();
				}
				else{
					temp.clear();
				}
			}
			else{
				System.out.println("###DUMP EVERYTHING");
				temp.clear();
				startpoint = map.get(t);
				starttime = t;
				temp.add(startpoint);
			}
		}
		LonLat sp = Tools.getave(temp);
		res.add(sp);

		//		System.out.println("staypoints : "+res);
		return res;
	}

}

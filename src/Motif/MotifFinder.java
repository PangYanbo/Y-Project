package Motif;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class MotifFinder {

	public static void main(String args[]) throws IOException, ParseException{
		
		/*
		 * test file
		 */
		String in = "c:/users/yabetaka/Desktop/dataforExp.csv";
		
//		String in = args[0];

		HashMap<String, HashMap<String, HashMap<Integer, LonLat>>> map = SPFinder.intomapZDC(in,"weekday");
		HashMap<String,HashMap<String, ArrayList<LonLat>>> res = SPFinder.getAllIDsSP(map);
		HashMap<String, HashMap<String, Integer>> id_day_motif = getID_day_motif(res); //[id|day|motifnumber]
		
		motifPercentage(id_day_motif);
	}

	public static HashMap<String, HashMap<String,Integer>> getID_day_motif(HashMap<String,HashMap<String,ArrayList<LonLat>>> map){
		HashMap<String, HashMap<String,Integer>> res = new HashMap<String, HashMap<String,Integer>>();
		int count = 0;
		int stay = 0;
		for(String id : map.keySet()){
			HashMap<String,Integer> temp = new HashMap<String,Integer>();
			for(String day : map.get(id).keySet()){
				ArrayList<Integer> temp_locchain = getLocChain(map.get(id).get(day));
				ArrayList<Integer> locchain = continueChecker(temp_locchain);
				count++;
				if(count%10000==0){
					System.out.println("#done " + count + " ID*days");
				}
				
				Integer motif = MotifNumber.motifs(locchain);
				temp.put(day, motif);
				res.put(id, temp);
			}
		}
		System.out.println("#Stay rate = "+(double)stay/(double)count);
		return res;
	}

	public static ArrayList<Integer> getLocChain(ArrayList<LonLat> list){
		HashMap<Integer,LonLat> temp = new HashMap<Integer,LonLat>();
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add(1);
		temp.put(1,list.get(0));
		int count = 2;
		for(int i = 1; i<list.size(); i++){
			if(overlapchecker(temp,list.get(i))==0){ //new point
				res.add(count);
				temp.put(count, list.get(i));
				count++;
			}
			else{
				res.add(overlapchecker(temp,list.get(i)));
			}
		}
		if(res.get(res.size()-1)!=1){
			res.add(1);
		}
		return res;
	}

	public static Integer overlapchecker(HashMap<Integer,LonLat> map, LonLat point){
		for(Integer i : map.keySet()){
			if(map.get(i).distance(point)<300){
				return i;
			}
		}
		return 0;
	}
	
	public static ArrayList<Integer> continueChecker(ArrayList<Integer> locchain){
		ArrayList<Integer> res = new ArrayList<Integer>();
		int prev = 99;
		for(Integer i : locchain){
			if(i!=prev){
				res.add(i);
			}
			prev = i;
		}
		return res;
	}
	
	public static void motifPercentage(HashMap<String, HashMap<String,Integer>> map){
		HashMap<Integer,Double> res = new HashMap<Integer,Double>();
		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
		int count = 0;
		for(String id : map.keySet()){
			for(String day : map.get(id).keySet()){
				Integer motif = map.get(id).get(day);
				if(temp.containsKey(motif)){
					int counter = temp.get(motif);
					counter = counter + 1;
					temp.put(motif, counter);
				}
				else{
					temp.put(motif, 1);
				}
				count++;
			}
		}
		for(Integer m : temp.keySet()){
			Double wariai = (double)temp.get(m)/(double)count;
			res.put(m, wariai);
			System.out.println(m +","+wariai*100);
		}
	}

}

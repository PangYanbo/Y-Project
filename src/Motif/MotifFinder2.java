package Motif;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import DataModify.Over8TimeSlots;
import StayPointDetection.StayPointGetter;

public class MotifFinder2 {

	/**
	 * @param args[0] : infile 
	 *
	 */
	public static void main(String args[]) throws IOException, ParseException{

		/*
		 * test file
		 */
//				String in = "c:/users/yabetaka/Desktop/dataforExp.csv";

		String in = args[0];
		executeMotif(in, "xxx","rain");

	}

	public static void executeMotif(String in, String ymd, String type) throws IOException, ParseException{
		HashMap<String,ArrayList<LonLat>> id_SPs = StayPointGetter.getSPs2(new File(in), 500, 300);

		HashMap<String, HashMap<String, ArrayList<LonLat>>> map = SPFinder.intomapY(in,"weekday"); 
		HashMap<String, ArrayList<String>> id_days = Over8TimeSlots.OKAY_id_days(in);
		HashMap<String, HashMap<String, Integer>> id_day_motif = getID_day_motif2(map, id_SPs, id_days); //[id|day|motifnumber]

		motifPercentage(id_day_motif, "/home/c-tyabe/Data/"+type+ymd+"/motifs.csv");
	}
	
	public static HashMap<String, HashMap<String,Integer>> getID_day_motif2
	(HashMap<String, HashMap<String, ArrayList<LonLat>>> map, HashMap<String,ArrayList<LonLat>> id_SPs, HashMap<String, ArrayList<String>> id_days){
		HashMap<String, HashMap<String,Integer>> res = new HashMap<String, HashMap<String,Integer>>();
		int count = 0;
		for(String id : map.keySet()){
			HashMap<String,Integer> temp = new HashMap<String,Integer>();
			for(String day : map.get(id).keySet()){
				if(id_SPs.get(id)!=null){
					if(id_days.containsKey(id)&&(id_days.get(id).contains(day))){
						ArrayList<Integer> temp_locchain = getLocChain2(map.get(id).get(day), id_SPs.get(id));
						ArrayList<Integer> locchain = continueChecker(temp_locchain);
						count++;
						if(count%10000==0){
							System.out.println("#done " + count + " ID*days");
						}
						//						System.out.println(id + ","+day+","+locchain);
						Integer motif = MotifNumber.motifs(locchain);
						temp.put(day, motif);
						res.put(id, temp);
					}
				}
			}
		}
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

	public static ArrayList<Integer> getLocChain2(ArrayList<LonLat> list, ArrayList<LonLat> id_SPs){
		HashMap<Integer,LonLat> temp = new HashMap<Integer,LonLat>();
		ArrayList<Integer> res = new ArrayList<Integer>();
		//		System.out.println("id_SP: " + id_SPs);
		int count = 1;
		for(int i = 0; i<list.size(); i++){
			for(LonLat sp : id_SPs){
				if(list.get(i).distance(sp)<500){
					if(overlapchecker(temp,sp)==0){ //new point
						res.add(count);
						temp.put(count,sp);
						count++;
					}
					else{
						res.add(overlapchecker(temp,sp));
					}
					break;
				}
			}
		}
		res.add(1);
		return res;
	}

	public static Integer overlapchecker(HashMap<Integer,LonLat> map, LonLat point){
		if(map.size()>0){
			for(Integer i : map.keySet()){
				if(map.get(i).distance(point)<1000){
					return i;
				}
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

	public static void motifPercentage(HashMap<String, HashMap<String,Integer>> map, String out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
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
			bw.write(m +","+wariai*100);
			bw.newLine();
			System.out.println("#done calculating motifs");
		}
		bw.close();
	}

}

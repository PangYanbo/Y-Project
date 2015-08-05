package Motif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class PTMotifFinder {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/PTKantoStay.csv");
		HashMap<String,ArrayList<LonLat>> id_lonlat = PTintoMap(in);
		System.out.println("#done putting into map");
		
		HashMap<String,Integer> id_mf = new HashMap<String,Integer>();
		int count=0;
		for(String id: id_lonlat.keySet()){
			ArrayList<LonLat> list = id_lonlat.get(id);
			ArrayList<Integer> temp_locchain = getLocChain(list);
			ArrayList<Integer> locchain = continueChecker(temp_locchain);
			Integer motif = MotifNumber.motifs(locchain);
			id_mf.put(id, motif);
			count++;
			if(count%100000==0){
				System.out.println("#"+count+" done.");
			}
//			if(list.size()==3){
//				System.out.println("#sample list: " + list);
//				System.out.println("#sample locchain: " + locchain);
//				System.out.println("#sample motif: " + motif);
//
//			}
		}

		motifPercentage(id_mf);

	}

	public static HashMap<String,ArrayList<LonLat>> PTintoMap(File in) throws IOException{
		HashMap<String, ArrayList<LonLat>> idll = new HashMap<String,ArrayList<LonLat>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		int count = 0;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(";");
			String id = tokens[0];
			LonLat p = new LonLat(Double.parseDouble(tokens[8]),Double.parseDouble(tokens[9]));
			if(count==0){
				System.out.println("#sample line: " + id + "," + p);
			}
			if(idll.containsKey(id)){
				idll.get(id).add(p);
			}
			else{
				ArrayList<LonLat> list = new ArrayList<LonLat>();
				list.add(p);
				idll.put(id, list);
			}
			count++;
		}
		br.close();
		return idll;
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
			if(map.get(i).distance(point)<10){
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

	public static void motifPercentage( HashMap<String,Integer> map){
		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
		int count = 0;
		for(String id : map.keySet()){
			Integer motif = map.get(id);
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

		for(Integer m : temp.keySet()){
			Double wariai = (double)temp.get(m)/(double)count;
			System.out.println(m +","+wariai*100);
		}
	}

}

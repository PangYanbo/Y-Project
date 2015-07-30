package DataAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class TimeAnalyser {

	public static void main(String args[]) throws IOException{
//		CountbyDay(args[0]);
//		GetTimes(args[0],args[1]);
		ID_Daycount(args[0],args[1]);
	}
	
	public static void CountbyDay(String in) throws IOException{
		HashMap<String,Integer> res = new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String date = tokens[3];
			String day = date.substring(8,10);
			if(res.containsKey(day)){
				int count = res.get(day)+1;
				res.put(day, count);
			}
			else{
				res.put(day, 1);
			}
		}
		br.close();
		for(String d : res.keySet()){
			System.out.println(d + "," + res.get(d));
		}
	}
	
	public static void GetTimes(String in,String out) throws IOException{
		HashMap<Integer,Integer> res = new HashMap<Integer,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String date = tokens[3];
			Integer hour = Integer.parseInt(date.substring(11,13));
			Integer mindig1  = Integer.parseInt(date.substring(14,15));
//			Integer sec  = Integer.parseInt(date.substring(17,19));
			Integer time = hour*60+mindig1*10;
			if(res.containsKey(time)){
				int count = res.get(time)+1;
				res.put(time, count);
			}
			else{
				res.put(time, 1);
			}
		}
		br.close();
		
		for(Integer i : res.keySet()){
			bw.write(String.valueOf(i)+","+String.valueOf(res.get(i)));
			bw.newLine();
		}
		bw.close();
	}
	
	public static void ID_Daycount(String in, String out) throws IOException{
		HashMap<String,HashSet<String>> res = new HashMap<String,HashSet<String>>();
		HashMap<String,Integer> resres = new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			String date = tokens[3];
			String ymd = date.substring(8,10);
			if(res.containsKey(id)){
				res.get(id).add(ymd);
			}
			else{
				HashSet<String> set = new HashSet<String>();
				set.add(ymd);
				res.put(id, set);
			}
		}
		br.close();
		
		for (String id : res.keySet()){
			int days = res.get(id).size();
			String d = String.valueOf(days);
			if(resres.containsKey(d)){
				int count = resres.get(d)+1;
				resres.put(d, count);
			}
			else{
				resres.put(d, 1);
			}
		}
		
		for(String i : resres.keySet()){
			bw.write(i+","+String.valueOf(resres.get(i)));
			bw.newLine();
		}
		bw.close();
	}
	
}
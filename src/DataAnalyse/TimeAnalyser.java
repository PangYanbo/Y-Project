package DataAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeAnalyser {

	public static void main(String args[]) throws IOException{
		CountbyDay(args[0]);
		GetTimes(args[0],args[1]);
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
		ArrayList<Integer> res = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String date = tokens[3];
			Integer hour = Integer.parseInt(date.substring(11,13));
			Integer min  = Integer.parseInt(date.substring(14,16));
			Integer sec  = Integer.parseInt(date.substring(17,19));
			Integer time = hour*3600+min*60+sec;
			res.add(time);
		}
		br.close();
		
		for(Integer i : res){
			bw.write(i);
			bw.newLine();
		}
		bw.close();
	}
}

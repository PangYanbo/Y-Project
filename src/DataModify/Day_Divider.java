package DataModify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Day_Divider {

	/*
	 * Divides alldatafile into files for each day.
	 * param 
	 * args[0] : all data file
	 * args[1] : filepath of outfile
	 * 
	 */

	public static void main(String args[]) throws IOException{
		DividebyDay(args[0], args[1]);
	}

	public static void DividebyDay(String in, String outpath) throws IOException{
		HashMap<Integer,ArrayList<String>> res = new HashMap<Integer,ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String date = tokens[3];
			int day = returnDay(date);
			if(res.containsKey(day)){
				res.get(day).add(line);
			}
			else{
				ArrayList<String> list = new ArrayList<String>();
				list.add(line);
				res.put(day,list);
			}
		}
		br.close();

		for(Integer i : res.keySet()){
			String path = outpath + "_" +String.valueOf(i);
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
			if(res.get(i).size()>300){
				for(String l : res.get(i)){
					bw.write(l);
					bw.newLine();
				}
			}
			bw.close();
		}
	}

	public static int returnDay(String date){
		Integer res = null;
		Integer d = Integer.parseInt(date.substring(8,10));
		Integer hour = Integer.parseInt(date.substring(11,13));
		if(hour<=3){
			res = d-1;
		}
		else{
			res = d;
		}
		return res;
	}

}

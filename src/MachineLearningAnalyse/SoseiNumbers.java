package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SoseiNumbers {
	
	public static void main(String args[]) throws IOException{


		ArrayList<String> disasters = new ArrayList<String>();
		disasters.add("rain");
		disasters.add("eq");
//		disasters.add("heats");
//		disasters.add("dosha");

		ArrayList<String> subjects = new ArrayList<String>();
//		subjects.add("home_exit");
		subjects.add("tsukin_time");
//		subjects.add("office_enter");
//		subjects.add("office_time");
//		subjects.add("office_exit");
		subjects.add("kitaku_time");
//		subjects.add("home_return");

		for(String disaster : disasters){
			for(String subject : subjects){
				String in  = "/home/c-tyabe/Data/MLResults_"+disaster+"12/forML/calc/"+subject+"_diff_ML2_plusminus_lineforeach_nozero.csv";
				String out =  "/home/c-tyabe/Data/MLResults_"+disaster+"12/forML/calc/"+subject+"_sosei-count.csv";
				checkfile(in,out);
			}
		}
	}

	public static void checkfile(String in, String out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		String line = null;
		HashMap<String, Integer> list = new HashMap<String,Integer>();
		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			for(String t : tokens){
				if(t.contains(":")){
					String soseinum = t.split(":")[0];
					if(list.containsKey(soseinum)){
						int count = list.get(soseinum)+1;
						list.put(soseinum, count);
					}
					else{
						list.put(soseinum, 1);
					}
				}
			}
		}
		br.close();
		for(String num : list.keySet()){
			String c = String.valueOf(list.get(num));
			bw.write(num+","+c);
			bw.newLine();
		}
		bw.close();
	}
	
}

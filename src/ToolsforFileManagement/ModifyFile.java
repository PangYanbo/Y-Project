package ToolsforFileManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ModifyFile {
	
	public static void main(String args[]) throws IOException{
		
		File infile  = new File("C:/users/t-tyabe/desktop/rgr_result.csv");
		File outfile = new File("C:/users/t-tyabe/desktop/rgr_result_analysis.csv");
		
		HashMap<String,Integer> temp = new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(infile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String abs_diff = tokens[3];
			Integer normallogs = Integer.valueOf(tokens[4]);
//			Integer disasterlogs = Integer.valueOf(tokens[5]);
			temp.put(abs_diff, normallogs);
		}
		br.close();
		
//		HashMap<String,String> res = new HashMap<String,String>();
		
		for(int i=0; i<=100; i++){
			Double tempsum = 0d;
			int count = 0;
			for(String diff : temp.keySet()){
				if((temp.get(diff)>=i)&&(temp.get(diff)<=(i+1))){
					count++;
					tempsum = tempsum + Double.parseDouble(diff);
				}
			}
			Double avgerror = tempsum/(double)count;
//			res.put(String.valueOf(i), String.valueOf(avgerror));
			bw.write(String.valueOf(i)+" to "+String.valueOf(i+10)+","+String.valueOf(avgerror)
					+","+String.valueOf(count)+","+String.valueOf(tempsum));
			bw.newLine();
		}
		
		
		bw.close();
	}

}

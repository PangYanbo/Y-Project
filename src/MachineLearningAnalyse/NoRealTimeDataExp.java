package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NoRealTimeDataExp {

	public static void main(String args[]) throws IOException{
		ArrayList<String> types = new ArrayList<String>();
		types.add("rain");
		types.add("eq");
		types.add("heats");
//		types.add("dosha");

		for(String type : types){
		
			ArrayList<String> subjects = new ArrayList<String>();
			subjects.add("home_exit_diff");
			subjects.add("tsukin_time_diff");
			subjects.add("office_enter_diff");
			subjects.add("office_time_diff");
			subjects.add("office_exit_diff");
			subjects.add("kitaku_time_diff");
			subjects.add("home_return_diff");

			for(String subject: subjects){
				
				String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"13/";
				String outdir4   = outdir+"forML/calc/sameexp/";
				String in = outdir4+subject+"_ML2_plusminus_lineforeach_same.csv";
				String out = outdir4+subject+"_ML2_plusminus_lineforeach_same_nonrealtime_noarea.csv";
				
				deleterealtimedata(in,out);
				
			}
			
		}
		
		
	}
	
	
	public static void deleterealtimedata(String infile, String outfile) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			bw.write(tokens[0]);
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=1; i<tokens.length-1; i++){
				if(!tokens[i].split(":")[0].isEmpty()){
					Integer num = Integer.valueOf(tokens[i].split(":")[0]);
					if((num<=144)||(num>=100000)){
						list.add(num);
					}
				}
			}
			Collections.sort(list);
			for(Integer n : list){
				bw.write(" "+String.valueOf(n)+":1");
			}
			bw.write(" "+tokens[tokens.length-1]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

}

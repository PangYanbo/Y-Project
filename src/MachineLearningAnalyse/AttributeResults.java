package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AttributeResults {

	public static void main(String args[]) throws IOException{
		
		File in  = new File("/home/c-tyabe/Data/MLResults_rain6/"+args[0]+"_diff_ML_lineforeach.csv");
		File out = new File("/home/c-tyabe/Data/MLResults_rain6/"+args[0]+"_diff_ML_lineforeach_analyse.csv");
		Integer start = Integer.valueOf(args[1]);
		Integer end   = Integer.valueOf(args[2]);
		
		makeMap(in,out,start,end);
		
	}
	
	public static ArrayList<ArrayList<String>> makeMap(File in, File out, Integer start, Integer end) throws IOException{
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!= null){
			String[] tokens = line.split(" ");
			String val = tokens[0];
			for(int i=start; i<=end; i++){
				if(line.contains(String.valueOf(i)+":1")){
					bw.write(val + "," + i);
					bw.newLine();
				}
			}
		}
		br.close();
		bw.close();
		return res;
	}
	
}

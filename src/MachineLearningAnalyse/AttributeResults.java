package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AttributeResults {

	public static void main(String args[]) throws IOException{

		File in = new File("/home/c-tyabe/Data/MLResults_rain6/"+args[0]+"_diff_ML_lineforeach.csv");
		File out = new File("/home/c-tyabe/Data/MLResults_rain6/"+args[0]+"_diff_ML_lineforeach_elements.csv");
		
		makeMap(in,out);

	}

	public static HashMap<String,ArrayList<String>> makeMap(File in, File out) throws IOException{
		HashMap<String,ArrayList<String>> res = new HashMap<String,ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!= null){
			String[] tokens = line.split(" ");
			String val = tokens[0];
			for(String ele : tokens){
				if(ele.split(":").length==2){
					String elenum = ele.split(":")[0];
					if(res.containsKey(elenum)){
						res.get(elenum).add(val);
					}
					else{
						ArrayList<String> list = new ArrayList<String>();
						list.add(val);
						res.put(elenum, list);
					}
				}
			}
		}
		br.close();
		
		for(String num : res.keySet()){
			for(String val : res.get(num)){
				bw.write(num + "," + val);
				bw.newLine();
			}
		}
		
		bw.close();
		return res;
	}

}

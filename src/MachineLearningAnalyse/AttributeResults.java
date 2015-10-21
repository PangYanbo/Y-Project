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

		int start = 0;
		int end = 4;

		for(int i = 0; i<=34; i++){
			ArrayList<String> subjects = new ArrayList<String>();
			subjects.add("home_exit_diff");
			subjects.add("tsukin_time_diff");
			subjects.add("office_enter_diff");
			subjects.add("office_time_diff");
			subjects.add("office_exit_diff");
			subjects.add("kitaku_time_diff");
			subjects.add("home_return_diff");

			for(String subject: subjects){
				File in = new File("/home/c-tyabe/Data/MLResults_rain9/"+subject+"_ML_lineforeach.csv");
				File out = new File("/home/c-tyabe/Data/MLResults_rain9/sosei/"+subject+"_ML_lineforeach_elements_"+args[0]+"_"+args[1]+".csv");
				makeMap(in,out,String.valueOf(start+5*i),String.valueOf(end+5*i));

			}
		}

	}

	public static HashMap<String,ArrayList<String>> makeMap(File in, File out, String start, String end) throws IOException{
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
					if(!elenum.equals("")){
						if((Integer.valueOf(elenum)<=Integer.valueOf(end))&&(Integer.valueOf(elenum)>=Integer.valueOf(start))){
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

	//	public static void soukancheck(HashMap<String,ArrayList<String>> map){
	//		int elenum = 4;
	//		
	//	}

}

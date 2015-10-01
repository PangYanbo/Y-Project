package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MLDataforMotif {


	public static HashMap<String, HashMap<String,String>> MotifMap(File in, String date, HashMap<String, HashMap<String,String>> res) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String day = tokens[2];
			if(!day.equals("99")){
				date = date.substring(0,6)+day;
			}
			String motif = tokens[3];

			if(res.containsKey(id)){
				res.get(id).put(date, motif);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(date, motif);
				res.put(id, temp);
			}
		}
		br.close();
		return res;
	}

	
	
	
}

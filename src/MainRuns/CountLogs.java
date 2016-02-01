package MainRuns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CountLogs {

	public static HashMap<String,Integer> CountNorLogs(File in, String disdate) throws IOException{
		HashMap<String,Integer> res = new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			String date = tokens[3]; //yyyymmdd 
			if(!date.equals(disdate)){
				if(res.containsKey(id)){
					int count = res.get(id) + 1;
					res.put(id, count);
				}
				else{
					res.put(id, 1);
				}
			}
		}
		br.close();
		return res;
	}
	
	public static HashMap<String,Integer> CountDisLogs(File in, String disdate) throws IOException{
		HashMap<String,Integer> res = new HashMap<String,Integer>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			String date = tokens[3]; //yyyymmdd 
			if(date.equals(disdate)){
				if(res.containsKey(id)){
					int count = res.get(id) + 1;
					res.put(id, count);
				}
				else{
					res.put(id, 1);
				}
			}
		}
		br.close();
		return res;
	}
	
}

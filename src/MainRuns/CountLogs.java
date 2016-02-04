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
			String date = tokens[3]; //yyyy-mm-dd HH:MM:SS
			String yyyymmdd = getonlydate(date);
			if(!yyyymmdd.equals(disdate)){
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
			String date = tokens[3]; //yyyy-mm-dd HH:MM:SS
			String yyyymmdd = getonlydate(date);
			if(yyyymmdd.equals(disdate)){
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
	
	public static String getonlydate(String t){
		String[] x = t.split(" ");
		String res = x[0];
		String date = res.split("-")[0]+res.split("-")[1]+res.split("-")[2];
		return date;
	}
	
	public static void main(String args[]){
		String line = "2011-04-12 12:11:10";
		String res = getonlydate(line);
		System.out.println(res);
	}
	
}

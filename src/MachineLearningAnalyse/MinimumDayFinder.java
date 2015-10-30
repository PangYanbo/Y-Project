package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MinimumDayFinder {

	public static void main(String args[]) throws IOException{
		
		File in  = new File("/home/c-tyabe/Data/expALL/office_enter.csv");
		File out = new File("/home/c-tyabe/Data/expALL/office_enter_shusokudays.csv");
		
		HashMap<String, HashMap<String,String>> map = sort(in);
		
		writeout(map,out);
		
	}

	public static File writeout(HashMap<String, HashMap<String,String>> map, File out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			int shusokudays = shusoku(map.get(id));
			bw.write(String.valueOf(shusokudays));
			bw.newLine();
		}
		bw.close();
		return out;
	}
	
	public static HashMap<String, HashMap<String,String>> sort(File in) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
		while((line=br.readLine())!=null){
			String id = line.split(",")[0];
			String day = line.split(",")[1];
			String time = line.split(",")[2];
			if(map.containsKey(id)){
				map.get(id).put(day, time);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(day, time);
				map.put(id, temp);
			}
		}
		br.close();
		return map;
	}

	public static int shusoku(HashMap<String,String> map){
		if(map.size()>0){
			Double avg = 0d;
			Double tmp = 0d;
			Double tmpavg = 0d;
			int count = 0;
			int score = 0;
			for(String day : map.keySet()){
				count++;
				tmp = tmp + Double.parseDouble(map.get(day));
				tmpavg = tmp/(double)count;
				if((count>=0)&&(Math.abs(tmpavg-avg))<1){
					score++;
					if(score==5){
						return count;
					}
					else{
						continue;
					}
				}
				else{
					score=0;
				}
				avg = tmpavg;
			}
			return 140;
		}
		else{
			return 0;
		}
	}
}

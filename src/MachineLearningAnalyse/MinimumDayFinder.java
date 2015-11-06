package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MinimumDayFinder {

	public static void main(String args[]) throws IOException{

		File in  = new File("/home/c-tyabe/Data/expALL2/office_enter.csv");
		File out = new File("/home/c-tyabe/Data/expALL2/office_enter_shusokudays2.csv");

		//		File in = new File("c:/users/yabetaka/desktop/test.txt");
		//		File out = new File("c:/users/yabetaka/desktop/test2.txt");

		HashMap<String, HashMap<String,String>> map = sort(in);

		writeout(map,out);

	}

	public static File writeout(HashMap<String, HashMap<String,String>> map, File out) throws IOException{
		int count = 1;
		for(String id : map.keySet()){
			writebunsanout(map.get(id),out, String.valueOf(count));
			count++;
		}
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

	public static int shusoku(HashMap<String,String> map) throws IOException{
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
				if((count>=10)&&(Math.abs(tmpavg-avg))<1){
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

	public static void writebunsanout(HashMap<String,String> map, File out, String id) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		if(map.size()>0){
			ArrayList<String> times = new ArrayList<String>();
			for(String m:map.keySet()){
				times.add(map.get(m));
			}

			ArrayList<String> list = new ArrayList<String>();
			for(int i = 2; i<=map.size(); i++){
				for(int j=1; j<=i; j++){
					list.add(times.get(j-1));
				}
				double stddev = getbunsan(list);
				list.clear();
				bw.write(id+","+i+","+stddev);
				bw.newLine();
			}
		}
		bw.close();
	}

	public static double getbunsan(ArrayList<String> list){
		Double avg = 0d;
		Double tempsum = 0d;
		for(String n : list){
			Double nd = Double.parseDouble(n);
			tempsum = tempsum + nd;
		}
		avg = tempsum/(double)list.size();

		Double bunsan = 0d;
		Double tmpsum2 = 0d;
		for(String n2 : list){
			Double nd2 = Double.parseDouble(n2);
			tmpsum2 = tmpsum2 + Math.pow(nd2-avg, 2);
		}
		bunsan = tmpsum2/(double)list.size();
		return Math.pow(bunsan, 0.5);
	}

	public static void modifyfile(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			if(map.keySet().contains(id)){
				map.get(id).add(tokens[2]);
			}
			else{
				ArrayList<String> list = new ArrayList<String>();
				list.add(tokens[2]);
				map.put(id, list);
			}
		}	
		br.close();

		for(int j = 0; j<=120; j++){
			for(int i = 0; i<map.keySet().size(); i++){
				bw.write(map.get(i).get(j)+",");
			}
		}
		bw.close();
	}

}

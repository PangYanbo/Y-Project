package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class TypeLevelDateCode {

	public static void main(String args[]) throws IOException{
		String in = "c:/users/yabetaka/Desktop/bbb.csv";
		File out = new File("c:/users/yabetaka/Desktop/out.csv");
		
		File pop = new File("c:/users/yabetaka/Desktop/pop.csv");

		HashMap<String, Integer> map = getmap(pop);
		System.out.println("got pop map");
		
		makelist("rain","1",in,out,map);
		makelist("rain","2",in,out,map);
		makelist("rain","3",in,out,map);
		makelist("rain","4",in,out,map);
		System.out.println("done rain");
		makelist("dosha","10",in,out,map);
		System.out.println("done dosha");
		makelist("flood","10",in,out,map);
		System.out.println("done flood");
//		makelist("emg1","1",in,out,map);
//		makelist("emg1","2",in,out,map);
//		makelist("emg1","3",in,out,map);
//		makelist("volc","10",in,out,map);


	}

	public static void makelist(String ty, String lvl, String in, File out, HashMap<String, Integer> map) throws IOException{

//		int count = 0;
		HashMap<String, HashSet<String>> res = new HashMap<String, HashSet<String>>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String date = tokens[0].split(" ")[0];
			String type = tokens[1];
			String level = tokens[2];
			String[] codes = tokens[3].split(" ");

			if((type.equals(ty))&&(lvl.equals(level))){

				if(res.containsKey(date)){
					for(String code: codes){
						res.get(date).add(code);
					}
				}
				else{
					HashSet<String> temp = new HashSet<String>();
					for(String code: codes){
						temp.add(code);
					}
					res.put(date, temp);
				}

			}
		}
		
		for(String d : res.keySet()){
			for(String c : res.get(d)){
				bw.write(ty + "," + lvl + "," + d + "," + c + "," + map.get(c));
				bw.newLine();
			}
		}
		
		br.close();
		bw.close();
	}
	
	public static HashMap<String, Integer> getmap(File in) throws IOException{
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			res.put(tokens[0], Integer.valueOf(tokens[1]));
		}
		br.close();
		return res;
	}
	
}

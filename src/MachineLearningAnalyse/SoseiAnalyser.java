package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SoseiAnalyser {

	public static void main(String args[]) throws IOException{

		/**@author yabetaka
		 */

		String type = args[0];
		String min = args[1];
		String max = args[2];

		File soseifile = new File("/home/c-tyabe/liblinear/sosei.txt");
		HashMap<String,String> map = getsoseimap(soseifile);

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit");
		subjects.add("tsukin_time");
		subjects.add("office_enter");
		subjects.add("office_time");
		subjects.add("office_exit");
		subjects.add("kitaku_time");
		subjects.add("home_return");

		for(String s:subjects){
			justshow(type,s,map,Integer.valueOf(min),Integer.valueOf(max));
		}
	}

	public static void checkfile(String type, String subject, HashMap<String,String> map) throws IOException{
		File in = new File("/home/c-tyabe/liblinear/"+type+"model/"+subject+"_diff_ML2_plusminus_lineforeach_nozero_word_c_1v_-1");
		File out = new File("/home/c-tyabe/liblinear/"+type+"model/"+subject+"_diff_ML2_plusminus_lineforeach_nozero_word_c_1v_-1_out");
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		HashMap<String, Double> list = new HashMap<String,Double>();
		System.out.println(" ");
		System.out.println(type+","+subject);
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String num  = tokens[0];
			//			String desc = tokens[1];
			Double val   = Double.parseDouble(tokens[2]);
			list.put(num, val);
		}
		br.close();

		sortandshow(list,map, out);
	}

	public static void justshow(String type, String subject, HashMap<String,String> map, Integer min, Integer max) throws IOException{
		File in = new File("/home/c-tyabe/liblinear/"+type+"model/"+subject+"_diff_ML2_plusminus_lineforeach_nozero_word_c_1v_-1");
//		File out = new File("/home/c-tyabe/liblinear/"+type+"model/"+subject+"_diff_ML2_plusminus_lineforeach_nozero_word_c_1v_-1_out");
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		System.out.println(" ");
		System.out.println(type+","+subject);
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String num  = tokens[0];
			Integer n = Integer.valueOf(num);
			if((n>=min)&&(n<=max)){
				System.out.println(num + "," + tokens[2]);
			}
			br.close();
		}
	}


	public static void sortandshow(HashMap<String,Double> map, HashMap<String,String> soseimap, File out) throws IOException{

		BufferedWriter bw = new BufferedWriter(new FileWriter(out));

		List<Map.Entry<String,Double>> entries = 
				new ArrayList<Map.Entry<String,Double>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String,Double>>() {

			@Override
			public int compare(Entry<String,Double> entry1, Entry<String,Double> entry2) {
				return ((Double)entry2.getValue()).compareTo((Double)entry1.getValue());
			}
		});

		ArrayList<String> temp = new ArrayList<String>();
		//		int count = 0;
		for (Entry<String,Double> s : entries) {
			String r = numchanger(s.getKey());
			if(!temp.contains(r)){
				if((Integer.valueOf(s.getKey())<1000)||(Integer.valueOf(s.getKey())>=100000)){
					bw.write(soseimap.get(s.getKey())+","+s.getKey()+","+s.getValue());
					bw.newLine();
					//					count++;
					temp.add(r);
				}
			}
		}
		bw.close();
	}

	public static HashMap<String,String> getsoseimap(File in) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		HashMap<String, String> list = new HashMap<String,String>();
		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			String num = tokens[0];
			String des = tokens[1];
			list.put(num, des);
		}
		br.close();
		return list;
	}

	public static String numchanger(String num){
		Integer n = Integer.valueOf(num);
		if(n<=4){
			String range = "0";
			return range;
		}
		else{
			String range = String.valueOf((int)(n-5)/5);
			return range;
		}
	}

}

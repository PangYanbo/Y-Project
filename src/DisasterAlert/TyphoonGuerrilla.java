package DisasterAlert;

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
import java.util.Map.Entry;

public class TyphoonGuerrilla {

	public static int count = 0;

	public static void main(String args[]) throws IOException{
		File in = new File("C:/users/yabetaka/desktop/Yahoo_Project2/DisasterModellingProject/bbb.csv");
		File out = new File("C:/users/yabetaka/desktop/xxx.csv");
		String type = "rain";

		HashMap<String, HashMap<String, HashMap<Double, String>>> map = alertsineacharea(in,type);
		writeoutresults(map, out);

	}

	public static void writeoutresults(HashMap<String, HashMap<String, HashMap<Double, String>>> map, File out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String date : map.keySet()){ //for each date
			for(String code : map.get(date).keySet()){
				HashMap<Double, String> inmap = map.get(date).get(code);
				if(inmap.containsValue("4")){
					String str = getStr(inmap);
					count++;
					bw.write(str);
					bw.newLine();
				}
			}
		}
		System.out.println(count);
		bw.close();
	}

	public static HashMap<String, HashMap<String, HashMap<Double, String>>> alertsineacharea(File in, String tp) throws IOException{
		HashMap<String, HashMap<String, HashMap<Double, String>>> res = new HashMap<String, HashMap<String, HashMap<Double, String>>>();
		// Date, Code, Time, Level
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String date = tokens[0].split(" ")[0]; // yyyy/mm/dd
			String time = tokens[0].split(" ")[1];
			Double hour = Double.parseDouble(time.split(":")[0]);
			Double mins = Double.parseDouble(time.split(":")[1]);
			//			String doubletime = String.valueOf(hour+mins/60).substring(0,4);
			Double doubletime = hour+mins/60;

			String type = tokens[1];
			String level = tokens[2];
			String[] codes = tokens[3].split(" ");
			if(type.equals(tp)){
				for(String code : codes){
					if(res.containsKey(date)){
						if(res.get(date).containsKey(code)){
							res.get(date).get(code).put(doubletime, level);
						}
						else{
							HashMap<Double, String> temp = new HashMap<Double, String>();
							temp.put(doubletime, level);
							res.get(date).put(code, temp);
						}
					}
					else{
						HashMap<Double, String> temp = new HashMap<Double, String>();
						temp.put(doubletime, level);
						HashMap<String, HashMap<Double, String>> tmp2 = new HashMap<String, HashMap<Double, String>>();
						tmp2.put(date,temp);
						res.put(date, tmp2);
					}
				}
			}
		}
		br.close();
		return res;
	}	

	public static String getStr(HashMap<Double, String> inmap){
		// inmap = time, level
		//Map.Entry ÇÃÉäÉXÉgÇçÏÇÈ
		List<Entry<Double, String>> entries = new ArrayList<Entry<Double, String>>(inmap.entrySet());

		//Comparator Ç≈ Map.Entry ÇÃílÇî‰är
		Collections.sort(entries, new Comparator<Entry<Double, String>>() {
			//î‰ärä÷êî
			@Override
			public int compare(Entry<Double, String> o1, Entry<Double, String> o2) {
				return o1.getKey().compareTo(o2.getKey());  
			}
		});

		ArrayList<String> temp = new ArrayList<String>();
		for (Entry<Double, String> e : entries) {
			temp.add(e.getValue());
		}

		Double start = entries.get(0).getKey();
		Double end   = entries.get(entries.size()-1).getKey(); 
		Double time = end - start;
		if(time<0){
			System.out.println("error: "+time+"="+end+"-"+start);
		}
		String strtime = String.valueOf(time);

		String res = "";
		for(String r : temp){
			res = res + ":" + r;
		}
		res = res + "," + strtime;
		return res;
	}

}

package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TypeCounter {

	public static void main(String args[]) throws IOException{
		
		File in = new File("C:/users/yabetaka/desktop/xxx.csv");
		File out = new File("C:/users/yabetaka/desktop/yyy.csv");
		
		readandcount(in,out);
		
	}
	
	public static void readandcount(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String line = null;
		while((line=br.readLine())!=null){
			String tokens[] = line.split(",");
			if(map.containsKey(tokens[0])){
				Integer count = map.get(tokens[0]) + 1;
				map.put(tokens[0], count);
			}
			else{
				map.put(tokens[0], 1);
			}
		}
		br.close();
		
		for(String seq : map.keySet()){
			bw.write(seq + "," + String.valueOf(map.get(seq)));
			bw.newLine();
		}
		bw.close();
	}
	
	public static void lookatinterval(File in, File out) throws IOException{
		// do it with excel filter!!
	}
	
}

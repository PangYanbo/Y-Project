package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class ID_LogCounter {

	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
		IdCounter(args[0],args[1]);
	}
	
//	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
//		String in = "c:/users/yabetaka/desktop/test.txt";
//		String out = "c:/users/yabetaka/desktop/testres.txt";
//		IdCounter(in,out);
//	}

	public static void IdCounter(String in, String out){
		int counter = 0;
		int errorLine = 0;
		HashMap<String, Integer> id_count = new HashMap<String,Integer>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
			String line = br.readLine();
			while ((line=br.readLine())!=null){
				String[] tokens = line.split("\t");
				if(tokens.length>1){
					String id = tokens[1];
					if(id_count.containsKey(id)){
						int count = id_count.get(id) + 1;
						id_count.put(id, count);
					}
					else{
						id_count.put(id, 1);
					}
					counter++;
					if(counter % 1 ==0){
						System.out.println("#done " + counter);
					}
				}
				else{
					errorLine++;
				}
			}
			System.out.println("done " + counter + " lines, and " + errorLine +" were error lines.");
			System.out.println("done sorting out, will start writing them out...");

			for(String str : id_count.keySet()){
				if(id_count.get(str)>0){
					bw.write(str + "\t" + id_count.get(str));
					bw.newLine();
				}
			}
			br.close();
			bw.close();
		}
		catch(FileNotFoundException xx) {
			System.out.println("File not found 5");
		}
		catch(IOException xxx) {
			System.out.println(xxx);
		}
	}
}

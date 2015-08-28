package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MLDataCleaner {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/test.csv");
		File out = new File("c:/users/yabetaka/desktop/testres.csv");

		DataClean(in,out);
	}

	public static void DataClean(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			ArrayList<String> temp = new ArrayList<String>();
			String[] tokens = line.split(" ");
			for(String s : tokens){
				if(!(s.split(":")[1].equals("null"))){
					if(!((Double.parseDouble(s.split(":")[1])==0))){
						temp.add(s);
					}
				}
			}
			for(String t : temp){
				bw.write(t+" ");
			}
			bw.newLine();
		}
		br.close();
		bw.close();
	}

}

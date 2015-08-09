package DataModify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ZDCtoYahoo {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/id_home.csv");
		File out = new File("c:/users/yabetaka/desktop/id_home_new.csv");
		ZDCtoYahooConvert(in,out);
	}
	
	public static File ZDCtoYahooConvert(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		int count = 0;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			bw.write(tokens[0] + "\t" + tokens[2] + "\t" + tokens[1]);
			bw.newLine();
			count++;
			if(count%10000==0){
				System.out.println(count);
			}
		}
		br.close();
		bw.close();
		return out;
	}


}

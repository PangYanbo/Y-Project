package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class deleteid {
	
	public static void main(String[] args) throws IOException{
		File in = new File("/home/c-tyabe/Data/idslogs_results.csv");
		File out = new File("/home/c-tyabe/Data/idslogs_results2.csv");
		modifyfile(in,out);
	}
	
	public static void modifyfile(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			bw.write(tokens[1]);
			bw.newLine();
		}	
		br.close();
		bw.close();
	}
}

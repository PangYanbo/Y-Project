package ToolsforFileManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyFile {
	
	public static void main(String args[]) throws IOException{
		
		File infile  = new File("C:/users/t-tyabe/desktop/xxx.csv");
		File outfile = new File("C:/users/t-tyabe/desktop/xxx2.csv");
		
		BufferedReader br = new BufferedReader(new FileReader(infile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
		String line = null;
		while((line=br.readLine())!=null){
			String endpart = line.split("#")[1];
			String normallogs = endpart.split("_")[0];
			String disasterlogs = endpart.split("_")[1].split("A")[0];
			
			bw.write(normallogs+","+disasterlogs);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

}

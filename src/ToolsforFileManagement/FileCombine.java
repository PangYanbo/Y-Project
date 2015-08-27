package ToolsforFileManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileCombine {

	public static void main(String args[]) throws IOException{
		File out = new File("c:/users/yabetaka/Desktop/landusedata.csv");
		File dir = new File("c:/users/yabetaka/Desktop/Landusedata/");
		File[] files = dir.listFiles();
		for(File f : files){
			System.out.println(f);
			if(isitdbf(f,"dbf")==true){
				combinefiles(f,out);
			}
		}
	}


	public static void combinefiles(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = br.readLine();
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			bw.write(tokens[0]+","+tokens[1]+","+tokens[3]+","+tokens[5]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

	public static boolean isitdbf(File in, String str){
		String filepath = in.toString();	
		int index = filepath.lastIndexOf(".");//Šg’£Žq‚Ì"."‚ð’T‚·
		String ext = filepath.substring(index).toLowerCase();
		if(ext.equals(str)){
			return true;			
		}
		else{
			return false;
		}
	}

}

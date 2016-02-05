package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SameNumberofLines {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/t-tyabe/desktop/eq.csv");
		File out = new File("c:/users/t-tyabe/desktop/eq2.csv");
		samenumberoflines(in,out);
	}
	
	
	public static void samenumberoflines(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
//		int count0 = 1000000;
		int count1 = 0;
		int count3 = 0;
		while((line=br.readLine())!=null){
			String val = line.split(",")[2];
			if(val.equals("0")){
//				count0++;
			}
			else if(val.equals("1")){
				count1++;
			}
			else if(val.equals("3")){
				count3++;
			}
			else{
//				count3++;
			}
		}
		br.close();
		
//		int min1 = Math.min(count0, count1);
		int min = Math.min(count1, count3);
		
//		Double rate0 = (double)min/(double)count0;
		Double rate1 = (double)min/(double)count1;
		Double rate3 = (double)min/(double)count3;
		
		BufferedReader br2 = new BufferedReader(new FileReader(in));
		
		while((line=br2.readLine())!=null){
			String val = line.split(",")[2];
			Double rand = Math.random();
//			if(val.equals("1")){
//				if(rand<=rate0){
//					bw.write(line);
//					bw.newLine();
//				}
//			}
			if(val.equals("1")){
				if(rand<=rate1){
					bw.write(line);
					bw.newLine();
				}
			}
			else if(val.equals("3")){
				if(rand<=rate3){
					bw.write(line);
					bw.newLine();
				}
			}
			else{
//				if(rand<=rate3){
//					bw.write(line);
//					bw.newLine();
//				}
			}
		}
		br2.close();
		bw.close();
	}

	
}

package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SameNumberofLines {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/t-tyabe/desktop/rain.csv");
		File out = new File("c:/users/t-tyabe/desktop/rain2.csv");
		samenumberoflines(in,out);
	}
	
	
	public static void samenumberoflines(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		
		while((line=br.readLine())!=null){
			String val = line.split(",")[0];
			if(val.equals("1")){
				count1++;
			}
			else if(val.equals("2")){
				count2++;
			}
			else if(val.equals("3")){
				count3++;
			}
			else{
				count4++;
			}
		}
		br.close();
		
		int min1 = Math.min(count1, count2);
		int min2 = Math.min(min1, count3);
		int min = Math.min(min2, count4);

		Double rate1 = (double)min/(double)count1;
		Double rate2 = (double)min/(double)count2;
		Double rate3 = (double)min/(double)count3;
		Double rate4 = (double)min/(double)count4;
		
		BufferedReader br2 = new BufferedReader(new FileReader(in));
		
		while((line=br2.readLine())!=null){
			String val = line.split(",")[2];
			Double rand = Math.random();
			if(val.equals("1")){
				if(rand<=rate1){
					bw.write(line);
					bw.newLine();
				}
			}
			if(val.equals("2")){
				if(rand<=rate2){
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
				if(rand<=rate4){
					bw.write(line);
					bw.newLine();
				}
			}
		}
		br2.close();
		bw.close();
	}

	
}

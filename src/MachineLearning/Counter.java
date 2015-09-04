package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Counter {
	public static void count(File in) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		int count = 0;
		int count1 = 0;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			if(tokens[0].equals("1")){
				count++;
			}
			else{
				count1++;
			}
		}
		br.close();
		System.out.println(count + " "+ count1 + " " + count/count1);
	}
	
	public static void main(String args[]) throws IOException{
		String type = "eq";
		String subject = "office_exit_diff";
		File in = new File("/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach3.csv");
		count(in);
	}
	
}

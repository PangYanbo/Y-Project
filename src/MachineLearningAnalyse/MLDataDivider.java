package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MLDataDivider {

	public static void main(String args[]) throws IOException{

		File in = new File("/home/c-tyabe/Data/MLResults_"+args[0]+"12/"+args[1]+"_diff_ML2_plusminus_lineforeach.csv");
//		File out1 = new File("/home/c-tyabe/Data/MLResults_rain8_irr/"+args[0]+"_diff_ML_cleaned_norfast.csv");
//		File out2 = new File("/home/c-tyabe/Data/MLResults_rain8_irr/"+args[0]+"_diff_ML_cleaned_norslow.csv");
//
//		makeMLFile(in,out1,"-1");
//		makeMLFile(in,out2,"1");
		alwaysheiji(in);
	}

	public static HashMap<String,ArrayList<String>> makeMLFile(File in, File out, String notthis) throws IOException{
		HashMap<String,ArrayList<String>> res = new HashMap<String,ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!= null){
			String[] tokens = line.split(" ");
			String val = tokens[0];
			if(!val.equals(notthis)){
				bw.write(line);
				bw.newLine();
			}
		}
		br.close();		
		bw.close();
		return res;
	}

	public static void count(File in) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		int count1 = 0;
		int counti = 0;
		int all = 0;
		while((line=br.readLine())!= null){
			String[] tokens = line.split(" ");
			String val = tokens[0];
			Double ran = Math.random();
			String rancon = randheiji(ran);
			if(val.equals(rancon)){
				count1++;
			}
			else{
				counti++;
			}
			all++;
		}
		br.close();		
		System.out.println((double)count1/(double)all + ","+ (double)counti/(double)all);
	}
	
	public static void alwaysheiji(File in) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		int count1 = 0;
		int count0 = 0;
		int counti = 0;
		int all = 0;
		while((line=br.readLine())!= null){
			String[] tokens = line.split(" ");
			String val = tokens[0];
			String rancon = "0";
			String one = "1";
			if(val.equals(rancon)){
				count0++;
			}
			else if (val.equals(one)){
				count1++;
			}
			else{
				counti++;
			}
			all++;
		}
		br.close();		
		System.out.println((double)count1/(double)all+","+ (double)count0/(double)all + ","+ (double)counti/(double)all);
	}
	
	public static String randheiji(Double random){
		if(random<0.95){
			return "0";
		}
		else if (random<0.975){
			return "1";
		}
		else{
			return "-1";
		}
	}

	public static String rand(Double random){
		if(random<0.33){
			return "0";
		}
		else if (random<0.66){
			return "1";
		}
		else{
			return "-1";
		}
	}
	
}

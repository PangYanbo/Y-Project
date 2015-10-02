package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SoukanChecker {

	public static void main(String args[]) throws IOException{

		String subject = args[0];
		String c = args[1];
		String b = args[2];
		
		File in1 = new File("/home/c-tyabe/yabe/tmp/"+subject+"_ML_plusminus_lineforeach.csv_model_"+c+"_"+b+"_out");
		File in2 = new File("/home/c-tyabe/yabe/tmp/evaldata_a.txt");
//		File out = new File("/home/c-tyabe/yabe/soukan/"+subject+".csv");
		
		System.out.println("Correlation Coefficient for "+ subject +" is: "+getCorrelation(combinefiles(in1,in2)));
		
	}

	public static ArrayList<ArrayList<String>> combinefiles(File in1, File evaldata) throws IOException{
		BufferedReader br1 = new BufferedReader(new FileReader(in1));
		BufferedReader br2 = new BufferedReader(new FileReader(evaldata));
		ArrayList<String> temp1 = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();

		String line1 = br1.readLine();
		while((line1=br1.readLine())!=null){
			String[] tokens1 = line1.split(" ");
			temp1.add(tokens1[1]);
		}
		br1.close();

		String line2 = null;
		while((line2=br2.readLine())!=null){
			String[] tokens2 = line2.split(" ");
			String res = tokens2[tokens2.length-1].replace("#","");
			temp2.add(res);
		}
		br2.close();

		out.add(temp1);
		out.add(temp2);
		return out;
	}

	public static Double getCorrelation(ArrayList<ArrayList<String>> in){
		Double correl = 0d;
		Double sigma1 = getSigma(in.get(0));
		Double sigma2 = getSigma(in.get(1));
		
		Double avg1 = getAvg(in.get(0));
		Double avg2 = getAvg(in.get(1));
		
		Double tempsum = 0d;
		for(int i=0; i<in.get(0).size(); i++){
			tempsum = tempsum + (Double.parseDouble(in.get(0).get(i))-avg1)*(Double.parseDouble(in.get(1).get(i))-avg2);
		}
		Double upper = tempsum/(double)in.get(0).size();

		correl = upper/(sigma1*sigma2);
		return correl;
	}
	
	public static Double getAvg(ArrayList<String> in){
		Double tempsum = 0d;
		for(String x : in){
			Double xd = Double.parseDouble(x);
			tempsum = tempsum + xd;
		}
		// got sum above
		Double avg = tempsum/(double)in.size();
		return avg;
	}
	
	public static Double getSigma(ArrayList<String> in){
	
		Double avg = getAvg(in);
		Double tempsum2 = 0d;
		for(String x2 : in){
			Double xd2 = Double.parseDouble(x2);
			tempsum2 = tempsum2 + Math.pow(xd2-avg, 2);
		}
		Double res = Math.pow(tempsum2/(double)in.size(), 0.5);
		return res;
	}
	
}

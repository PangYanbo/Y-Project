package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SoukanChecker {

	public static void main(String args[]) throws IOException{

		String subject = "home_exit_diff";
		String c = "0.1";
		String b = "1";
		
		File in1 = new File("/home/c-tyabe/yabe/tmp/"+subject+"_ML_plusminus_lineforeach.csv_model_"+c+"_"+b+"_out");
		File in2 = new File("/home/c-tyabe/yabe/tmp/evaldata_a.txt");
		File out = new File("/home/c-tyabe/yabe/soukan/"+subject+".csv");
		
		combinefiles(in1,in2,out);
		
	}

	public static File combinefiles(File in1, File evaldata, File out) throws IOException{
		BufferedReader br1 = new BufferedReader(new FileReader(in1));
		BufferedReader br2 = new BufferedReader(new FileReader(evaldata));
		ArrayList<String> temp1 = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();

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

		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		if(temp1.size()==temp2.size()){
			for(int i = 0; i<temp1.size(); i++){
				bw.write(temp1.get(i)+","+temp2.get(i));
				bw.newLine();
			}
		}
		else{
			System.out.println("didnt match number of lines");
		}
		bw.close();

		return out;
	}

}

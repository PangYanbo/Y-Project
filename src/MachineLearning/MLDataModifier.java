package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MLDataModifier {

	public static void main(String args[]) throws IOException{

		ArrayList<String> types = new ArrayList<String>();
		types.add("rain");
		types.add("eq");	
		types.add("dosha");
		for(String type : types){

			ArrayList<String> subjects = new ArrayList<String>();
			subjects.add("tsukin_time_diff");
			subjects.add("office_time_diff");
			subjects.add("kitaku_time_diff");
			subjects.add("home_exit_diff");
			subjects.add("home_return_diff");
			subjects.add("office_enter_diff");
			subjects.add("office_exit_diff");

			for(String subject : subjects){
				String in = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_no2.csv";
				String in2 = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach2.csv";

				String newoutfile   = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_no3.csv"; 
				String newoutfile2   = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach3.csv"; 
				DeleteBisho(new File(in), new File(newoutfile));
				DeleteBisho(new File(in2), new File(newoutfile2));

				//			String multiplelines = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach2.csv";
				//			MLDataModifier.Modify(new File(in), new File(multiplelines));
			}
		}
	}

	public static void Modify(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			Double y = Double.parseDouble(tokens[0]);

			Integer numoflines = numofline(y);

			for(int i = 0; i<numoflines; i++){
				bw.write(line);
				bw.newLine();
			}

		}
		br.close();
		bw.close();
	}

	public static int numofline(Double num){
		double num2 = Math.abs(num);
		int res = 0;
		if(num2<10){
			res = (int)Math.round(num2);
			if(res==0){
				res = 1;
			}
		}
		else if(num2>=10){
			res = 10;
		}
		else{
			res = 0;
		}
		return res;
	}

	public static void DeleteBisho(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			ArrayList<String> temp = new ArrayList<String>();
			String[] tokens = line.split(" ");
			for(String token: tokens){
				if(token.split(":").length==2){
					if(!token.split(":")[1].contains("E")){
						temp.add(token);
					}
				}
				else{
					temp.add(token);
				}
			}
			for(String t : temp){
				bw.write(t+" ");
			}
			bw.newLine();
		}
		br.close();
		bw.close();
	}

}

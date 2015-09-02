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

		String type = "dosha";
		
		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("tsukin_time_diff");
		subjects.add("office_time_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_exit_diff");
		subjects.add("home_return_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_exit_diff");

		for(String subject : subjects){
			String in = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach.csv.csv";
			
			String newoutfile   = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach2.csv"; 
			MLDataCleaner.DataClean(new File(in), new File(newoutfile));
			
//			String multiplelines = "/home/c-tyabe/Data/MLResults_"+type+"/"+subject+"_ML_lineforeach2.csv";
//			MLDataModifier.Modify(new File(in), new File(multiplelines));
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
		if((num2>=1)&&(num2<10)){
			res = (int)Math.round(num2);
		}
		else if(num2>=10){
			res = 10;
		}
		else{
			res = 1;
		}
		return res;

	}


}

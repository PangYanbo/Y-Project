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

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("tsukin_time_diff");
		subjects.add("office_time_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_exit_diff");
		subjects.add("home_return_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_exit_diff");

		for(String subject : subjects){
			File in = new File("c:/users/c-tyabe/Desktop/Exfiles/"+subject+"_ML.csv");
			File out = new File("c:/users/c-tyabe/Desktop/Exfiles/"+subject+"_ML_ver3.csv");
			Modify(in,out);
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
		int res = 0;
		String abs = String.valueOf(Math.abs(num));
		res = Integer.valueOf(abs);
		return res;

	}


}

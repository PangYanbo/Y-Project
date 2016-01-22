package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Only1orMinus {

	public static void main(String args[]) throws IOException{

		ArrayList<String> disasters = new ArrayList<String>();
		disasters.add("rain");
		disasters.add("eq");
		disasters.add("heats");
//		disasters.add("dosha");

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit");
		subjects.add("tsukin_time");
		subjects.add("office_enter");
		subjects.add("office_time");
		subjects.add("office_exit");
		subjects.add("kitaku_time");
		subjects.add("home_return");

		for(String disaster : disasters){
			for(String subject : subjects){
				String in = "/home/c-tyabe/Data/MLResults_"+disaster+"13/forML/calc/"+subject+"_diff_ML2_plusminus_lineforeach.csv";
				String out = "/home/c-tyabe/Data/MLResults_"+disaster+"13/forML/calc/"+subject+"_diff_ML2_plusminus_lineforeach_nozero.csv";
				removezero(in,out);
			}
		}
	}

	public static void removezero(String in, String out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split(" ");
			String date = tokens[0];
			if(!date.equals("0")){
				bw.write(line);
				bw.newLine();
			}
		}
		br.close();
		bw.close();
	}

}

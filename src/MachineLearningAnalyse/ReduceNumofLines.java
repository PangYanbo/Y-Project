package MachineLearningAnalyse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReduceNumofLines {

	public static void main(String[] args) throws IOException{

		ArrayList<String> disasters = new ArrayList<String>();
		disasters.add("rain");
		disasters.add("eq");
		disasters.add("heats");

		for(String dis : disasters){

			ArrayList<String> subjects = new ArrayList<String>();
			subjects.add("home_exit_diff");
			subjects.add("tsukin_time_diff");
			subjects.add("office_enter_diff");
			subjects.add("office_time_diff");
			subjects.add("office_exit_diff");
			subjects.add("kitaku_time_diff");
			subjects.add("home_return_diff");

			for(String subject:subjects){
				File in = new File("/home/c-tyabe/Data/MLResults_"+dis+"13/"+subject+"_diff_ML2_lineforeach.csv");
				File out = new File("/home/c-tyabe/Data/MLResults_"+dis+"13/"+subject+"_diff_ML2_lineforeach_short.csv");
				BufferedReader br = new BufferedReader(new FileReader(in));
				BufferedWriter bw = new BufferedWriter(new FileWriter(out));
				String line = null;
				Integer count = 0;
				while((line=br.readLine())!=null){
					count++;
				}
				Double rate = 100000d/(double)count;

				BufferedReader br2 = new BufferedReader(new FileReader(in));
				String line2 = null;
				while((line2=br2.readLine())!=null){
					Double rand = Math.random();
					if(rand<=rate){
						bw.write(line2);
						bw.newLine();
					}
				}
				br.close();
				br2.close();
				bw.close();
			}
		}
	}

}

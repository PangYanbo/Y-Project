package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MLDataCleaner {

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
			File in = new File("c:/users/c-tyabe/Desktop/Exfiles/"+subject+"_ML_ver2.csv");
			File out = new File("c:/users/c-tyabe/Desktop/Exfiles/"+subject+"_ML_ver3.csv");
			DataClean(in,out);
		}
	}

	public static void DataClean(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			ArrayList<String> temp = new ArrayList<String>();
			String[] tokens = line.split(" ");
			for(String s : tokens){
				if(s.split(":").length==2){
					if(!(s.split(":")[1].equals("null"))){
						if(!((Double.parseDouble(s.split(":")[1])==0))){
							temp.add(s);
						}
					}
				}
				else{
					temp.add(s);
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

	public static void RemoveOne(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			ArrayList<String> temp = new ArrayList<String>();
			String[] tokens = line.split(" ");
			for(String s : tokens){
				if(s.split(":")[0].equals("1")){
					temp.add(s.split(":")[1]);
				}
				else{
					temp.add(s);
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

	public static void MakeOne(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			ArrayList<String> temp = new ArrayList<String>();
			String[] tokens = line.split(" ");
			for(String s : tokens){
				if(s.split(":")[0].equals("1")){
					temp.add(s.split(":")[1]);
				}
				else{
					temp.add(s);
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

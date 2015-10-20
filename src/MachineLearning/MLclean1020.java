package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//ML Data for normal-irregular decision

public class MLclean1020 {

	public static final String type      = "rain";
	public static final String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"9/";
	public static final String outdir2   = outdir+"forML/";
	public static final String outdir3   = outdir+"forML/calc/";

	public static void main(String args[]) throws IOException{

		File outputdir  = new File(outdir);  outputdir.mkdir();
		File outputdir2 = new File(outdir2); outputdir2.mkdir();
		File outputdir3 = new File(outdir3); outputdir3.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit_diff");
		subjects.add("tsukin_time_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_time_diff");
		subjects.add("office_exit_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_return_diff");
		run(subjects);

	}

	public static void run(ArrayList<String> subjects) throws IOException{


		for(String subject : subjects){
			String infile   = outdir3+subject+"_ML_plusminus_lineforeach.csv"; 
			String outfile   = outdir3+subject+"_ML2_plusminus_lineforeach.csv"; 
			
			naosu(infile,outfile);
		}
	}


	public static void naosu(String infile, String outfile) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile),true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			bw.write(tokens[0]);
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=1; i<tokens.length; i++){
				Integer num = Integer.valueOf(tokens[i].split(":")[0]);
				list.add(num);
			}
			Collections.sort(list);
			for(Integer n : list){
				bw.write(" "+String.valueOf(n)+":1");
			}
			bw.write(" "+tokens[tokens.length-1]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}


}


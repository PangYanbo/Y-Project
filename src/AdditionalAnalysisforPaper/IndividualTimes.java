package AdditionalAnalysisforPaper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IndividualTimes {

	/*
	 * do シャピロ-ウィルクの正規性の検定 in python (shapiroooo.py)
	 * 
	 */

	public static void main(String args[]) throws IOException{

		ArrayList<String> dirs = new ArrayList<String>();
		dirs.add("/home/c-tyabe/Data/eqTokyo6/");
		dirs.add("/home/c-tyabe/Data/rainTokyo6/");
		dirs.add("/home/c-tyabe/Data/heatsTokyo6/");

		String outdir    = "/home/c-tyabe/Data/exp3res/";
		File outputdir  = new File(outdir);  outputdir.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit_diff");
		subjects.add("tsukin_time_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_time_diff");
		subjects.add("office_exit_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_return_diff");
		run(subjects, dirs, outdir, Double.parseDouble(args[0]), Double.parseDouble(args[1]));

	}

	public static void run(ArrayList<String> subjects, ArrayList<String> dirs, String outdir, Double starttime, Double endtime) throws IOException{

		for(String subject : subjects){
			
			String outfile   = outdir+subject+"_forexp3.csv"; 
			for(String dir : dirs){
				File typelevel = new File(dir);
				for(File datetime :typelevel.listFiles()){
					for(File f : datetime.listFiles()){
						if(f.toString().contains(subject)){
							System.out.println("#working on " + f.toString());
							getAttributes(f,new File(outfile));
						}}}

			}
		}
	}

	public static void getAttributes(File in, File out) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0]; String movementtime = tokens[3];
			String label = tokens[1];

			if(!label.equals("2")){
				bw.write(id+","+movementtime);
			}
			bw.newLine();
		}
		br.close();
		bw.close();
	}


}

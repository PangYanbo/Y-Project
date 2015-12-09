package AdditionalAnalysisforPaper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ActionChangesbyDisaster {

	/*
	 * eqTokyo6, rainTokyo6 のhighest levelファイルを全部読む 
	 * 災害発生時刻以後に動いている人only
	 * 災害レベル0も残す
	 * into python
	 * 
	 * args = start and end time
	 * 
	 */

	public static void main(String args[]) throws IOException{

		String outdir    = "/home/c-tyabe/Data/exp2res/";
		File outputdir  = new File(outdir);  outputdir.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
		//		subjects.add("home_exit.csv");
		//		subjects.add("tsukin_time.csv");
		//		subjects.add("office_enter.csv");
		//		subjects.add("office_time.csv");
		//		subjects.add("office_exit.csv");
		subjects.add("kitaku_time.csv");
		subjects.add("home_return.csv");

		ArrayList<String> dirs = new ArrayList<String>();
		dirs.add("/home/c-tyabe/Data/rainTokyo6/rain_3/");
		dirs.add("/home/c-tyabe/Data/eqTokyo6/eq_3/");
		dirs.add("/home/c-tyabe/Data/doshaTokyo6/dosha_10");

		run(subjects, dirs, outdir, Double.parseDouble(args[0]), Double.parseDouble(args[1]));

	}

	public static void run(ArrayList<String> subjects, ArrayList<String> dirs, String outdir, Double starttime, Double endtime) throws IOException{

		for(String subject : subjects){
			String outfile   = outdir+subject+"_forexp2.csv"; 

			HashSet<String> id_dates = new HashSet<String>();
			HashSet<String> id_dates_rain = new HashSet<String>();

			for(String dir : dirs){
				File typelevel = new File(dir);
				for(File datetime :typelevel.listFiles()){
					String date = datetime.getName().split("_")[0];
					String time = datetime.getName().split("_")[1];
					Double saigaitime  = Double.parseDouble(time);
					if((saigaitime>starttime)&&(saigaitime<endtime)){
						for(File f : datetime.listFiles()){
							if(f.toString().contains(subject)){
								System.out.println("#working on " + f.toString());
								String type = gettype(dir);
								getAttributes(f,new File(outfile), type ,date,time, subject, id_dates, id_dates_rain);
							}}}}}}}

	public static void getAttributes(File in, File out, String type, String date, String time,
			String subject, HashSet<String> id_date, HashSet<String> id_date_rain) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0]; String movementtime = tokens[3];	String label = tokens[1];

			if(label.equals("2")){
				if(type.equals("1")){
					if(!id_date_rain.contains(id)){
						bw.write(id+","+type+","+movementtime);
						bw.newLine();
					}
					else{
						System.out.println("yeah");
					}
					id_date_rain.add(id);
				}
				else{
					bw.write(id+","+type+","+movementtime);
					bw.newLine();
				}
			}
			else{
				if(!id_date.contains(id)){
					bw.write(id+",0,"+movementtime);
					bw.newLine();
				}
			}
			id_date.add(id);
			//			}
		}

		br.close();
		bw.close();
	}

	public static String gettype(String dir){
		if(dir.equals("/home/c-tyabe/Data/rainTokyo6/rain_3/")){
			return "1";
		}
		else if (dir.equals("/home/c-tyabe/Data/eqTokyo6/eq_3/")){
			return "2";
		}
		else{
			return "3";
		}
	}

}


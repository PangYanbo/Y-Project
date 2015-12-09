package AdditionalAnalysisforPaper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ActionChangesbyLevel {

	/**@author yabetaka
	 * 
	 * this program gets the possibility distribution for all the people's actions for different levels
	 * 
	 * eqTokyo6のファイルを全部読む 
	 * 災害発生時刻以後に動いている人only
	 * 災害レベルも残す
	 * into python
	 * 
	 * args = start and end time
	 * 
	 */

	public static void main(String args[]) throws IOException{

		String type      = "eq";	
		String dir       = "/home/c-tyabe/Data/"+type+"Tokyo6/";
		String outdir    = "/home/c-tyabe/Data/"+type+"exp1res/";
		File outputdir  = new File(outdir);  outputdir.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
//		subjects.add("home_exit.csv");
//		subjects.add("tsukin_time.csv");
//		subjects.add("office_enter.csv");
//		subjects.add("office_time.csv");
//		subjects.add("office_exit.csv");
		subjects.add("kitaku_time.csv");
		subjects.add("home_return.csv");
		run(subjects, dir, outdir, type, Double.parseDouble(args[0]), Double.parseDouble(args[1]));

	}

	public static void run(ArrayList<String> subjects, String dir, String outdir, String type, Double starttime, Double endtime) throws IOException{

		for(String subject : subjects){
			String outfile   = outdir+subject+"_forexp1.csv"; 

			ArrayList<String> done_ids = new ArrayList<String>();

			int start;
			int end;
			if(type.equals("rain")){
				start = 4; end = 1;
			}
			else if(type.equals("eq")||type.equals("heats")){
				start = 3; end = 1;
			}
			else{
				start = 10; end = 10;
			}

			for(int l=start; l>=end; l--){
				File typelevel = new File(dir+type+"_"+String.valueOf(l)+"/");
				String level = String.valueOf(l);
				for(File datetime :typelevel.listFiles()){
					String date = datetime.getName().split("_")[0];
					String time = datetime.getName().split("_")[1];
					Double saigaitime  = Double.parseDouble(time);
					if((saigaitime>starttime)&&(saigaitime<endtime)){
						for(File f : datetime.listFiles()){
							if(f.toString().contains(subject)){
								System.out.println("#working on " + f.toString());
								getAttributes(f,new File(outfile),level,date,time, subject, done_ids);
							}}}
				}
			}

		}
	}

	public static void getAttributes(File in, File out, String level, String date, String time,
			String subject, ArrayList<String> done_ids) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0]; String movementtime = tokens[3];
			String label = tokens[1];


			//			Double saigaitime  = Double.parseDouble(time);
			//			Double toujitutime = Double.parseDouble(movementtime);

			//			if(saigaitime<toujitutime){

			if(label.equals("2")){
				bw.write(id+","+level+","+movementtime);
				bw.newLine();
			}
			else{
				if(!done_ids.contains(id)){
					bw.write(id+",0,"+movementtime);
					bw.newLine();
				}
			}
			done_ids.add(id);
			//			}
		}

		br.close();
		bw.close();
	}

}



package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

//ML Data for normal-irregular decision

public class MLData3 {

	public static final double k = 2; 

	static File shapedir = new File("/home/c-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	public static void main(String args[]) throws IOException{

		ArrayList<String> disasters = new ArrayList<String>();
		disasters.add("rain");
		disasters.add("eq");
		disasters.add("heats");

		for(String type : disasters){
			String dir       = "/home/c-tyabe/Data/"+type+"Tokyo6/";
			String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"14/";
			String outdir2   = outdir+"forML/";
			String outdir3   = outdir+"forML/calc/";
			String outdir4   = outdir+"forML/calc/sameexp/";

			File outputdir  = new File(outdir);  outputdir.mkdir();
			File outputdir2 = new File(outdir2); outputdir2.mkdir();
			File outputdir3 = new File(outdir3); outputdir3.mkdir();
			File outputdir4 = new File(outdir4); outputdir4.mkdir();

			ArrayList<String> subjects = new ArrayList<String>();
			subjects.add("home_exit_diff");
			subjects.add("tsukin_time_diff");
			subjects.add("office_enter_diff");
			subjects.add("office_time_diff");
			subjects.add("office_exit_diff");
			subjects.add("kitaku_time_diff");
			subjects.add("home_return_diff");
			runMLData(subjects, dir, outdir, outdir2, outdir3, outdir4, type);

		}
	}

	public static void runMLData(ArrayList<String> subjects, String dir, String outdir, String outdir2, String outdir3, String outdir4, String type) throws IOException{

		HashMap<String, HashMap<String,String>> homeexit   = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> officeent  = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> officeexit = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_he     = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_ox     = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_oe     = new HashMap<String, HashMap<String,String>>();
		//		HashMap<String, HashMap<String,String>> motifmap   = new HashMap<String, HashMap<String,String>>();

		for(File typelevel : new File(dir).listFiles()){
			String level = typelevel.getName().split("_")[1];
			for(File datetime :typelevel.listFiles()){
				String date = datetime.getName().split("_")[0];
				String time = datetime.getName().split("_")[1];
				for(File f : datetime.listFiles()){
					if(f.toString().contains("home_exit_diff")){
						getActionMap(f,level,date+time,homeexit);
						getSaigaiMap(f,level,date+time,dis_he);
					}
					else if(f.toString().contains("office_enter_diff")){
						getActionMap(f,level,date+time,officeent);
						getSaigaiMap(f,level,date+time,dis_oe);
					}
					else if(f.toString().contains("office_exit_diff")){
						getActionMap(f,level,date+time,officeexit);
						getSaigaiMap(f,level,date+time,dis_ox);
					}

				}}}

		//--- test if working properly
		System.out.println("#actionmap for home exit : " + homeexit.size());
		System.out.println("#actionmap for ofce exit : " + officeexit.size());
		System.out.println("#saigai timemap for home exit : " + dis_he.size());
		System.out.println("#saigai timemap for ofce exit : " + dis_oe.size());
		//		System.out.println("#motif map size : " + motifmap.size());

		for(String subject : subjects){
			String outfile   = outdir+subject+"_ML.csv"; 

			HashMap<String, ArrayList<String>> id_dates = new HashMap<String, ArrayList<String>>();

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
					for(File f : datetime.listFiles()){
						if(f.toString().contains(subject)){
							System.out.println("#working on " + f.toString());
							getAttributes(f,new File(outfile),level,date,time,
									//									popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
									homeexit, officeent, officeexit, dis_he, dis_oe, dis_ox, subject, id_dates);
						}}}
			}

			String newoutfile   = outdir+subject+"_ML_cleaned.csv"; 
			MLDataCleaner.DataClean(new File(outfile), new File(newoutfile)); //delete 0s and Es

			String plusminus_normal  = outdir2+subject+"_ML_plusminus_normal.csv";
			MLDataCleaner.ytoone2(new File(newoutfile), new File(plusminus_normal),k);

			String multiplelines = outdir+subject+"_ML_lineforeach.csv";
			MLDataModifier.Modify(new File(newoutfile), new File(multiplelines));

			String multiplelinesclean = outdir+subject+"_ML2_lineforeach.csv";
			MLclean1020.naosu(multiplelines, multiplelinesclean);

			String plusminus_multiplelines = outdir3+subject+"_ML_plusminus_lineforeach.csv";
			MLDataCleaner.ytoone2(new File(multiplelines), new File(plusminus_multiplelines),k);

			String plusminus_multiplelinesclean = outdir3+subject+"_ML2_plusminus_lineforeach.csv";
			MLclean1020.naosu(plusminus_multiplelines, plusminus_multiplelinesclean);

			/*
			 * same number of lines between -1, 0, 1 
			 */
			String plusminus_multiplelinesclean_samenumlines = outdir4+subject+"_ML2_plusminus_lineforeach_same.csv";
			samenumberoflines(new File(plusminus_multiplelinesclean), new File(plusminus_multiplelinesclean_samenumlines));
		}
	}

	public static void getActionMap(File in, String level, String datetime,HashMap<String, HashMap<String,String>> res) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String diff = tokens[1];
			if(res.containsKey(id)){
				res.get(id).put(datetime+level, diff);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(datetime+level, diff);
				res.put(id, temp);
			}
		}
		br.close();
	}

	public static void getSaigaiMap(File in, String level, String datetime,HashMap<String, HashMap<String,String>> res) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String time = tokens[11];
			if(res.containsKey(id)){
				res.get(id).put(datetime+level, time);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(datetime+level, time);
				res.put(id, temp);
			}
		}
		br.close();
	}

	public static void getAttributes(File in, File out, String level, String date, String time,
			//			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			//			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			//			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap,
			HashMap<String, HashMap<String,String>> homeexit, HashMap<String, HashMap<String,String>>officeexit, 
			HashMap<String, HashMap<String,String>> dis_he, HashMap<String, HashMap<String,String>>dis_oe, 
			HashMap<String, HashMap<String,String>> officeent, HashMap<String, HashMap<String,String>>dis_ox, 
			//			HashMap<String, HashMap<String,String>> motifmap, 
			String subject, HashMap<String,ArrayList<String>> id_date) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String id = null; String diff = null; String normaltime = null; String disdaytime = null; Double sigma = 0d;

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				id = tokens[0]; diff = tokens[1]; 
				disdaytime = tokens[11]; normaltime = tokens[12]; 
				sigma = Double.parseDouble(tokens[13]);
			}
			else{ // output version 2
				id = tokens[0]; diff = tokens[1]; 
				disdaytime = tokens[11]; normaltime = tokens[12]; sigma = Double.parseDouble(tokens[13]);
			}

			Double saigaitime  = Double.parseDouble(time);
			Double toujitutime = Double.parseDouble(disdaytime);

			if((subject.equals("tsukin_time_diff"))||(subject.equals("office_time_diff"))||(subject.equals("kitaku_time_diff"))){
				toujitutime = 30d;
			}

			if(saigaitime<toujitutime){

				if(id_date.containsKey(id)){
					if(!id_date.get(id).contains(date)){

						ArrayList<String> list = new ArrayList<String>();
						for(String l  : GetLevel.getLevel(level).split(",")){ //level (0,0,0,0 etc.) 1-4
							list.add(l);
						}
						for(String t  : BinsforMLData3.timerange(time).split(",")){ //time of disaster 5-9
							list.add(t);
						}
						for(String df : BinsforMLData3.getline4Diffs(subject, normaltime).split(",")){ //10-14
							list.add(df);
						}
						for(String si : BinsforMLData3.sigmaline(k*sigma).split(",")){ //15-19
							list.add(si);
						}

						bw.write(diff);

						for(int i = 1; i<=list.size(); i++){
							bw.write(" "+i+":"+list.get(i-1));
						}

						String bilinearline = BinsforMLData3.bilinearline2(level,time,subject,normaltime);
						bw.write(bilinearline);
						bw.write(" 1000000:1");
						bw.write(" #"+diff+"A"+sigma);
						bw.newLine();
						id_date.get(id).add(date);
					}
				}
				else{
					ArrayList<String> list = new ArrayList<String>();
					for(String l  : GetLevel.getLevel(level).split(",")){ //level (0,0,0,0 etc.) 1-4
						list.add(l);
					}
					for(String t  : BinsforMLData3.timerange(time).split(",")){ //time of disaster 5-9
						list.add(t);
					}
					for(String df : BinsforMLData3.getline4Diffs(subject, normaltime).split(",")){ //10-14
						list.add(df);
					}
					for(String si : BinsforMLData3.sigmaline(k*sigma).split(",")){ //15-19
						list.add(si);
					}

					bw.write(diff);

					for(int i = 1; i<=list.size(); i++){
						bw.write(" "+i+":"+list.get(i-1));
					}
					String bilinearline = BinsforMLData3.bilinearline2(level,time,subject,normaltime);
					bw.write(bilinearline);
					bw.write(" 1000000:1");
					bw.write(" #"+diff+"A"+sigma);
					bw.newLine();
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(date);
					id_date.put(id, temp);
				}
			}
		}

		br.close();
		bw.close();
	}

	public static String getCode(double lon, double lat){
		List<String> list = gchecker.listOverlaps("JCODE", lon, lat);
		if(!list.isEmpty()){
			return list.get(0);
		}
		else{
			return "null";
		}
	}

	public static LonLat StringtoLonLat(String x){
		String[] tokens = x.split(",");
		String slon = tokens[0].replace("(", "");
		String slat = tokens[1].replace(")", "");
		Double lon = Double.parseDouble(slon);
		Double lat = Double.parseDouble(slat);
		LonLat p = new LonLat(lon,lat);
		return p;
	}

	public static boolean isEarly(String disastertime, String disdayactiontime){
		Double disaster = Double.parseDouble(disastertime);
		Double disdayaction = Double.parseDouble(disdayactiontime);
		if(disdayaction<disaster){
			return true;
		}
		else{
			return false;
		}
	}

	public static File samenumberoflines(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		int count0 = 0;
		int count1 = 0;
		int countm = 0;
		while((line=br.readLine())!=null){
			String val = line.split(" ")[0];
			if(val.equals("0")){
				count0++;
			}
			else if(val.equals("1")){
				count1++;
			}
			else{
				countm++;
			}
		}
		br.close();

		int min1 = Math.min(count0, count1);
		int min = Math.min(min1, countm);

		Double rate0 = (double)min/(double)count0;
		Double rate1 = (double)min/(double)count1;
		Double ratem = (double)min/(double)countm;

		BufferedReader br2 = new BufferedReader(new FileReader(in));

		while((line=br2.readLine())!=null){
			String val = line.split(" ")[0];
			Double rand = Math.random();
			if(val.equals("0")){
				if(rand<=rate0){
					bw.write(line);
					bw.newLine();
				}
			}
			else if(val.equals("1")){
				if(rand<=rate1){
					bw.write(line);
					bw.newLine();
				}
			}
			else{
				if(rand<=ratem){
					bw.write(line);
					bw.newLine();
				}
			}
		}
		br2.close();
		bw.close();

		return out;
	}

}

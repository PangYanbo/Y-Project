package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import MachineLearning.GetLandPrice;
import MachineLearning.GetLanduse;
import MachineLearning.GetPop;
import MachineLearning.GetRoadData;
import MachineLearning.GetTrainData;

//ML Data for normal-irregular decision

public class DataMaker {

	public static final File popfile     = new File("/home/t-tyabe/Data/DataforML/mesh_daytimepop.csv");
	public static final File landusefile = new File("/home/t-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/t-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/t-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/t-tyabe/Data/DataforML/landpricedata.csv");

	static File shapedir = new File("/home/t-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	public static void main(String args[]) throws IOException{

		String type      = args[0];	
		String dir       = "/home/t-tyabe/Data/AnalysisResults/"+type+"Tokyo6/";
		String outdir    = "/home/t-tyabe/Data/segmentexp_"+type+"1/";

		File outputdir  = new File(outdir);  outputdir.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
		//		subjects.add("home_exit_diff");
		//		subjects.add("tsukin_time_diff");
		//		subjects.add("office_enter_diff");
		//		subjects.add("office_time_diff");
		subjects.add("office_exit_diff");
		//		subjects.add("kitaku_time_diff");
		//		subjects.add("home_return_diff");
		runMLData(subjects, dir, outdir, type);

	}

	public static void runMLData(ArrayList<String> subjects, String dir, String outdir, String type) throws IOException{

		HashMap<String, String>  popmap       = GetPop.getpopmap(popfile);
		HashMap<String, String>  buildingmap  = GetLanduse.getmeshbuilding(landusefile);
		HashMap<String, String>  farmmap      = GetLanduse.getmeshfarm(landusefile);
		HashMap<String, String>  sroadmap     = GetRoadData.getsmallroad(roadfile);
		HashMap<String, String>  broadmap     = GetRoadData.getfatroad(roadfile);
		HashMap<String, String>  allroadmap   = GetRoadData.getallroad(roadfile);
		HashMap<LonLat, String>  trainmap     = GetTrainData.getpopmap(trainfile);
		HashMap<LonLat, String>  pricemap     = GetLandPrice.getpricemap(pricefile);

		HashMap<String, HashMap<String,String>> homeexit   = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> officeent  = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> officeexit = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_he     = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_ox     = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_oe     = new HashMap<String, HashMap<String,String>>();

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
			HashSet<String> disasterdates = new HashSet<String>(); 

			int start;
			int end;
			if(type.equals("rain")){
				start = 4; end = 1;
			}
			else if(type.equals("eq")||type.equals("heats")){
				start = 3; end = 1;
			}
			else{
				continue;
			}

			for(int l=start; l>=end; l--){
				File typelevel = new File(dir+type+"_"+String.valueOf(l)+"/");
				String level = String.valueOf(l);
				for(File datetime :typelevel.listFiles()){
					String date = datetime.getName().split("_")[0];
					String time = datetime.getName().split("_")[1];
					if(!disasterdates.contains(date)){
						disasterdates.add(date);
						for(File f : datetime.listFiles()){
							if(f.toString().contains(subject)){
								System.out.println("#working on " + f.toString());
								getAttributes(f,new File(outfile),level,date,time,
										popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
										homeexit, officeent, officeexit, dis_he, dis_oe, dis_ox, subject, id_dates);
							}}}}
			}
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
			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap,
			HashMap<String, HashMap<String,String>> homeexit, HashMap<String, HashMap<String,String>>officeexit, 
			HashMap<String, HashMap<String,String>> dis_he, HashMap<String, HashMap<String,String>>dis_oe, 
			HashMap<String, HashMap<String,String>> officeent, HashMap<String, HashMap<String,String>>dis_ox, 
			//			HashMap<String, HashMap<String,String>> motifmap, 
			String subject, HashMap<String,ArrayList<String>> id_date) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			/*String id = null;*/ String diff = null; String dis = null; String normaltime = null; String disdaytime = null;
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; Double sigma = 0d; //String norlogs = null; String dislogs = null;

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				/*id = tokens[0];*/ diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				disdaytime = tokens[11]; normaltime = tokens[12]; 
				sigma = Double.parseDouble(tokens[13]);
				//				norlogs = tokens[14]; dislogs = tokens[15];
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				/*id = tokens[0];*/ diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				disdaytime = tokens[11]; normaltime = tokens[12]; sigma = Double.parseDouble(tokens[13]);
				//				norlogs = tokens[14]; dislogs = tokens[15];
				dis = String.valueOf(homep.distance(officep)/100000);
			}

			Double saigaitime  = Double.parseDouble(time);
			Double toujitutime = Double.parseDouble(disdaytime);

			if(saigaitime<toujitutime){

				bw.write(diff+","+level+","+time+","+normaltime+","+sigma+","+disdaytime+","+dis+","
						/*
						 * +homeexit.get(id).get(date+time+level)+","+officeent.get(id).get(date+time+level)+",");
						 */
						);

				String nowcode = getCode(nowp.getLon(),nowp.getLat());
				String homecode = getCode(homep.getLon(),homep.getLat());
				String offcode  = getCode(officep.getLon(),officep.getLat());

				if(!nowcode.equals("null")){
					bw.write(nowcode+",");
				}
				if(!homecode.equals("null")){
					bw.write(homecode+",");
				}
				if(!offcode.equals("null")){
					bw.write(offcode+",");
				}
				bw.newLine();
				//					id_date.get(id).add(date);	
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

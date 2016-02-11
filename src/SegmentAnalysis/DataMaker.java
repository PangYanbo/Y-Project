package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;
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

		ArrayList<String> types      = new ArrayList<String>();
		//		types.add("rain");
		types.add("rain");
		//		types.add("heats");

		for(String type : types){
			String dir       = "/home/t-tyabe/Data/"+type+"Tokyoonlylevel4/";
			String outdir    = "/home/t-tyabe/Data/segmentexp_"+type+"_onlylevel4/";

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
	}

	public static void clean(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;

		HashMap<String, HashMap<Integer,String>> res = new HashMap<String, HashMap<Integer,String>>();

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String ymd = tokens[2];
			Integer level = Integer.valueOf(tokens[3]);
			if(res.containsKey(id)){
				res.get(id).put(level, ymd);
			}
			else{
				HashMap<Integer,String> temp = new HashMap<Integer,String>();
				temp.put(level, ymd);
				res.put(id, temp);
			}
		}
		br.close();

		HashMap<String, HashMap<String,Integer>> res2 = new HashMap<String, HashMap<String,Integer>>();

		for(String id : res.keySet()){
			List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(res.get(id).entrySet());
			Collections.sort(entries, new Comparator<Entry<Integer, String>>() {
				//î‰ärä÷êî
				@Override
				public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
					return o1.getKey().compareTo(o2.getKey());  
				}
			});

			for (Entry<Integer, String> e : entries) {
				if(res2.containsKey(id)){
					if(!res2.containsValue(e.getValue())){
						res2.get(id).put(e.getValue(),e.getKey());
					}
				}
				else{
					HashMap<String,Integer> temp = new HashMap<String,Integer>();
					temp.put(e.getValue(),e.getKey());
					res2.put(id, temp);
				}
			}
		}

		BufferedReader br2 = new BufferedReader(new FileReader(in));
		String line2 = null;
		while((line2=br2.readLine())!=null){
			String[] tokens2 = line2.split(",");
			String id = tokens2[0];
			String ymd2 = tokens2[2];
			Integer level2 = Integer.valueOf(tokens2[3]);
			if(res2.get(id).get(ymd2)==level2){
				bw.write(line2);
				bw.newLine();
			}
		}
		br2.close();
		bw.close();
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

			String outfile   = outdir+subject+"_"+type+".csv"; 

			int start;
			int end;
			if(type.equals("rain")){
				start = 4; end = 1;
			}
			else if(type.equals("emg1")||type.equals("heats")){
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
					Double t = Double.parseDouble(time);
					if((17<=t)&&(t<=22)){
						for(File f : datetime.listFiles()){
							if(f.toString().contains(subject)){
								System.out.println("#working on " + f.toString());
								getAttributes(f,new File(outfile),level,date,time,
										popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
										homeexit, officeent, officeexit, dis_he, dis_oe, dis_ox, subject);
							}}}}
			}

			File out1 = new File(outdir+subject+"_"+type+".csv");
			File out2 = new File(outdir+subject+"_"+type+"_clean.csv");
			clean(out1,out2);

			File out3 = new File(outdir+subject+"_"+type+"_final.csv");
			removeOverlap(out2,out3);

		}
	}

	public static void removeOverlap(File in, File out) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;

		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String ymd = tokens[2];
			if(map.containsKey(id)){
				if(!map.get(id).contains(ymd)){
					bw.write(line);
					bw.newLine();
					map.get(id).add(ymd);
				}
			}
			else{
				bw.write(line);
				bw.newLine();
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(ymd);
				map.put(id,temp);
			}
		}
		br.close();
		bw.close();
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
			String subject) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String id = null; String diff = null; String dis = null; String normaltime = null; String disdaytime = null;
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; Double sigma = 0d; String norlogs = null; String dislogs = null;

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				disdaytime = tokens[11]; normaltime = tokens[12]; 
				sigma = Double.parseDouble(tokens[13]);
				norlogs = tokens[14]; dislogs = tokens[15];
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				disdaytime = tokens[11]; normaltime = tokens[12]; sigma = Double.parseDouble(tokens[13]);
				norlogs = tokens[14]; dislogs = tokens[15];
				dis = String.valueOf(homep.distance(officep)/100000);
			}

			Double saigaitime  = Double.parseDouble(time);
			Double toujitutime = Double.parseDouble(disdaytime);

			//			if(saigaitime<toujitutime){

			bw.write(id+","+diff+","+date+","+level+","+time+","+normaltime+","+sigma+","+disdaytime+","+dis+","

						+ popmap.get(new Mesh(3, nowp.getLon(),nowp.getLat()).getCode()) + ","
						+ popmap.get(new Mesh(3, homep.getLon(),homep.getLat()).getCode()) + ","
						+ popmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ buildingmap.get(new Mesh(3, nowp.getLon(),nowp.getLat()).getCode()) + ","
						+ buildingmap.get(new Mesh(3, homep.getLon(),homep.getLat()).getCode()) + ","
						+ buildingmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ farmmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ farmmap.get(new Mesh(3, homep.getLon(),homep.getLat()).getCode()) + ","
						+ farmmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ allroadmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ allroadmap.get(new Mesh(3, homep.getLon(),homep.getLat()).getCode()) + ","
						+ allroadmap.get(new Mesh(3, officep.getLon(),officep.getLat()).getCode()) + ","
						+ GetTrainData.getpopofnearestStation(trainmap, nowp) + ","
						+ GetTrainData.getpopofnearestStation(trainmap, homep) + ","
						+ GetTrainData.getpopofnearestStation(trainmap, officep) + ","
						+ GetLandPrice.getnearestprice(pricemap, nowp) + ","
						+ GetLandPrice.getnearestprice(pricemap, homep) + ","
						+ GetLandPrice.getnearestprice(pricemap, officep) + ","
						+ norlogs + "," + dislogs + ","
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
			//			}
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

}

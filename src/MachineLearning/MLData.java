package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class MLData {

	public static final String type      = "rain";
	public static final String dir       = "/home/c-tyabe/Data/"+type+"Tokyo3/";
	public static final String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"6/";
	public static final String outdir2   = outdir+"forML/";
	public static final String outdir3   = outdir+"forML/calc/";
	public static final double min       = 0.5;

	public static final File popfile     = new File("/home/c-tyabe/Data/DataforML/mesh_daytimepop.csv");
	public static final File landusefile = new File("/home/c-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/c-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/c-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/c-tyabe/Data/DataforML/landpricedata.csv");

	public static void main(String args[]) throws IOException{

		File outputdir = new File(outdir); outputdir.mkdir();
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
		runMLData(subjects);

	}

	public static void runMLData(ArrayList<String> subjects) throws IOException{

		HashMap<String, String>  popmap       = GetPop.getpopmap(popfile);
		HashMap<String, String>  buildingmap  = GetLanduse.getmeshbuilding(landusefile);
		HashMap<String, String>  farmmap      = GetLanduse.getmeshfarm(landusefile);
		HashMap<String, String>  sroadmap     = GetRoadData.getsmallroad(roadfile);
		HashMap<String, String>  broadmap     = GetRoadData.getfatroad(roadfile);
		HashMap<String, String>  allroadmap   = GetRoadData.getallroad(roadfile);
		HashMap<LonLat, String>  trainmap     = GetTrainData.getpopmap(trainfile);
		HashMap<LonLat, String>  pricemap     = GetLandPrice.getpricemap(pricefile);

		HashMap<String, HashMap<String,String>> homeexit   = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> officeexit = new HashMap<String, HashMap<String,String>>();
		HashMap<String, HashMap<String,String>> dis_he     = new HashMap<String, HashMap<String,String>>();
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
					else if(f.toString().contains("office_exit_diff")){
						getActionMap(f,level,date+time,officeexit);
						getSaigaiMap(f,level,date+time,dis_oe);
					}
				}}}

		//--- test if working properly
		System.out.println("#actionmap for home exit : " + homeexit.size());
		System.out.println("#actionmap for ofce exit : " + officeexit.size());
		System.out.println("#saigai timemap for home exit : " + dis_he.size());
		System.out.println("#saigai timemap for ofce exit : " + dis_oe.size());
		
		for(String subject : subjects){
			String outfile   = outdir+subject+"_ML.csv"; 

			for(File typelevel : new File(dir).listFiles()){
				String level = typelevel.getName().split("_")[1];
				for(File datetime :typelevel.listFiles()){
					String date = datetime.getName().split("_")[0];
					String time = datetime.getName().split("_")[1];
					for(File f : datetime.listFiles()){
						if(f.toString().contains(subject)){
							System.out.println("#working on " + f.toString());
							getAttributes(f,new File(outfile),level,date,time,
									popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
									homeexit, officeexit, dis_he, dis_oe, subject);
						}}}}

			String newoutfile   = outdir+subject+"_ML_cleaned.csv"; 
			MLDataCleaner.DataClean(new File(outfile), new File(newoutfile)); //delete 0s and Es

			String plusminus_normal  = outdir2+subject+"_ML_plusminus_normal.csv";
			MLDataCleaner.ytoone(new File(newoutfile), new File(plusminus_normal));

			String multiplelines = outdir+subject+"_ML_lineforeach.csv";
			MLDataModifier.Modify(new File(newoutfile), new File(multiplelines), min);

			String plusminus_multiplelines = outdir3+subject+"_ML_plusminus_lineforeach.csv";
			MLDataCleaner.ytoone(new File(multiplelines), new File(plusminus_multiplelines));
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
			String subject) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		
		int totallines = 0;
		int checkline1 = 0;
		int checkline2 = 0;
		int checkline3 = 0;
		int checkline4 = 0;
		
		while((line=br.readLine())!=null){
			String id = null; String diff = null; String dis = null; String normaltime = null; //String distime = null;
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; 

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				normaltime = tokens[12]; 
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				normaltime = tokens[12]; dis = String.valueOf(homep.distance(officep)/100000);
			}

			if(Math.abs(Double.parseDouble(diff))>min){

				ArrayList<String> list = new ArrayList<String>();
				for(String l  : GetLevel.getLevel(level).split(",")){ //level (0,0,0,0 etc.)
					list.add(l);
				}
				for(String t  : Bins.timerange(time).split(",")){ //time of disaster 
					list.add(t);
				}
				for(String df : Bins.getline4Diffs(subject, normaltime).split(",")){
					list.add(df);
				}
				for(String p  : GetPop.getpop(popmap, nowp, homep, officep).split(",")){ //pop data
					list.add(p);
				}
				for(String la : GetLanduse.getlanduse(buildingmap, farmmap, nowp, homep, officep).split(",")){
					list.add(la);
				}
				for(String r  : GetRoadData.getroaddata(sroadmap, broadmap, allroadmap, nowp, homep, officep).split(",")){
					list.add(r);
				}
				for(String st : GetTrainData.getstationpop(trainmap, nowp, homep, officep).split(",")){
					list.add(st);
				}
				for(String lp : GetLandPrice.getlandprice(pricemap, nowp, homep, officep).split(",")){
					list.add(lp);
				}
				for(String ds : Bins.getlineDistance(dis).split(",")){
					list.add(ds);
				}		

				if(!subject.equals("home_exit_diff")){
					if(homeexit.containsKey(id)){
						if(homeexit.get(id).containsKey(date+time+level)){
							for(String he : Bins.h_e_line(homeexit.get(id).get(date+time+level)).split(",")){
								list.add(he);
								checkline1++;
							}}
						else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
					else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
				}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

				if(!subject.equals("home_exit_diff")){
					if(dis_he.containsKey(id)){
						if(dis_he.get(id).containsKey(date+time+level)){
							for(String he2 : Bins.getline4Diffs("home_exit_diff",dis_he.get(id).get(date+time+level)).split(",")){
								list.add(he2);
								checkline2++;
							}}						
						else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
					else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
				}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

				if((subject.equals("office_time_diff"))||(subject.equals("home_return_diff"))){
					if(officeexit.containsKey(id)){
						if(officeexit.get(id).containsKey(date+time+level)){
							for(String oe : Bins.h_e_line(officeexit.get(id).get(date+time+level)).split(",")){
								list.add(oe);
								checkline3++;
							}}
						else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
					else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
				}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

				if((subject.equals("office_time_diff"))||(subject.equals("home_return_diff"))){
					if(dis_oe.containsKey(id)){
						if(dis_oe.get(id).containsKey(date+time+level)){
							for(String oe2 : Bins.getline4Diffs("office_exit_diff",dis_oe.get(id).get(date+time+level)).split(",")){
								list.add(oe2);
								checkline4++;
							}}
						else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
					else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
				}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

				bw.write(diff);
				for(int i = 1; i<=list.size(); i++){
					bw.write(" "+i+":"+list.get(i-1));
				}
				bw.newLine();
			}
			
			totallines++;
		}
		
		System.out.println("#check lines " + totallines + " " + checkline1 + " " + checkline2 + " " + checkline3 + " " + checkline4);
		
		br.close();
		bw.close();
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

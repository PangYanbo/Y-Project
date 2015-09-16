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

public class MLDataforMotif {

	public static final String type      = "rain";
	public static final String dir       = "/home/c-tyabe/Data/"+type+"Tokyo3/";
	public static final String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"4/";
	public static final double min       = 0;

	public static final File popfile     = new File("/home/c-tyabe/Data/DataforML/mesh_daytimepop.csv");
	public static final File landusefile = new File("/home/c-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/c-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/c-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/c-tyabe/Data/DataforML/landpricedata.csv");

	public static void main(String args[]) throws IOException{

		File outputdir = new File(outdir); outputdir.mkdir();
		String outdir2 = outdir+"forML/"; File outputdir2 = new File(outdir2); outputdir2.mkdir();
		
		String subject = "id_day_motifs";
		String outfile   = outdir+subject+"_ML.csv"; 

		HashMap<String, String>  popmap       = GetPop.getpopmap(popfile);
		HashMap<String, String>  buildingmap  = GetLanduse.getmeshbuilding(landusefile);
		HashMap<String, String>  farmmap      = GetLanduse.getmeshfarm(landusefile);
		HashMap<String, String>  sroadmap     = GetRoadData.getsmallroad(roadfile);
		HashMap<String, String>  broadmap     = GetRoadData.getfatroad(roadfile);
		HashMap<String, String>  allroadmap   = GetRoadData.getallroad(roadfile);
		HashMap<LonLat, String>  trainmap     = GetTrainData.getpopmap(trainfile);
		HashMap<LonLat, String>  pricemap     = GetLandPrice.getpricemap(pricefile);

		for(File typelevel : new File(dir).listFiles()){
			String level = typelevel.getName().split("_")[1];
			for(File datetime :typelevel.listFiles()){
				String time = datetime.getName().split("_")[1];
				for(File f : datetime.listFiles()){
					if(f.toString().contains(subject)){
						System.out.println("#working on " + f.toString());
						getAttributes(f,new File(outfile),level,time,popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap);
					}}}}

		String newoutfile   = outdir+subject+"_ML_cleaned.csv"; 
		MLDataCleaner.DataClean(new File(outfile), new File(newoutfile)); //delete 0s and Es

		String plusminus_normal  = outdir2+subject+"_ML_plusminus_normal.csv";
		MLDataCleaner.ytoone(new File(newoutfile), new File(plusminus_normal));

		String multiplelines = outdir+subject+"_ML_lineforeach.csv";
		MLDataModifier.Modify(new File(newoutfile), new File(multiplelines));

		String plusminus_multiplelines = outdir2+subject+"_ML_plusminus_lineforeach.csv";
		MLDataCleaner.ytoone(new File(multiplelines), new File(plusminus_multiplelines));

	}


	public static void getAttributes(File in, File out, String level, String time,
			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		while((line=br.readLine())!=null){
			String diff = null; String dis = null; String normaltime = null; String distime = null;
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; 

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				normaltime = tokens[12]; distime = tokens[11];
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				distime = tokens[11]; normaltime = tokens[12];
				dis = String.valueOf(homep.distance(officep)/100000);
			}

			if(Math.abs(Double.parseDouble(diff))>min){
				
				ArrayList<String> list = new ArrayList<String>();
				for(String l  : GetLevel.getLevel(level).split(",")){ //level (0,0,0,0 etc.)
					list.add(l);
				}
				for(String t  : timerange(time).split(",")){ //time of disaster 
					list.add(t);
				}
				for(String nt : timerange(normaltime).split(",")){ //time of action (normal)
					list.add(nt);
				}
				for(String dt : timerange(distime).split(",")){ //time of action (disaster)
					list.add(dt);
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
				for(String ds : getlineDistance(dis).split(",")){
					list.add(ds);
				}

				bw.write(diff);
				for(int i = 1; i<=list.size(); i++){
					bw.write(" "+i+":"+list.get(i-1));
				}
				bw.newLine();
			}
		}
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

	public static String timerange(String time){
		if(time==null){
			return "0,0,0,0,0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<6){return "1,0,0,0,0";}
			else if ((timerange>=6)&&(timerange<10)){return "0,1,0,0,0";}
			else if ((timerange>=10)&&(timerange<16)){return "0,0,1,0,0";}
			else if ((timerange>=16)&&(timerange<20)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}

	public static String getlineDistance(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.01){return "1,0,0,0,0";}
			else if ((poprange>=0.01)&&(poprange<0.05)){return "0,1,0,0,0";}
			else if ((poprange>=0.05)&&(poprange<0.25)){return "0,0,1,0,0";}
			else if ((poprange>=0.25)&&(poprange<0.40)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	public static HashMap<String, HashMap<String,String>> getMotifMap(File in) throws IOException{
		HashMap<String, HashMap<String,String>> res = new HashMap<String, HashMap<String,String>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
//		String line = null;
//		while((line=br.readLine())!=null){
//			
//		}
		br.close();
		return res;
	}
	
	/*
	 * wait a minute... how are we going to set the ñ⁄ìIïœêî!?
	 * @ ask Tsubochan
	 * 
	 */
}

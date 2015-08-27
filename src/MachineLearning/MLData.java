package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class MLData {

	public static final String type      = "rain";
	public static final String subject   = "home_exit_diff";
	public static final String dir       = "/home/c-tyabe/Data/"+type+"Tokyo/";
	public static final String outfile   = "/home/c-tyabe/Data/"+type+"Tokyo/"+subject+"_ML.csv"; 

//	public static final File popfile     = new File("/home/c-tyabe/Data/DataforML/popdata.csv");
	public static final File landusefile = new File("/home/c-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/c-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/c-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/c-tyabe/Data/DataforML/landpricedata.csv");

	public static void main(String args[]) throws IOException{

		HashMap<String, String>  popmap       = GetPop.getpopmap(landusefile);
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
					}}}}}


	public static void getAttributes(File in, File out, String level, String time,
			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		while((line=br.readLine())!=null){
			String diff = null; String dis = null; 
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; 
			
			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				dis = String.valueOf(homep.distance(officep)/100000);
			}

			String res = "1:"+diff+" 2:"+level+" 3:"+timerange(time)
					+GetPop.getpop(popmap,nowp,homep,officep)
					+GetLanduse.getlanduse(buildingmap, farmmap, nowp, homep, officep)
					+GetRoadData.getroaddata(sroadmap, broadmap, allroadmap, nowp, homep, officep)
					+GetTrainData.getstationpop(trainmap, nowp, homep, officep)
					+GetLandPrice.getlandprice(pricemap, nowp, homep, officep)
					+" 28:"+dis;

			bw.write(res);
			bw.newLine();

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
		Double timerange = Double.parseDouble(time);
		if(timerange<6){return "1";}
		else if ((timerange>=6)&&(timerange<10)){return "2";}
		else if ((timerange>=10)&&(timerange<16)){return "3";}
		else if ((timerange>=16)&&(timerange<20)){return "4";}
		else{return "5";}
	}
}

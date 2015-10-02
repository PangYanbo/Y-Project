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
	public static final String dir       = "/home/c-tyabe/Data/"+type+"Tokyo4/";
	public static final String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"7/";
	public static final String outdir2   = outdir+"forML/";
	public static final String outdir3   = outdir+"forML/calc/";

	public static final File popfile     = new File("/home/c-tyabe/Data/DataforML/mesh_daytimepop.csv");
	public static final File landusefile = new File("/home/c-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/c-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/c-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/c-tyabe/Data/DataforML/landpricedata.csv");

	public static void main(String args[]) throws IOException{

		File outputdir  = new File(outdir);  outputdir.mkdir();
		File outputdir2 = new File(outdir2); outputdir2.mkdir();
		File outputdir3 = new File(outdir3); outputdir3.mkdir();

		String subject = "id_day_motifs";
		runMLDataMotif(subject);
	}

	public static void runMLDataMotif(String subject) throws IOException{

		HashMap<String, String>  popmap       = GetPop.getpopmap(popfile);
		HashMap<String, String>  buildingmap  = GetLanduse.getmeshbuilding(landusefile);
		HashMap<String, String>  farmmap      = GetLanduse.getmeshfarm(landusefile);
		HashMap<String, String>  sroadmap     = GetRoadData.getsmallroad(roadfile);
		HashMap<String, String>  broadmap     = GetRoadData.getfatroad(roadfile);
		HashMap<String, String>  allroadmap   = GetRoadData.getallroad(roadfile);
		HashMap<LonLat, String>  trainmap     = GetTrainData.getpopmap(trainfile);
		HashMap<LonLat, String>  pricemap     = GetLandPrice.getpricemap(pricefile);

		String outfile   = outdir+subject+"_ML.csv"; 

		for(File typelevel : new File(dir).listFiles()){
			String level = typelevel.getName().split("_")[1];
			for(File datetime :typelevel.listFiles()){
				String date = datetime.getName().split("_")[0];
				String time = datetime.getName().split("_")[1];
				for(File f : datetime.listFiles()){
					if(f.toString().contains(subject)){
						getAttributesMotif(f,new File(outfile),level,date,time,
								popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
								subject);
					}}}

			String newoutfile   = outdir+subject+"_ML_cleaned.csv"; 
			MLDataCleaner.DataClean(new File(outfile), new File(newoutfile)); //delete 0s and Es

			String plusminus_normal  = outdir2+subject+"_ML_plusminus_normal.csv";
			MLDataCleaner.ytoone(new File(newoutfile), new File(plusminus_normal));

			String multiplelines = outdir+subject+"_ML_lineforeach.csv";
			MLDataModifier.Modify(new File(newoutfile), new File(multiplelines));

			String plusminus_multiplelines = outdir3+subject+"_ML_plusminus_lineforeach.csv";
			MLDataCleaner.ytoone(new File(multiplelines), new File(plusminus_multiplelines));
		}
	}


	public static HashMap<String, HashMap<String,String>> MotifMap(File in, String date, HashMap<String, HashMap<String,String>> res) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String day = tokens[2];
			if(!day.equals("99")){
				date = date.substring(0,6)+day;
			}
			String motif = tokens[3];

			if(res.containsKey(id)){
				res.get(id).put(date, motif);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(date, motif);
				res.put(id, temp);
			}
		}
		br.close();
		return res;
	}

	public static void getAttributesMotif(File in, File out, String level, String date, String time,
			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap, String subject) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;

		while((line=br.readLine())!=null){
			String id = null; String diff = null; String dis = null; String normaltime = null;
			LonLat nowp = null; LonLat homep = null; LonLat officep = null; Double sigma = 0d;

			String[] tokens = line.split(",");
			if(tokens[5].contains("(")){ // output version 1 
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5].replace("(","")),Double.parseDouble(tokens[6].replace(")","")));
				homep = new LonLat(Double.parseDouble(tokens[7].replace("(","")),Double.parseDouble(tokens[8].replace(")","")));
				officep = new LonLat(Double.parseDouble(tokens[9].replace("(","")),Double.parseDouble(tokens[10].replace(")","")));
				normaltime = tokens[12]; 
				sigma = Double.parseDouble(tokens[13]);
				dis = String.valueOf(homep.distance(officep)/100000);
			}
			else{ // output version 2
				id = tokens[0]; diff = tokens[1]; dis = tokens[4];
				nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
				normaltime = tokens[12]; sigma = Double.parseDouble(tokens[13]);
				dis = String.valueOf(homep.distance(officep)/100000);
			}

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

			//put daily motifs in this area				

			bw.write(diff);
			for(int i = 1; i<=list.size(); i++){
				bw.write(" "+i+":"+list.get(i-1));
			}
			bw.write(" #"+diff);
			bw.newLine();

		}

		br.close();
		bw.close();
	}

}

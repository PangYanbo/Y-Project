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

						HashMap<String, HashMap<String,String>> motifmap = MotifMap(f);
						HashMap<String, ArrayList<LonLat>>      locmap   = LocMap(f);

						getAttributesMotif(motifmap,locmap,new File(outfile),level,date,time,
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

	public static HashMap<String, HashMap<String,String>> MotifMap(File in) throws IOException{
		HashMap<String, HashMap<String,String>> res = new HashMap<String,HashMap<String,String>>();		
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String day = tokens[1];
			String motif = tokens[2];

			if(res.containsKey(id)){
				res.get(id).put(day, motif);
			}
			else{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(day, motif);
				res.put(id, temp);
			}
		}
		br.close();
		return res;
	}

	public static HashMap<String, ArrayList<LonLat>> LocMap(File in) throws IOException{
		HashMap<String, ArrayList<LonLat>> res = new HashMap<String, ArrayList<LonLat>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			LonLat now  = new LonLat(Double.parseDouble(tokens[3]),Double.parseDouble(tokens[4]));
			LonLat home = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
			LonLat off  = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));

			if(res.containsKey(id)){
				continue;
			}
			else{
				ArrayList<LonLat> temp = new ArrayList<LonLat>();
				temp.add(now);
				temp.add(home);
				temp.add(off);
				res.put(id, temp);
			}
		}
		br.close();
		return res;
	}

	public static void getAttributesMotif(HashMap<String, HashMap<String,String>> motifmap, HashMap<String, ArrayList<LonLat>> locmap,
			File out, String level, String date, String time,
			HashMap<String, String> popmap, HashMap<String, String> buildingmap, HashMap<String, String> farmmap, 
			HashMap<String, String> sroadmap, HashMap<String, String> broadmap, HashMap<String, String> allroadmap,
			HashMap<LonLat, String> trainmap, HashMap<LonLat, String> pricemap, String subject) throws IOException{

		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));

		for(String id : motifmap.keySet()){
			if(motifmap.get(id).containsKey("99")){
				String dis = null; String normaltime = null;
				LonLat nowp = null; LonLat homep = null; LonLat officep = null;

				nowp    = locmap.get(id).get(0);
				homep   = locmap.get(id).get(1);
				officep = locmap.get(id).get(2);
				dis     = String.valueOf(homep.distance(officep)/100000);

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
				for(String m : motiflist(motifmap.get(id))){
					list.add(m);
				}

				bw.write(motifmap.get(id).get("99"));
				for(int i = 1; i<=list.size(); i++){
					bw.write(" "+i+":"+list.get(i-1));
				}
				bw.write(" #"+motifmap.get(id).get("99"));
				bw.newLine();

			}
		}
		bw.close();
	}

	public static ArrayList<String> motiflist(HashMap<String,String> map){
		ArrayList<String> res = new ArrayList<String>();
		for(int i=1; i<=17; i++){
			res.add("0");
		}

		for(String day: map.keySet()){
			if(!day.equals("99")){
				if(!map.get(day).equals("0")){
					Integer motif = Integer.valueOf(map.get(day));
					if(res.get(motif-1).equals("0")){
						res.get(motif-1).replace("0","1");
					}
				}
			}
		}
		return res;
	}

}

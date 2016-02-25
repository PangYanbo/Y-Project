package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

//ML Data for normal-irregular decision

public class MLData2 {

	public static final double k         = 2;

	public static final File popfile     = new File("/home/t-tyabe/Data/DataforML/mesh_daytimepop.csv");
	public static final File landusefile = new File("/home/t-tyabe/Data/DataforML/landusedata.csv");
	public static final File roadfile    = new File("/home/t-tyabe/Data/DataforML/roadnetworkdata.csv");
	public static final File trainfile   = new File("/home/t-tyabe/Data/DataforML/railnodedata.csv");
	public static final File pricefile   = new File("/home/t-tyabe/Data/DataforML/landpricedata.csv");

	static File shapedir = new File("/home/t-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	public static void main(String args[]) throws IOException{

		String type      = "rain";	
		String dir       = "/home/t-tyabe/Data/"+type+"Tokyo7/";
		String outdir    = "/home/t-tyabe/Data/MLResults_"+type+"14/";
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

	public static void runMLData(ArrayList<String> subjects, String dir, String outdir, String outdir2, String outdir3, String outdir4, String type) throws IOException{

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
									popmap,buildingmap,farmmap,sroadmap,broadmap,allroadmap,trainmap,pricemap,
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
						for(String t  : Bins.timerange(time).split(",")){ //time of disaster 5-9
							list.add(t);
						}
						for(String df : Bins.getline4Diffs(subject, normaltime).split(",")){ //10-14
							list.add(df);
						}
						for(String si : Bins.sigmaline(k*sigma).split(",")){ //15-19
							list.add(si);
						}
						for(String p  : GetPop.getpop(popmap, nowp, homep, officep).split(",")){ //pop data 20-24,25-29,30-34
							list.add(p);
						}
						for(String la : GetLanduse.getlanduse(buildingmap, farmmap, nowp, homep, officep).split(",")){ //35-39,40-44,45-49, 50-54,55-59,60-64
							list.add(la);
						}
						for(String r  : GetRoadData.getroaddata(sroadmap, broadmap, allroadmap, nowp, homep, officep).split(",")){ //65-,70-,75-, 80-,85-,90-, 95-,100-,105-
							list.add(r);
						}
						for(String st : GetTrainData.getstationpop(trainmap, nowp, homep, officep).split(",")){ //110-,115-,120-
							list.add(st);
						}
						for(String lp : GetLandPrice.getlandprice(pricemap, nowp, homep, officep).split(",")){ //125-,130-,135-
							list.add(lp);
						}
						for(String ds : Bins.getlineDistance(dis).split(",")){ //140-
							list.add(ds);
						}	

						if((subject.equals("tsukin_time_diff"))||(subject.equals("office_time_diff"))||(subject.equals("kitaku_time_diff"))){
							disdaytime = "30";
						}
						
						if(isEarly(time,disdaytime)==true){
							if(!subject.equals("home_exit_diff")){
								if(homeexit.containsKey(id)){
									if(homeexit.get(id).containsKey(date+time+level)){
										for(String he : Bins.h_e_line(homeexit.get(id).get(date+time+level)).split(",")){
											list.add(he);
											//								checkline1++;
										}}
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

							if(!subject.equals("home_exit_diff")){
								if(dis_he.containsKey(id)){
									if(dis_he.get(id).containsKey(date+time+level)){
										for(String he2 : Bins.getline4Diffs("home_exit_diff",dis_he.get(id).get(date+time+level)).split(",")){
											list.add(he2);
											//								checkline2++;
										}}						
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

							if(!(subject.equals("home_exit_diff"))||(subject.equals("tsukin_time_diff"))||(subject.equals("office_enter_diff"))){
								if(officeent.containsKey(id)){
									if(officeent.get(id).containsKey(date+time+level)){
										for(String oe : Bins.h_e_line(officeent.get(id).get(date+time+level)).split(",")){
											list.add(oe);
											//								checkline3++;
										}}
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

							if(!(subject.equals("home_exit_diff"))||(subject.equals("tsukin_time_diff"))||(subject.equals("office_enter_diff"))){
								if(dis_oe.containsKey(id)){
									if(dis_oe.get(id).containsKey(date+time+level)){
										for(String oe2 : Bins.getline4Diffs("office_enter_diff",dis_oe.get(id).get(date+time+level)).split(",")){
											list.add(oe2);
											//								checkline4++;
										}}
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

							if((subject.equals("kitaku_time_diff"))||(subject.equals("home_return_diff"))){
								if(officeexit.containsKey(id)){
									if(officeexit.get(id).containsKey(date+time+level)){
										for(String ox : Bins.h_e_line(officeexit.get(id).get(date+time+level)).split(",")){
											list.add(ox);
											//								checkline3++;
										}}
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

							if((subject.equals("kitaku_time_diff"))||(subject.equals("home_return_diff"))){
								if(dis_ox.containsKey(id)){
									if(dis_ox.get(id).containsKey(date+time+level)){
										for(String ox2 : Bins.getline4Diffs("office_exit_diff",dis_ox.get(id).get(date+time+level)).split(",")){
											list.add(ox2);
											//							checkline4++;
										}}
									else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
							}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}
						else{
							for(int i = 1; i <=30 ; i++){list.add("0");}
						}

						bw.write(diff);

						for(int i = 1; i<=list.size(); i++){
							bw.write(" "+i+":"+list.get(i-1));
						}

						HashSet<String> codes = new HashSet<String>();
						ArrayList<String> codesinorder = new ArrayList<String>();
						String nowcode = getCode(nowp.getLon(),nowp.getLat());
						String homecode = getCode(homep.getLon(),homep.getLat());
						String offcode  = getCode(officep.getLon(),officep.getLat());

						if(!nowcode.equals("null")){
							codes.add(nowcode);
						}
						if(!homecode.equals("null")){
							codes.add(homecode);
						}
						if(!offcode.equals("null")){
							codes.add(offcode);
						}
						if(!codes.isEmpty()){
							for(String code : codes){
								codesinorder.add(code);
							}
							Collections.sort(codesinorder);
							for(String c : codesinorder){
								bw.write(" "+c+":1");
							}
						}

						String bilinearline = BilinearFeatures.bilinearline(level,time,dis,homep,officep,popmap,pricemap);
						bw.write(bilinearline);

						bw.write(" #"+norlogs+"_"+dislogs+"A"+String.valueOf(sigma));
						bw.newLine();
						id_date.get(id).add(date);
					}
				}
				else{
					ArrayList<String> list = new ArrayList<String>();
					for(String l  : GetLevel.getLevel(level).split(",")){ //level (0,0,0,0 etc.) 1-4
						list.add(l);
					}
					for(String t  : Bins.timerange(time).split(",")){ //time of disaster 5-9
						list.add(t);
					}
					for(String df : Bins.getline4Diffs(subject, normaltime).split(",")){ //10-14
						list.add(df);
					}
					for(String si : Bins.sigmaline(k*sigma).split(",")){ //15-19
						list.add(si);
					}
					for(String p  : GetPop.getpop(popmap, nowp, homep, officep).split(",")){ //pop data 20-24,25-29,30-34
						list.add(p);
					}
					for(String la : GetLanduse.getlanduse(buildingmap, farmmap, nowp, homep, officep).split(",")){ //35-39,40-44,45-49, 50-54,55-59,60-64
						list.add(la);
					}
					for(String r  : GetRoadData.getroaddata(sroadmap, broadmap, allroadmap, nowp, homep, officep).split(",")){ //65-,70-,75-, 80-,85-,90-, 95-,100-,105-
						list.add(r);
					}
					for(String st : GetTrainData.getstationpop(trainmap, nowp, homep, officep).split(",")){ //110-,115-,120-
						list.add(st);
					}
					for(String lp : GetLandPrice.getlandprice(pricemap, nowp, homep, officep).split(",")){ //125-,130-,135-
						list.add(lp);
					}
					for(String ds : Bins.getlineDistance(dis).split(",")){ //140-
						list.add(ds);
					}	

					if((subject.equals("tsukin_time_diff"))||(subject.equals("office_time_diff"))||(subject.equals("kitaku_time_diff"))){
						disdaytime = "30";
					}
					
					if(isEarly(time,disdaytime)==true){
						if(!subject.equals("home_exit_diff")){
							if(homeexit.containsKey(id)){
								if(homeexit.get(id).containsKey(date+time+level)){
									for(String he : Bins.h_e_line(homeexit.get(id).get(date+time+level)).split(",")){
										list.add(he);
										//								checkline1++;
									}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

						if(!subject.equals("home_exit_diff")){
							if(dis_he.containsKey(id)){
								if(dis_he.get(id).containsKey(date+time+level)){
									for(String he2 : Bins.getline4Diffs("home_exit_diff",dis_he.get(id).get(date+time+level)).split(",")){
										list.add(he2);
										//								checkline2++;
									}}						
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

						if(!(subject.equals("home_exit_diff"))||(subject.equals("tsukin_time_diff"))||(subject.equals("office_enter_diff"))){
							if(officeent.containsKey(id)){
								if(officeent.get(id).containsKey(date+time+level)){
									for(String oe : Bins.h_e_line(officeent.get(id).get(date+time+level)).split(",")){
										list.add(oe);
										//								checkline3++;
									}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

						if(!(subject.equals("home_exit_diff"))||(subject.equals("tsukin_time_diff"))||(subject.equals("office_enter_diff"))){
							if(dis_oe.containsKey(id)){
								if(dis_oe.get(id).containsKey(date+time+level)){
									for(String oe2 : Bins.getline4Diffs("office_enter_diff",dis_oe.get(id).get(date+time+level)).split(",")){
										list.add(oe2);
										//								checkline4++;
									}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

						if((subject.equals("kitaku_time_diff"))||(subject.equals("home_return_diff"))){
							if(officeexit.containsKey(id)){
								if(officeexit.get(id).containsKey(date+time+level)){
									for(String ox : Bins.h_e_line(officeexit.get(id).get(date+time+level)).split(",")){
										list.add(ox);
										//								checkline3++;
									}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}

						if((subject.equals("kitaku_time_diff"))||(subject.equals("home_return_diff"))){
							if(dis_ox.containsKey(id)){
								if(dis_ox.get(id).containsKey(date+time+level)){
									for(String ox2 : Bins.getline4Diffs("office_exit_diff",dis_ox.get(id).get(date+time+level)).split(",")){
										list.add(ox2);
										//							checkline4++;
									}}
								else{ for(int i = 1; i <=5 ; i++){list.add("0");}}}
							else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
						}else{ for(int i = 1; i <=5 ; i++){list.add("0");}}
					}
					else{
						for(int i = 1; i <=30 ; i++){list.add("0");}
					}

					bw.write(diff);

					for(int i = 1; i<=list.size(); i++){
						bw.write(" "+i+":"+list.get(i-1));
					}

					HashSet<String> codes = new HashSet<String>();
					ArrayList<String> codesinorder = new ArrayList<String>();
					String nowcode = getCode(nowp.getLon(),nowp.getLat());
					String homecode = getCode(homep.getLon(),homep.getLat());
					String offcode  = getCode(officep.getLon(),officep.getLat());

					if(!nowcode.equals("null")){
						codes.add(nowcode);
					}
					if(!homecode.equals("null")){
						codes.add(homecode);
					}
					if(!offcode.equals("null")){
						codes.add(offcode);
					}
					if(!codes.isEmpty()){
						for(String code : codes){
							codesinorder.add(code);
						}
						Collections.sort(codesinorder);
						for(String c : codesinorder){
							bw.write(" "+c+":1");
						}
					}

					String bilinearline = BilinearFeatures.bilinearline(level,time,dis,homep,officep,popmap,pricemap);
					bw.write(bilinearline);

					bw.write(" #"+norlogs+"_"+dislogs+"A"+String.valueOf(sigma));
					bw.newLine();
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(date);
					id_date.put(id, temp);
				}
			}
		}

		//			}

		//		totallines++;
		//		}
		//		System.out.println("#lines which have cleared sigma*"+k+" restriction: "+sigmalines+" out of "+totallines);
		//		System.out.println("#check lines " + totallines + " " + checkline1 + " " + checkline2 + " " + checkline3 + " " + checkline4);

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

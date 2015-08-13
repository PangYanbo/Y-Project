package StayPointDetection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.STPoint;

public class OfficeSchoolDetection {

	/*
	 * param 
	 * args[0] : infile for all data
	 * args[1] : outfile for 'id \t homepoint'
	 * args[2] : file containing id-homelon-homelat
	 * 	
	 */
	
	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{

//		File in = new File(args[0]);
//		File result = new File (args[1]);
		File in = new File("c:/users/yabetaka/desktop/dataforexp.csv");

	}
	
	public static File getOfficeSchool(String inpath, String date, String type) throws NumberFormatException, ParseException, IOException{
		File in = new File(inpath);
		File result = new File ("/home/c-tyabe/Data/"+type+date+"/id_office_"+ date +".csv");
		HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> SPmap = StayPointGetter.getSPs(in,"08:00:00", "18:00:00", 5, 2000, 1000);

		HashMap<String, ArrayList<STPoint>> alldatamap = StayPointGetter.sortintoMapY(in);
		HashMap<String, ArrayList<STPoint>> targetmap = StayPointGetter.getTargetMap(alldatamap,"08:00:00","18:00:00");
		
		HashMap<String,Integer> numberofDays = new HashMap<String,Integer>();
		for(String id : targetmap.keySet()){
			int days = StayPointTools.NumberofWeekDays(targetmap.get(id));
			numberofDays.put(id, days);
		}

//		File id_home = new File (args[2]);
		File id_home = new File ("/home/c-tyabe/Data/id_home_"+date+".csv");

		HashMap<String,LonLat> idhome = StayPointTools.getHomeMap(id_home);

		HashMap<String,HashMap<LonLat,Integer>> id_SP_visitcount = ExcludeLowFrequentSPsbyNumberofPoints(SPmap,idhome,numberofDays,0.4);

		HashMap<String,LonLat> resmap = getOfficePoints(id_SP_visitcount,idhome,1000);
		writeOut(resmap, result);
		return result;
	}

	public static HashMap<String,HashMap<LonLat,Integer>> ExcludeLowFrequentSPsbyNumberofPoints(HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> map, HashMap<String,LonLat> idhome, HashMap<String, Integer> numberoflogs, double minrate){
		HashMap<String,HashMap<LonLat,Integer>> res = new HashMap<String,HashMap<LonLat,Integer>>();
		int counter = 0;
		for(String id : map.keySet()){
			HashMap<LonLat,Integer> tempmap = new HashMap<LonLat,Integer>();
			for(LonLat sp : map.get(id).keySet()){
				HashSet<String> temp = new HashSet<String>();
				for(STPoint stp : map.get(id).get(sp)){
					String date = (new SimpleDateFormat("yyyy-MM-dd")).format(stp.getTimeStamp());
					String[] youso = date.split("-");
					String youbi = (new SimpleDateFormat("u")).format(stp.getTimeStamp());
					if(!(youbi.equals(6)||youbi.equals(7))){
						temp.add(youso[2]);
					}
				}
				double rate = (double)temp.size()/(double)numberoflogs.get(id);
				if(rate>minrate){
					tempmap.put(sp, map.get(id).get(sp).size());
				}
			}
			res.put(id, tempmap);
			if(tempmap.size()>0){
				counter++;
			}
		}
		System.out.println(counter + " IDs have SPs above minimum rate");
		return res;
	}

	public static HashMap<String,HashMap<LonLat,Double>> ExcludeLowFrequentSPsbyVisitRate(HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> map, HashMap<String,LonLat> idhome, HashMap<String, Integer> numberoflogs, int minpoints){
		HashMap<String,HashMap<LonLat,Double>> res = new HashMap<String,HashMap<LonLat,Double>>();
		for(String id : map.keySet()){
			HashMap<LonLat,Double> tempmap = new HashMap<LonLat,Double>();
			for(LonLat sp : map.get(id).keySet()){
				HashSet<String> temp = new HashSet<String>();
				for(STPoint stp : map.get(id).get(sp)){
					String date = (new SimpleDateFormat("yyyy-MM-dd")).format(stp.getTimeStamp());
					String[] youso = date.split("-");
					String youbi = (new SimpleDateFormat("u")).format(stp.getTimeStamp());
					if(!(youbi.equals(6)||youbi.equals(7))){
						temp.add(youso[2]);
					}
				}
				double rate = (double)temp.size()/(double)numberoflogs.get(id);
				if(map.get(id).get(sp).size()>minpoints){
					tempmap.put(sp, rate);
				}
			}
			res.put(id, tempmap);
		}
		return res;
	}

	public static HashMap<String,LonLat> getOfficePoints(HashMap<String,HashMap<LonLat,Integer>> map, HashMap<String,LonLat> idhome, double homedis){
		int count = 0;
		HashMap<String,LonLat> res = new HashMap<String,LonLat>();
		for(String id:map.keySet()){
			if(map.get(id).size()>0){
				if(idhome.containsKey(id)){
					LonLat office = getOffice(map.get(id),idhome,id,homedis);
					res.put(id, office);
					if(office!=null){
						count ++;
					}
				}
			}
		}
		System.out.println(count +" IDs have offices away from homes");
		return res;
	}

	public static HashMap<String,LonLat> getOfficePointsbyVisitRate(HashMap<String,HashMap<LonLat,Double>> map, HashMap<String,LonLat> idhome, double homedis){
		HashMap<String,LonLat> res = new HashMap<String,LonLat>();
		for(String id:map.keySet()){
			if(map.get(id).size()>0){
				if(idhome.containsKey(id)){
					LonLat office = getOfficebyVisitRate(map.get(id),idhome,id,homedis);
					res.put(id, office);
				}
			}
		}
		return res;
	}

	public static LonLat getOffice(HashMap<LonLat,Integer> map, HashMap<String,LonLat> idhome, String id, double homedis){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(LonLat p:map.keySet()){
			list.add(map.get(p));
		}
		Collections.sort(list);
		Collections.reverse(list);
		int count = list.get(0);
		LonLat point = null;
		LonLat home = idhome.get(id);
		if(home!=null){
			for(LonLat p :map.keySet()){
				if(map.get(p)==count){
					point = p;
					if(point.distance(home)<homedis){
						if(list.size()>=2){
							count = list.get(1);
							for(LonLat p2 :map.keySet()){
								if(map.get(p2)==count){
									point = p2;
									if((point.distance(home)<homedis)){
										if(list.size()>=3){
											count = list.get(2);
											for(LonLat p3 :map.keySet()){
												if(map.get(p3)==count){
													point = p3;
													if((point.distance(home)<homedis)){
//														System.out.println("OMGOMGOMGOMGOMGOMG");
														return null;
													}
													else{
														return point;
													}
												}
											}
										}
										else{
											return null;
										}
									}
									else{
										return point;
									}
								}
							}
						}
						else{
							return null;
						}
					}
					else{
						return point;
					}

				}
			}
		}	
		return point;
	}

	public static LonLat getOfficebyVisitRate(HashMap<LonLat,Double> map, HashMap<String,LonLat> idhome, String id, double homedis){
		ArrayList<Double> list = new ArrayList<Double>();
		for(LonLat p:map.keySet()){
			list.add(map.get(p));
		}
		Collections.sort(list);
		Collections.reverse(list);
		Double count = list.get(0);
		LonLat point = null;
		LonLat home = idhome.get(id);
		if(home!=null){
			for(LonLat p :map.keySet()){
				if(map.get(p)==count){
					point = p;
					if(point.distance(home)<homedis){
						if(list.size()>=2){
							count = list.get(1);
							for(LonLat p2 :map.keySet()){
								if(map.get(p2)==count){
									point = p2;
									if((point.distance(home)<homedis)){
										if(list.size()>=3){
											count = list.get(2);
											for(LonLat p3 :map.keySet()){
												if(map.get(p3)==count){
													point = p3;
													if((point.distance(home)<homedis)){
														return null;
													}
													else{
														return point;
													}
												}
											}
										}
										else{
											return null;
										}
									}
									else{
										return point;
									}
								}
							}
						}
						else{
							return null;
						}
					}
					else{
						return point;
					}

				}
			}
		}		
		return point;
	}

	public static File writeOut(HashMap<String,LonLat> map, File out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		int count = 0;
		for(String id:map.keySet()){
			if(map.get(id)!=null){
				bw.write(id + "\t" + map.get(id).getLat() + "\t" + map.get(id).getLon());
				bw.newLine();
				count++;
			}
		}
		System.out.println(count+" IDs' Offices were detected");
		bw.close();
		return out;
	}

	
}

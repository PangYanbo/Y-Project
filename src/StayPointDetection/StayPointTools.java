package StayPointDetection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.STPoint;
import GPSOrganize.IDExtractor;

public class StayPointTools {

	public static double getAverageSPs(HashSet<Integer> set,HashMap<Integer, ArrayList<STPoint>> map, double d, double r, int min) throws NumberFormatException, ParseException{
		ArrayList<Integer> NumofSPs = new ArrayList<Integer>();
		int counter = 0;
		HashMap<LonLat, ArrayList<STPoint>> SPmap = null;
		for(int id:set){
			ArrayList<STPoint> list = IDExtractor.getDataofIDListwithIntervalfromList(map.get(id),",",id);
			SPmap = StayPointGetter.getStayPoints(list,d,r,min);
			int num = 0;
			for(LonLat p:SPmap.keySet()){
				if(SPmap.get(p).size()>=min){
					num++;
				}
			}
			NumofSPs.add(num);
			counter++;
			//			System.out.println(counter);
			if(counter%100==0){
				System.out.println(counter);
			}
		}
		int sum= 0;
		for(int n:NumofSPs){
			sum = sum+n;
		}
		double avgpoints = sum/(double)counter;
		return avgpoints;
	}
	
	public static File getHistogramofSPs(File out, HashSet<Integer> set,HashMap<Integer, ArrayList<STPoint>> map, double d, double r, int min) throws NumberFormatException, ParseException, IOException{
		int counter = 0;
		HashMap<LonLat, ArrayList<STPoint>> SPmap = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(int id:set){
			ArrayList<STPoint> list = IDExtractor.getDataofIDListwithIntervalfromList(map.get(id),",",id);
			SPmap = StayPointGetter.getStayPoints(list,d,r,min);
			int num = 0;
			for(LonLat p:SPmap.keySet()){
				if(SPmap.get(p).size()>=min){
					num++;
				}
			}
			bw.write(id + "," + num);
			bw.newLine();
			counter++;
			//			System.out.println(counter);
			if(counter%100==0){
				System.out.println(counter);
			}
		}
		bw.close();
		return out;
	}

	public static File writeout(int id,HashMap<LonLat, ArrayList<STPoint>> map, File out){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
			for(LonLat p:map.keySet()){
				for(STPoint sp:map.get(p)){
					bw.write(id+","+p.getLon()+","+p.getLat()+","+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(sp.getTimeStamp()));
					bw.newLine();
				}
			}
			bw.close();
		}
		catch(FileNotFoundException xx) {
			System.out.println("File not found 5");
		}
		catch(IOException xxx) {
			System.out.println(xxx);
		}
		return out;
	}

	public static File writeoutonlyFrequentSPs(int id, HashMap<LonLat, ArrayList<STPoint>> map, File out, int min){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
			for(LonLat p:map.keySet()){
				if(map.get(p).size()>=min){
					//for (STPoint sp:map.get(p)){
					//bw.write(id+","+p.getLon()+","+p.getLat()+","+map.get(p).size()+","+STPoint.FORMAT_YMDHMS.format(sp.getTimeStamp()));
					bw.write(id+","+p.getLon()+","+p.getLat()+","+map.get(p).size());
					bw.newLine();
					//}
				}
			}
			bw.close();
		}
		catch(FileNotFoundException xx) {
			System.out.println("File not found 5");
		}
		catch(IOException xxx) {
			System.out.println(xxx);
		}
		return out;
	}
	
	public static Integer NumberofDays(ArrayList<STPoint> list){
		int days = 0;
		HashSet<String> set = new HashSet<String>();
		for(STPoint p : list){
			Date date = p.getTimeStamp();
			String d =  (new SimpleDateFormat("yyyy-MM-dd")).format(date);
			String[] x = d.split("-");
			String xd = x[2];
			set.add(xd);
		}
		days = set.size();
		return days;
	}
	
	//TODO change the logic
	public static Integer NumberofWeekDays(ArrayList<STPoint> list){
		int days = 0;
		HashSet<String> set = new HashSet<String>();
		for(STPoint p : list){
			Date date = p.getTimeStamp();
			String d1 =  (new SimpleDateFormat("yyyy-MM-dd")).format(date);
			String[] x = d1.split("-");
			String xd = x[2];
			Integer d = Integer.valueOf(xd);
			if(!((d==3)||(d==10)||(d==17)||(d==24)||(d==2)||(d==9)||(d==16)||(d==23)||(d==11))){
				set.add(xd);
			}
		}
		days = set.size();
		return days;
	}
	
	public static HashMap<String,HashMap<LonLat,Integer>> ExcludeLowFrequentSPsbyNumberofPoints(HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> map, HashMap<String,Integer> totaldays, double minrate){
		HashMap<String,HashMap<LonLat,Integer>> res = new HashMap<String,HashMap<LonLat,Integer>>();
		for(String id : map.keySet()){
			HashMap<LonLat,Integer> tempmap = new HashMap<LonLat,Integer>();
			for(LonLat sp : map.get(id).keySet()){
				HashSet<String> temp = new HashSet<String>();
				for(STPoint stp : map.get(id).get(sp)){
					String date = (new SimpleDateFormat("yyyy-MM-dd")).format(stp.getTimeStamp());
					String[] youso = date.split("-");
					temp.add(youso[2]);
				}
				double rate = (double)temp.size()/(double)totaldays.get(id);
				if(rate>minrate){
					tempmap.put(sp, map.get(id).get(sp).size());
				}
			}
			res.put(id, tempmap);
		}
		return res;
	}
	
	public static HashMap<String,LonLat> getHomeMap(File in) throws IOException{
		HashMap<String,LonLat> res = new HashMap<String,LonLat>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line=br.readLine()) != null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			LonLat point = new LonLat(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
			res.put(id, point);
		}
		br.close();
		return res;
	}
	
}

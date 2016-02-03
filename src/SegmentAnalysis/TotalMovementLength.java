package SegmentAnalysis;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import DataModify.ExtractFile;
import DisasterAlert.DisLogDecider;
import DisasterAlert.DisasterLogs;
import IDExtract.ID_Extract_Tools;
import MainRuns.FilePaths;

public class TotalMovementLength {

	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd");//change time format
	static File shapedir = new File("/home/t-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	private static final String homepath = "/home/t-tyabe/Data/TotalLengthCheck/";
	private static final String GPSpath  = "/tmp/bousai_data/gps_";

	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{

		String type = args[0]; //rain,heats,emg1

		File dir = new File(homepath);
		dir.mkdir();

		File in = new File("/home/t-tyabe/Data/DisasterLogs/DisasterAlertData.csv");
		File out = new File("/home/t-tyabe/Data/DisasterLogs/DisasterAlertData_shutoken_"+type+".csv");
		File jiscodes = new File("/home/t-tyabe/Data/ShutokenSHP/JIScodes.csv");
		DisLogDecider.choosebyAreaDateType(in,out,jiscodes,type,"2014-10-21","2015-09-30");		

		String disasterlogfile = "/home/t-tyabe/Data/DisasterLogs/DisasterAlertData_shutoken_"+type+".csv";
		runforallevents(disasterlogfile, type);

		File res = new File(homepath+type+"_length.csv");
		File cleanres = new File(homepath+type+"_length_clean.csv");
		clean(res,cleanres);

	}

	public static void clean(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;

		HashMap<String, HashMap<Integer,String>> res = new HashMap<String, HashMap<Integer,String>>();

		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String ymd = tokens[1];
			Integer level = Integer.valueOf(tokens[2]);
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
				//比較関数
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
			String ymd2 = tokens2[1];
			Integer level2 = Integer.valueOf(tokens2[2]);
			if(res2.get(id).get(ymd2)==level2){
				bw.write(line2);
				bw.newLine();
			}
		}
		br2.close();
		bw.close();
	}

	public static void runforallevents(String dislog, String type) throws IOException, NumberFormatException, ParseException{

		File out = new File(homepath+type+"_length.csv");

		HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> dislogs = DisasterLogs.sortLogs(dislog);

		int c = 0;
		for(String day : dislogs.keySet()){
			for(String time : dislogs.get(day).keySet()){
				c = c + dislogs.get(day).get(time).size();
			}
		}
		System.out.println("#successfully sorted out disaster info logs... there are " + c);

		int count = 0;
		for(String ymd : dislogs.keySet()){
			for(String time : dislogs.get(ymd).keySet()){
				for(String level : dislogs.get(ymd).get(time).keySet()){
					if(filedoublechecker(ymd,time,type,level)==true){
						System.out.println("#starting run for " + ymd +", time: "+ time + ", level:" +level);
						ArrayList<String> codes = dislogs.get(ymd).get(time).get(level);
						run(codes, ymd, time, level, dislog, type, out);
						System.out.println("------------------done " + count + " disasters------------------");
						System.out.println(" ");
					}
					count++;
				}
			}
		}
	}

	public static boolean filedoublechecker(String ymd, String time, String type, String level){
		File file = new File(homepath+type+"_"+level+"/"+ymd+"_"+time);
		if(file.exists()){
			return false;
		}
		else{
			return true;
		}
	}

	public static void run(ArrayList<String> zones, String ymd, String time, String level, String dislog, String type, File out) throws IOException, NumberFormatException, ParseException{
		System.out.println("start run for " + zones.size() +" zones"); System.out.println("zones are " + zones);
		String disGPS = GPSpath+ymd+".tar.gz"; //ymd=yyyymmddの形になっている

		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));

		if(new File(disGPS).exists()){ //もしログのファイルがあれば！
			ExtractFile.extractfromcommand(ymd); System.out.println("#done uncompressing " + disGPS);

			String unzippedfile = FilePaths.deephomepath(ymd);

			HashSet<String> targetIDs = new HashSet<String>();
			targetIDs = extractID(unzippedfile,time,zones,5, type); //0: minimum logs
			System.out.println("#the number of IDs for " + ymd+time+ " is " + targetIDs.size());

			if(targetIDs.size()>10){ //対象人数で絞る
				HashMap<String, HashMap<Integer,LonLat>> id_data = getallData(new File(unzippedfile),targetIDs);
				for(String id : id_data.keySet()){
					Double distance = calculateLength(id_data.get(id));
					String strdis = String.valueOf(distance);
					bw.write(id + "," + ymd + "," + level + "," + strdis);
					bw.newLine();
				}

			}
			File i = new File(unzippedfile); i.delete();
		}
		bw.close();
	}

	public static Double calculateLength(HashMap<Integer,LonLat> map){ //return km

		List<Entry<Integer, LonLat>> entries = new ArrayList<Entry<Integer, LonLat>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<Integer, LonLat>>() {
			//比較関数
			@Override
			public int compare(Entry<Integer, LonLat> o1, Entry<Integer, LonLat> o2) {
				return o1.getKey().compareTo(o2.getKey());  
			}
		});

		LonLat prevpoint = new LonLat(0,0);
		Double lengthsum = 0d;
		for (Entry<Integer, LonLat> e : entries) {
			if(prevpoint.getLon()==0){
				prevpoint = e.getValue();
			}
			else{
				lengthsum = lengthsum + prevpoint.distance(e.getValue());
				prevpoint = e.getValue();
			}
		}
		return lengthsum/1000d;
	}

	public static HashMap<String, HashMap<Integer,LonLat>> getallData(File in, HashSet<String> ids) throws NumberFormatException, IOException{

		HashMap<String, HashMap<Integer,LonLat>> res = new HashMap<String, HashMap<Integer,LonLat>>();

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];		
						if(ids.contains(id)){
							Integer time = getonlytimeinsecs(tokens[4]);
							Double lat = Double.parseDouble(tokens[2]);
							Double lon = Double.parseDouble(tokens[3]);
							LonLat p = new LonLat(lon,lat);
							if(res.containsKey(id)){
								res.get(id).put(time, p);
							}
							else{
								HashMap<Integer,LonLat> temp = new HashMap<Integer,LonLat>();
								temp.put(time, p);
								res.put(id, temp);
							}
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		return res;
	}

	public static HashSet<String> extractID(String in, String t, ArrayList<String> JIScodes, int minimumlogs, String type){
		HashSet<String> map = new HashSet<String>();
		File infile = new File(in);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(infile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		String prevline = null;
		try {
			while((line=br.readLine())!=null){
				if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
					String[] tokens = line.split("\t");
					if(tokens.length>=5){
						String id = tokens[0];
						if(!id.equals("null")){
							if(!tokens[4].equals("null")){
								if(tokens[4].length()>=18){
									String tz = tokens[4].substring(11,19);
									String time = DisasterLogs.converttime(tz);
									if(time.equals(t)){
										Double lat = Double.parseDouble(tokens[2]);
										Double lon = Double.parseDouble(tokens[3]);
										LonLat p = new LonLat(lon,lat);
										if(type.equals("eq")){
											String JIScode = AreaOverlapPref(p,JIScodes);
											if(!JIScode.equals("null")){
												map.add(id);
											}	
										}
										else{
											String JIScode = AreaOverlap(p,JIScodes);
											if(!JIScode.equals("null")){
												map.add(id);
											}
										}
									}
								}
							}
						}
					}
					prevline = line;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static String AreaOverlap(LonLat point, ArrayList<String> JIScodes){
		List<String> zonecodeList = gchecker.listOverlaps("JCODE",point.getLon(),point.getLat());
		if(zonecodeList == null || zonecodeList.isEmpty()) {
			return "null";
		}
		else if (JIScodes.contains(zonecodeList.get(0))){
			return zonecodeList.get(0);
		}
		else{
			return "null";
		}
	}

	public static String AreaOverlapPref(LonLat point, ArrayList<String> JIScodes){ //JIScodes = 08,...
		List<String> zonecodeList = gchecker.listOverlaps("JCODE",point.getLon(),point.getLat());
		//		System.out.println("point: " + point + ", zonecodelist: " + zonecodeList);
		if(zonecodeList == null || zonecodeList.isEmpty()) { //zonecodelist.get(0) = 8988, ...
			return "null";
		}
		else if (zonecodeList.get(0).length()>0){
			if (Integer.valueOf(zonecodeList.get(0))>=10000){
				String code =  zonecodeList.get(0).substring(0,2);
				if(JIScodes.contains(code)){
					return code;
				}
				else{
					return "null";
				}
			}
			else if (Integer.valueOf(zonecodeList.get(0))<9999){
				String code =  String.format("%02d", Integer.valueOf(zonecodeList.get(0).substring(0,1)));
				if(JIScodes.contains(code)){
					return code;
				}
				else{
					return "null";
				}
			}
			else{
				return "null";
			}
		}
		else{
			return "null";
		}
	}


	public static boolean SameLogCheck(String line, String prevline){
		if(line.equals(prevline)){
			return false;
		}
		else{
			return true;
		}
	}

	public static Integer getonlytimeinsecs(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		Integer hour = Integer.valueOf(time.split(":")[0]);
		Integer mins = Integer.valueOf(time.split(":")[1]);
		Integer secs = Integer.valueOf(time.split(":")[2]);
		Integer timeinsecs = hour*3600+mins*60+secs;
		return timeinsecs;
	}
}

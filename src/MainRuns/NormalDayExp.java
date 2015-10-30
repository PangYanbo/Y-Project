package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;
import DisasterAlert.DayChooser;
import MobilityAnalyser.HomeOfficeMaps;

public class NormalDayExp {

	private static final String homepath = "/home/c-tyabe/Data/expALL2/";
	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_MDS = new SimpleDateFormat("HH:mm:ss");//change time format

	public static void main(String args[]) throws NumberFormatException, IOException, ParseException{

		run();
		
	}


	public static void run() throws IOException, NumberFormatException, ParseException{

		ArrayList<String> idlist = getIDs(new File(homepath+"id_home.csv"));
		String ymd = "20150101";
		String dataforexp = homepath+"dataforexp.csv";
		HashSet<String> targetdays = DayChooser.getTargetDates2(ymd); System.out.println("#the number of days are " + targetdays.size());
		Makedata4exp.makedata2(dataforexp, targetdays, idlist); System.out.println("#successfully made data for exp");

		executeAnalyser(dataforexp, FilePaths.dirfile(homepath,"id_office.csv"), homepath);

		File data = new File(dataforexp); data.delete();

	}

	public static ArrayList<String> getIDs(File in) throws IOException{
		ArrayList<String> res = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			res.add(id);
		}
		br.close();
		return res;
	}

	public static void executeAnalyser(String infile, String idoffice, String outputpath) throws NumberFormatException, IOException, ParseException{
		File in = new File(infile);
		File Office = new File(idoffice);

		HashMap<String,HashMap<String,ArrayList<Integer>>> omap = getLogsnearX(in,Office);
		System.out.println("#done getting logs near office");

		HashMap<String, HashMap<String, Integer>> officeenter = officeEnterTime(omap); //System.out.println("#done getting office enter time");

		System.out.println("#writing everything out...");
		writeout(officeenter, outputpath, "office_enter.csv");
	}

	public static HashMap<String, HashMap<String, Integer>> officeEnterTime(HashMap<String,HashMap<String,ArrayList<Integer>>> omap){
		HashMap<String, HashMap<String, Integer>> OEntertimes = new HashMap<String, HashMap<String, Integer>>();
		for(String id : omap.keySet()){
			HashMap<String, Integer> day_lastlog = new HashMap<String, Integer>();
			for(String day : omap.get(id).keySet()){
				ArrayList<Integer> list = omap.get(id).get(day);
				Collections.sort(list);
				if(list.size()>1){
					day_lastlog.put(day, list.get(0));
				}
			}
			OEntertimes.put(id, day_lastlog);
		}
		return OEntertimes;
	}

	public static File writeout(HashMap<String, HashMap<String, Integer>> map, String path, String name) throws IOException{
		File out = new File(path+name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			for(String day : map.get(id).keySet()){
				double time = (double)map.get(id).get(day)/(double)3600;
				BigDecimal x = new BigDecimal(time);
				x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
				bw.write(id + "," + day + "," + x);
				bw.newLine();
			}
		}
		bw.close();
		return out;
	}

	public static HashMap<String,HashMap<String,ArrayList<Integer>>> getLogsnearX(File in, File X) throws IOException, NumberFormatException, ParseException{
		HashMap<String,LonLat> id_X = HomeOfficeMaps.getXMap(X);
		HashMap<String,HashMap<String,ArrayList<Integer>>> res = new HashMap<String,HashMap<String,ArrayList<Integer>>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = (tokens[0]);
			
			if(id_X.containsKey(id)){
				Double lon = Double.parseDouble(tokens[2]);
				Double lat = Double.parseDouble(tokens[1]);
				LonLat point = new LonLat(lon,lat);
				String date = tokens[3];
				
				String[] youso = date.split(" ");
				String ymd = youso[0];
				String[] youso2 = ymd.split("-");
				String month  = youso2[1];
				String hiniti = youso2[2];
				String hms = youso[1];
				String hour = hms.substring(0,2);

				if(point.distance(id_X.get(id))<500){
					Integer time = HomeOfficeMaps.converttoSecs(SDF_MDS.format(SDF_TS.parse(tokens[3])));
					if(Integer.valueOf(hour)<3){
						time = time + 86400;
					}

					if(res.containsKey(id)){
						if(res.get(id).containsKey(month+"-"+hiniti)){
							res.get(id).get(month+"-"+hiniti).add(time);
						}
						else{
							ArrayList<Integer> list = new ArrayList<Integer>();
							list.add(time);
							res.get(id).put(month+"-"+hiniti, list);
						}
					}
					else{
						HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
						ArrayList<Integer> list = new ArrayList<Integer>();
						list.add(time);
						map.put(month+"-"+hiniti, list);
						res.put(id, map);
					}
				}
			}
		}		
		br.close();
		return res;
	}
	
}

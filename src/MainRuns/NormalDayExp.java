package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import DisasterAlert.DayChooser;
import MobilityAnalyser.HomeOfficeMaps;

public class NormalDayExp {

	private static final String homepath = "/home/c-tyabe/Data/expALL/";

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

		HashMap<String,HashMap<String,ArrayList<Integer>>> omap = HomeOfficeMaps.getLogsnearX(in,Office);
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

}

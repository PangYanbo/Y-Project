package DisasterAlert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import IDExtract.ID_Extract_Tools;

public class ExtractIDbyDate {

	//	public static void main(String args[]) throws IOException{
	//		ArrayList<String> disasterzones = new ArrayList<String>();
	//		HashSet<String> targetIDs = extractID(args[0],disasterzones);
	//	}

	static File shapedir = new File("/home/c-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	//check Pref or not.! 
	
	public static HashMap<String,LonLat> extractID(String in, String t, ArrayList<String> JIScodes, int minimumlogs){
		HashMap<String,LonLat> map = new HashMap<String,LonLat>();
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
								String tz = tokens[4].substring(11,19);
								String time = DisasterLogs.converttime(tz);
								if(time.equals(t)){
									Double lat = Double.parseDouble(tokens[2]);
									Double lon = Double.parseDouble(tokens[3]);
									LonLat p = new LonLat(lon,lat);
									String JIScode = AreaOverlapPref(p,JIScodes);
									if(!JIScode.equals("null")){
										map.put(id,p);
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
		System.out.println("zonecodelist: " + zonecodeList);
		if(zonecodeList == null || zonecodeList.isEmpty()) { //zonecodelist.get(0) = 8988, ...
			return "null";
		}
		else if (Integer.valueOf(zonecodeList.get(0))>=10000){
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


}

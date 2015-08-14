package DisasterAlert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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

	public static HashSet<String> extractID(String in, String t, ArrayList<String> JIScodes, int minimumlogs) throws IOException{
		HashSet<String> set = new HashSet<String>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = null;
		String prevline = null;
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
								if(AreaOverlap(p,JIScodes)==true){
									set.add(id);
								}
							}
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		return set;
	}

	public static boolean AreaOverlap(LonLat point, ArrayList<String> JIScodes){
		List<String> zonecodeList = gchecker.listOverlaps("JCODE",point.getLon(),point.getLat());
		if(zonecodeList == null || zonecodeList.isEmpty()) {
			return false;
		}
		else if (JIScodes.contains(zonecodeList.get(0))){
			return true;
		}
		else{
			return false;
		}
	}


}

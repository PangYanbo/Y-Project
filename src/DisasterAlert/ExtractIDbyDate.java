package DisasterAlert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

public class ExtractIDbyDate {

//	public static void main(String args[]) throws IOException{
//		ArrayList<String> disasterzones = new ArrayList<String>();
//		HashSet<String> targetIDs = extractID(args[0],disasterzones);
//	}
	
	static File shapedir = new File("/home/c-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
	
	public static HashSet<String> extractID(String in, ArrayList<String> JIScodes) throws IOException{
		HashSet<String> set = new HashSet<String>();
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			Double lat = Double.parseDouble(tokens[1]);
			Double lon = Double.parseDouble(tokens[2]);
			LonLat p = new LonLat(lon,lat);
			if(AreaOverlap(p,JIScodes)==true){
				set.add(tokens[0]);
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

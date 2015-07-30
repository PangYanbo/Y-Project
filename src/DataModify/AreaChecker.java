package DataModify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

public class AreaChecker {

	/*
	 * params 
	 * args [0] : all data file
	 * args [1] : result data file 
	 * CHECK : shape file of area
	 * 
	 */
	
	public static void main(String args[]) throws IOException{
		System.out.println("#start");
		WriteonlyGcheckedLogs(args[0],args[1]);
	}
	
	static File shapedir = new File("/home/c-tyabe/Data/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
	
	public static boolean AreaOverlap(LonLat point){
		List<String> zonecodeList = gchecker.listOverlaps("JCODE",point.getLon(),point.getLat());
		if( zonecodeList == null || zonecodeList.isEmpty()) {
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void WriteonlyGcheckedLogs(String args, String args2) throws IOException{
		int counter = 0;
		BufferedReader br = new BufferedReader(new FileReader(new File(args)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args2)));
		String line = null;
		System.out.println("#start reading lines");
		while((line = br.readLine())!=null){
			String[] tokens = line.split("\t");
			Double lon = Double.parseDouble(tokens[2]);
			Double lat = Double.parseDouble(tokens[1]);
			System.out.println("#read ... lon:" + lon + " lat: "+ lat);
			LonLat p = new LonLat(lon,lat);
			if(AreaOverlap(p)==true){
				bw.write(line);
				bw.newLine();
				counter++;
				if(counter%10==0){
					System.out.println("#done : " + counter);
				}
			}
			else{
				System.out.println("nope ... lon:" + lon + " lat: "+ lat);
			}
		}
		br.close();
		bw.close();
	}
	
}

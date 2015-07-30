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

public class testGcheck {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/test.txt");
		File out = new File("c:/users/yabetaka/desktop/testres.txt");
		WriteonlyGcheckedLogs(in,out);
	}
	
	static File shapedir = new File("c:/users/yabetaka/desktop/jpnshp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
	
	public static boolean AreaOverlap(LonLat point){
		List<String> zonecodeList = gchecker.listOverlaps("JCODE",point.getLon(),point.getLat());
		if( zonecodeList == null || zonecodeList.isEmpty()) {
			return false;
		}
		else{
			System.out.println(zonecodeList.get(0));
			return true;
		}
	}
	
	public static void WriteonlyGcheckedLogs(File args, File args2) throws IOException{
		int counter = 0;
		BufferedReader br = new BufferedReader(new FileReader(args));
		BufferedWriter bw = new BufferedWriter(new FileWriter(args2));
		String line = null;
		while((line = br.readLine())!=null){
			String[] tokens = line.split("\t");
			Double lon = Double.parseDouble(tokens[1]);
			Double lat = Double.parseDouble(tokens[2]);
			LonLat p = new LonLat(lon,lat);
			if(AreaOverlap(p)==true){
				bw.write(line);
				bw.newLine();
				counter++;
				if(counter%1==0){
					System.out.println("#done : " + counter);
				}
			}
		}
		br.close();
		bw.close();
	}
	
}

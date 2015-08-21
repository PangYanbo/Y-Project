package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ObtainAttributes {

	public static void getAttributes(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String diff = tokens[1];
			String nowzone = tokens[2];
			String homezone = tokens[3];
			String dis = tokens[4];
			
			
		}
		br.close();
		bw.close();
	}
	
	public static String getLanduse(String zone, File landusefile){
		String landuse = null;
		
		return landuse;
	}
	
	public static String getRoadnetwork(String zone, File roadnwfile){
		String roadnw = null;
		
		return roadnw;
	}
	
	public static String getlandprice(String zone, File landprice){
		String landp = null;
		
		return landp;		
	}
}

package MobilityAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class testSortMapbyjunban {

	public static void main(String args[]) throws IOException{
		File in = new File ("c:/users/yabetaka/desktop/ahh.txt");
		BufferedReader br = new BufferedReader(new FileReader(in));
		HashMap<Integer,LonLat> map = new HashMap<Integer,LonLat>();
		String line = null;
		LonLat prevp = new LonLat(100,100);
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			Integer id = Integer.valueOf(tokens[0]);
			LonLat p = new LonLat(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
			System.out.println(p.distance(prevp));
			prevp = p;
		}
		br.close();
		
		
	}
	
}

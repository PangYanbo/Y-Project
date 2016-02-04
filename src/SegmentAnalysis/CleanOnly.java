package SegmentAnalysis;

import java.io.File;
import java.io.IOException;

public class CleanOnly {

	private static final String homepath = "c:/Users/t-tyabe/Desktop/";

	public static void main(String args[]) throws IOException{
		
		String type = "rain";
		
		File res = new File(homepath+type+"distance_temp.csv");
		File cleanres = new File(homepath+type+"distance_clean.csv");
		TotalMovementLength.getHighestLevelData(res,cleanres);

	}
	
	
	
}

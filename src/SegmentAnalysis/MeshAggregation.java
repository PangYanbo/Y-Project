package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import DisasterAlert.DisasterLogs;
import IDExtract.ID_Extract_Tools;
import MainRuns.FilePaths;

public class MeshAggregation {

	public static void main(String args[]) throws IOException{

		/*
		 * uncompress
		 * read 
		 * take out logs which are between 9:30PM-10:30PM
		 * take out logs which are in Kanto
		 * aggregate
		 * 
		 */

		ArrayList<String> ymds = new ArrayList<String>();
		ymds.add("20150512");
//		ymds.add("20150522");
		ymds.add("20150511");
//		ymds.add("20150521");
		
		
		for(String ymd : ymds){
//		ExtractFile.extractfromcommand(ymd); 
		String unzippedfile = FilePaths.deephomepath(ymd);

		File out = new File("/home/t-tyabe/Data/"+ymd+"_raw_onlyshutoken.csv");

		extractID(unzippedfile,out,ymd);
		

		}
		
		
	}

	public static void extractID(String in, File out, String t) throws IOException{
		File infile = new File(in);
		BufferedReader br = new BufferedReader(new FileReader(infile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					String id = tokens[0];
					if(!id.equals("null")){
						if(!tokens[4].equals("null")){
							if(tokens[4].length()>=18){
								String tz = tokens[4].substring(11,19);
								String time = DisasterLogs.converttime(tz);
								if(time.equals(t)){
									Double lat = Double.parseDouble(tokens[2]);
									Double lon = Double.parseDouble(tokens[3]);
									LonLat p = new LonLat(lon,lat);
									String yesno = AreaOverlap(p);
									if(yesno.equals("yep")){
										bw.write(id+"\t"+lon+"\t"+lat);
										bw.newLine();
									}	

								}
							}
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		bw.close();
	}

	public static String AreaOverlap(LonLat point){
		List<String> zonecodeList = gchecker.listOverlaps("A03_001",point.getLon(),point.getLat());
		if(zonecodeList == null || zonecodeList.isEmpty()) {
			return "null";
		}
		else{
			return "yep";
		}
	}

	static File shapedir = new File("/home/t-tyabe/Data/ShutokenSHP");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
}

package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

import DataModify.ExtractFile;
import IDExtract.ID_Extract_Tools;

public class Makedata4exp {

	public static final String GPSpath  = "/tmp/bousai_data/gps_";
	public static final String GPSdeeppath = "/home/c-tyabe/Data/grid/0/tmp/ktsubouc/gps_";

	public static void makedata(String outpath, HashSet<String> targetdays, HashMap<String,String> targetIDs) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outpath),true));
		int count = 0;
		for(String d : targetdays){
			int daa = 0;
			String[] youso = d.split("-");
			String ymd = youso[0]+youso[1]+youso[2];
			String GPS = GPSpath+ymd+".tar.gz"; //ymd=yyyymmdd‚ÌŒ`‚É‚È‚Á‚Ä‚¢‚é
			ExtractFile.uncompress(Paths.get(GPS));

			BufferedReader br = new BufferedReader(new FileReader(new File(GPSdeeppath+ymd+".csv")));
			String line = null;
			String prevline = null;
			while((line=br.readLine())!=null){
				if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
					String[] tokens = line.split("\t");
					if(tokens.length>=5){
						if(!tokens[4].equals("null")){
							String id = tokens[0];
							if(targetIDs.keySet().contains(id)){
								String lat = tokens[2];
								String lon = tokens[3];
								String time = converttime(tokens[4]);
								bw.write(id + "\t" + lat + "\t" + lon + "\t" + time);
								bw.newLine();
								count++;
								daa++;
							}
						}
					}
					prevline = line;
				}
			}
			br.close();
			System.out.println("done " + d +" ... " + daa +" lines");
			File i = new File(GPSdeeppath+ymd+".csv");
			i.delete();
		}

		bw.close();
		System.out.println("#the size of data for exp is "+ count);
	}

	public static String converttime(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		String res = x[0]+ " " + time;
		return res;
	}

}

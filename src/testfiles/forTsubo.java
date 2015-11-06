package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataModify.ExtractFile;
import IDExtract.ID_Extract_Tools;
import MainRuns.FilePaths;

public class forTsubo {

	public static final String GPSdeeppath = "/home/c-tyabe/Data/grid/0/tmp/ktsubouc/gps_";

	public static void main(String args[]) throws IOException{

		File out = new File("/home/c-tyabe/Data/dayslogs_results.csv");

		ArrayList<String> subjects = getdates();
		//		for(int i = 0; i<subjects.size(); i++){
		//			System.out.println(subjects.get(i));
		//		}

		for(String ymd : subjects){
			ExtractFile.extractfromcommand(ymd); System.out.println("#done uncompressing "+ymd);
			String unzippedfile = FilePaths.deephomepath(ymd);
			getres(unzippedfile, ymd, out);
		}
	}

	//	public static void main(String[] args) throws IOException{
	//		String ymd = "20150601";
	//		File out = new File("/home/c-tyabe/Data/idslogs_results.csv");
	//		ExtractFile.extractfromcommand(ymd); System.out.println("#done uncompressing "+ymd);
	//		String unzippedfile = FilePaths.deephomepath(ymd);
	//		getloghisto(unzippedfile,ymd,out);
	//	}

	public static ArrayList<String> getdates(){
		ArrayList<String> subjects = new ArrayList<String>();
		String year = "2014";
		for(int i = 11; i<=12; i++){
			for(int j = 1; j<=30; j++){
				subjects.add(year+String.valueOf(i)+String.format("%02d", j));
			}
		}
		year = "2015";
		for(int i = 1; i<=7; i++){
			for(int j = 1; j<=28; j++){
				subjects.add(year+String.format("%02d", i)+String.format("%02d", j));
			}
		}
		return subjects;
	}

	public static void getres(String file, String ymd, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		String prevline = null;
		int count = 0;
		HashSet<String> set = new HashSet<String>();
		while((line=br.readLine())!= null){
			if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];
						set.add(id);
						count++;
					}
				}
				prevline = line;
			}
		}
		bw.write(ymd+","+count+","+set.size());
		bw.newLine();
		br.close();
		bw.close();
		File i = new File(GPSdeeppath+ymd+".csv");
		i.delete();
	}

	public static void getloghisto(String file, String ymd, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		String prevline = null;
		HashMap<String,Integer> set = new HashMap<String,Integer>();
		while((line=br.readLine())!= null){
			if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];
						if(set.containsKey(id)){
							int count = set.get(id) + 1;
							set.put(id, count);
						}
						else{
							set.put(id, 1);
						}
					}
				}
				prevline = line;
			}
		}
		for(String ids : set.keySet()){
			bw.write(ids+","+set.get(ids));
			bw.newLine();
		}
		br.close();
		bw.close();
		File i = new File(GPSdeeppath+ymd+".csv");
		i.delete();
	}

}

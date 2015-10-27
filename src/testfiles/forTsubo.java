package testfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import DataModify.ExtractFile;
import IDExtract.ID_Extract_Tools;
import MainRuns.FilePaths;

public class forTsubo {

	public static final String GPSdeeppath = "/home/c-tyabe/Data/grid/0/tmp/ktsubouc/gps_";

	public static void main(String args[]) throws IOException{

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("20150601");
		subjects.add("20150602");
		subjects.add("20150603");
		subjects.add("20150604");
		subjects.add("20150605");

		for(String ymd : subjects){
			ExtractFile.extractfromcommand(ymd); System.out.println("#done uncompressing "+ymd);
			String unzippedfile = FilePaths.deephomepath(ymd);
			getres(unzippedfile, ymd);
		}
	}

	public static void getres(String file, String ymd) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		String prevline = null;
		int count = 0;
		HashSet<String> set = new HashSet<String>();
		while((line=br.readLine())!= null){
			if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[1];
						set.add(id);
						count++;
					}
				}
				prevline = line;
			}
		}
		System.out.println("number of lines: " +count + ", number of IDs:"+ set.size());
		br.close();
		File i = new File(GPSdeeppath+ymd+".csv");
		i.delete();
	}

}

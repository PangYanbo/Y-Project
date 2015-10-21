package testfiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import DataModify.ExtractFile;
import MainRuns.FilePaths;

public class forTsubo {

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
			getres(unzippedfile);
		}
	}
	
	public static void getres(String file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		int count = 0;
		HashSet<String> set = new HashSet<String>();
		while((line=br.readLine())!= null){
			String[] tokens = line.split("\t");
			String id = tokens[1];
			set.add(id);
			count++;
		}
		System.out.println("number of lines: " +count + ", number of IDs:"+ set.size());
		br.close();
	}

}

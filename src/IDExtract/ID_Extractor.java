package IDExtract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ID_Extractor {

	/*
	 * param
	 * 	args[0] : All Data File
	 * 	args[1] : ID_File
	 * 
	 */
	public static void main(String args[]) throws IOException{
		ID_Extracter(args[0],args[1],args[2]);
	}

	public static void ID_Extracter(String alldata, String IDs, String out) throws IOException{
		HashSet<String> IDmap = intoMap(IDs);
		BufferedReader br = new BufferedReader(new FileReader(new File(alldata)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out),true));
		String line = br.readLine();
		int counter = 0;
		while ((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			if(tokens.length>1){
				String id = tokens[0];
				if(IDmap.contains(id)){
					bw.write(line);
					counter++;
				}
			}
		}
		bw.close();
		br.close();
		System.out.println("Done extracting from " + alldata + ", " + counter +" logs.");
	}

	public static HashSet<String> intoMap(String in) throws IOException{
		HashSet<String> IDmap = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(in)));
		String line = null;
		int count = 0;
		while ((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			IDmap.add(id);
			count++;
		}		
		System.out.println("number of IDs: " + count);
		br.close();
		return IDmap;
	}

}

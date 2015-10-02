package Motif;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class MotifFileModify {

	//id_home + id_office + motif file --> new motif file with home,office attributes 

	public static final String type      = "rain";
	public static final String dir       = "/home/c-tyabe/Data/"+type+"Tokyo4/";
	public static final String subject   = "id_day_motifs";

	public static void main(String args[]) throws NumberFormatException, IOException{

		for(File typelevel : new File(dir).listFiles()){
			for(File datetime :typelevel.listFiles()){
				for(File f : datetime.listFiles()){
					if(f.toString().contains(subject)){
						modifymotiffile(f,new File(datetime.toString()+"/id_home.csv"),
								new File(datetime.toString()+"/id_office.csv"),new File(datetime.toString()+"/id_motif_new.csv"));						
					}
				}
			}
		}
	}

	public static File modifymotiffile(File in, File id_home, File id_office, File out) throws NumberFormatException, IOException{
		HashMap<String, LonLat> idhome = intomap(id_home);
		HashMap<String, LonLat> idoff  = intomap(id_office);

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String day = tokens[2];
			String motif = tokens[3];
			String lon = tokens[4].replace("(", "");
			String lat = tokens[5].replace(")", "");

			bw.write(id + "," + day + "," + motif + "," + lon + "," + lat
					+ "," + idhome.get(id).getLon() + "," + idhome.get(id).getLat() 
					+ "," + idoff.get(id).getLon() + "," + idoff.get(id).getLat());
			bw.newLine();
		}
		br.close();
		bw.close();

		return out;
	}

	public static HashMap<String, LonLat> intomap(File in) throws NumberFormatException, IOException{
		HashMap<String, LonLat> res = new HashMap<String, LonLat>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			LonLat p = new LonLat(Double.parseDouble(tokens[2]),Double.parseDouble(tokens[1]));
			res.put(id, p);
		}
		br.close();
		return res;
	}

}

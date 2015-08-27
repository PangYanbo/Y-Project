package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetRoadData {

	public static String getroaddata
	(HashMap<String, String> smallroad, HashMap<String, String> bigroad, HashMap<String, String> allroad, 
			LonLat now, LonLat home, LonLat office){
		Mesh nowm  = new Mesh(3, now.getLon(),now.getLat());
		Mesh homem = new Mesh(3, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(3, office.getLon(), office.getLat());
		
		String nows = smallroad.get(nowm);
		String homes = smallroad.get(homem);
		String offs = smallroad.get(offm);

		String nowb = bigroad.get(nowm);
		String homeb = bigroad.get(homem);
		String offb = bigroad.get(offm);
		
		String nowa = allroad.get(nowm);
		String homea = allroad.get(homem);
		String offa = allroad.get(offm);
		
		String res =  "num:"+nows +" num:"+homes+" num:"+offs +
				     " num:"+nowb +" num:"+homeb + " num:" + offb+
				     " num:"+nowa +" num:"+homea + " num:" + offa;
		return res;
	}
	
	public static HashMap<String, String> getsmallroad(File in) throws IOException{
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			String pop  = toks[2];
			res.put(mesh, pop);
		}
		br.close();
		return res;
	}
	
	public static HashMap<String, String> getfatroad(File in) throws IOException{
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			String pop  = toks[1];
			res.put(mesh, pop);
		}
		br.close();
		return res;
	}
	
	public static HashMap<String, String> getallroad(File in) throws IOException{
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			String pop  = toks[3];
			res.put(mesh, pop);
		}
		br.close();
		return res;
	}
	
}

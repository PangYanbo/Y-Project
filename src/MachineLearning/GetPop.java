package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetPop {

	//popmap = meshcode - pop
	public static String getpop(HashMap<String, String> popmap, LonLat now, LonLat home, LonLat office){
		Mesh nowm  = new Mesh(5, now.getLon(),now.getLat());
		Mesh homem = new Mesh(5, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(5, office.getLon(), office.getLat());
		
		String nowpop = popmap.get(nowm);
		String homepop = popmap.get(homem);
		String offpop = popmap.get(offm);
		
		String res = "num:"+nowpop +" num:"+homepop+" num:"+offpop;
		return res;
	}
	
	public static HashMap<String, String> getpopmap(File pops) throws IOException{
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
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
	
}

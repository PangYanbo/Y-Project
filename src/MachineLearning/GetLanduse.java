package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetLanduse {

	public static String getlanduse
	(HashMap<String, String> building, HashMap<String, String> farm, LonLat now, LonLat home, LonLat office){
		Mesh nowm  = new Mesh(3, now.getLon(),now.getLat());
		Mesh homem = new Mesh(3, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(3, office.getLon(), office.getLat());
		
		String nowb = building.get(nowm);
		String homeb = building.get(homem);
		String offb = building.get(offm);

		String nowf = farm.get(nowm);
		String homef = farm.get(homem);
		String offf = farm.get(offm);
		
		String res =  "num:"+nowb+" num:"+homeb+" num:"+offb+
				     " num:"+nowf+" num:"+homef+" num:"+offf;
		return res;
	}
	
	public static HashMap<String, String> getmeshbuilding(File pops) throws IOException{
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			String pop  = toks[5];
			res.put(mesh, pop);
		}
		br.close();
		return res;
	}
	
	public static HashMap<String, String> getmeshfarm(File pops) throws IOException{
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

package MachineLearning;

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
		
		String res = "";
		return res;
	}
	
}

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
		Mesh nowm  = new Mesh(3, now.getLon(),now.getLat());
		Mesh homem = new Mesh(3, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(3, office.getLon(), office.getLat());

		System.out.println(nowm);
		
		String nowpop = popmap.get(nowm);
		String homepop = popmap.get(homem);
		String offpop = popmap.get(offm);

		String res = " 4:"+nowpop +" 5:"+homepop+" 6:"+offpop;
		return res;
	}

	public static HashMap<String, String> getpopmap(File pops) throws IOException{
		HashMap<String, Double> temp = new HashMap<String, Double>();
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		Double max = Double.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Double pop  = Double.parseDouble(toks[1]);
			temp.put(mesh, pop);
			if(pop>max){
				max = pop;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			res.put(s, String.valueOf(temp.get(s)/max));
		}
		return res;
	}

}

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
		String nowm  = new Mesh(3, now.getLon(),now.getLat()).getCode();
		String homem = new Mesh(3, home.getLon(), home.getLat()).getCode();
		String offm  = new Mesh(3, office.getLon(), office.getLat()).getCode();

		//		System.out.println(nowm);

		String nowpop = popmap.get(nowm);
		String homepop = popmap.get(homem);
		String offpop = popmap.get(offm);

		String res = getline(nowpop)+","+getline(homepop)+","+getline(offpop);
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

	public static String getline(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.00025){return "1,0,0,0,0";}
			else if ((poprange>=0.00025)&&(poprange<0.001)){return "0,1,0,0,0";}
			else if ((poprange>=0.001)&&(poprange<0.005)){return "0,0,1,0,0";}
			else if ((poprange>=0.005)&&(poprange<0.01)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
}

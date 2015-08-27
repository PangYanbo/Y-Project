package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class GetTrainData {

	public static String getstationpop(HashMap<LonLat, Integer> popmap, LonLat now, LonLat home, LonLat office){

		String nowpop = getnearestStationPop(popmap,now);
		String homepop = getnearestStationPop(popmap,home);
		String offpop = getnearestStationPop(popmap,office);

		String res = "num:"+nowpop +" num:"+homepop+" num:"+offpop;
		return res;
	}

	public static HashMap<LonLat, Integer> getpopmap(File pops) throws IOException{
		HashMap<LonLat, Integer> res = new HashMap<LonLat, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			Integer pop  = Integer.valueOf(toks[3]);
			LonLat point = new LonLat(Double.parseDouble(toks[4]),Double.parseDouble(toks[5]));
			res.put(point, pop);
		}
		br.close();
		return res;
	}

	public static String getnearestStationPop(HashMap<LonLat, Integer> map, LonLat p){
		Integer pop = 0;
		for(LonLat sta : map.keySet()){
			if(sta.distance(p)<5000){
				if(map.get(sta)>pop){
					pop = map.get(sta);
				}
			}
		}
		return String.valueOf(pop);
	}

}

package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class GetLandPrice {

	public static String getlandprice(HashMap<LonLat, Integer> pricemap, LonLat now, LonLat home, LonLat office){

		String nowpop = getnearestprice(pricemap,now);
		String homepop = getnearestprice(pricemap,home);
		String offpop = getnearestprice(pricemap,office);

		String res = "num:"+nowpop +" num:"+homepop+" num:"+offpop;
		return res;
	}

	public static HashMap<LonLat, Integer> getpricemap(File in) throws IOException{
		HashMap<LonLat, Integer> res = new HashMap<LonLat, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			Integer price  = Integer.valueOf(toks[0]);
			LonLat point = new LonLat(Double.parseDouble(toks[1]),Double.parseDouble(toks[2]));
			res.put(point, price);
		}
		br.close();
		return res;
	}

	public static String getnearestprice(HashMap<LonLat, Integer> map, LonLat p){
		Double dis = Double.MAX_VALUE;
		Integer price = 0;
		for(LonLat point : map.keySet()){
			if(point.distance(p)<dis){
				price = map.get(point);
				dis = point.distance(p);
			}
		}
		return String.valueOf(price);
	}

	
}

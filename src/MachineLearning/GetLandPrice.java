package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class GetLandPrice {

	public static String getlandprice(HashMap<LonLat, String> pricemap, LonLat now, LonLat home, LonLat office){

		String nowpop = getnearestprice(pricemap,now);
		String homepop = getnearestprice(pricemap,home);
		String offpop = getnearestprice(pricemap,office);

		String res = " 25:"+nowpop +" 26:"+homepop+" 27:"+offpop;
		return res;
	}

	public static HashMap<LonLat, String> getpricemap(File in) throws IOException{
		HashMap<LonLat, Integer> temp = new HashMap<LonLat, Integer>();
		HashMap<LonLat, String> res = new HashMap<LonLat, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			Integer price  = Integer.valueOf(toks[0]);
			LonLat point = new LonLat(Double.parseDouble(toks[1]),Double.parseDouble(toks[2]));
			temp.put(point, price);
			if(price>max){
				max = price;
			}
		}
		br.close();
		
		for(LonLat p : temp.keySet()){
			BigDecimal x = new BigDecimal((double)temp.get(p)/max);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			res.put(p, String.valueOf(x));
		}
		return res;
	}

	public static String getnearestprice(HashMap<LonLat, String> map, LonLat p){
		Double dis = Double.MAX_VALUE;
		Double price = 0d;
		for(LonLat point : map.keySet()){
			if(point.distance(p)<dis){
				price = Double.parseDouble(map.get(point));
				dis = point.distance(p);
			}
		}
		return String.valueOf(price);
	}

	
}

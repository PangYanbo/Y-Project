package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class GetTrainData {

	public static String getstationpop(HashMap<LonLat, String> popmap, LonLat now, LonLat home, LonLat office){

		String nowpop = getpopofnearestStation(popmap,now);
		String homepop = getpopofnearestStation(popmap,home);
		String offpop = getpopofnearestStation(popmap,office);

		String res = getline(nowpop)+","+getline(homepop)+","+getline(offpop);
		return res;
	}

	public static HashMap<LonLat, String> getpopmap(File pops) throws IOException{
		HashMap<LonLat, Integer> temp = new HashMap<LonLat,Integer>();
		HashMap<LonLat, String> res = new HashMap<LonLat, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			Integer pop  = Integer.valueOf(toks[3]);
			LonLat point = new LonLat(Double.parseDouble(toks[4]),Double.parseDouble(toks[5]));
			temp.put(point, pop);
			if(pop>max){
				max = pop;
			}
		}
		br.close();
		
		for(LonLat s : temp.keySet()){
			BigDecimal x = new BigDecimal((double)temp.get(s)/(double)max);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			res.put(s, String.valueOf(x));
		}
		return res;
	}

	public static String getpopofnearestStation(HashMap<LonLat, String> map, LonLat p){
		Double pop = 0d;
		for(LonLat sta : map.keySet()){
			if(sta.distance(p)<5000){
				if(Double.parseDouble(map.get(sta))>pop){
					pop = Double.parseDouble(map.get(sta));
				}
			}
		}
		return String.valueOf(pop);
	}

	public static String getline(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.05){return "1,0,0,0,0";}
			else if ((poprange>=0.05)&&(poprange<0.1)){return "0,1,0,0,0";}
			else if ((poprange>=0.1)&&(poprange<0.3)){return "0,0,1,0,0";}
			else if ((poprange>=0.3)&&(poprange<0.6)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
}

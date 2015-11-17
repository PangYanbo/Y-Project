package MachineLearning;

import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class BilinearFeatures {

	/*
	 * level x citi-ness of home     1000XX ok
	 * level x citi-ness of office   2000XX ok
	 * level x time                  3000XX ok
	 * level x landprice             4000XX ok
	 * level x distance              5000XX ok
	 * time  x distance              6000XX ok
 	 * 
	 */
	
	/*
	 * 3 digits: normal features
	 * 5 digits: area codes
	 * 6 digits: for bilinear codes
	 * 
	 */
	
	public static String bilinearline(String level, String time, String distance, LonLat homep, LonLat offp, 
			HashMap<String, String> popmap, HashMap<LonLat, String> pricemap){
				
		String levelhpop  = "1000"+level(level)+citypop(homep,popmap);
		String levelopop  = "2000"+level(level)+citypop(offp,popmap);
		String leveltime  = "3000"+level(level)+time(time);
		String levelprice = "4000"+level(level)+landprice(homep,pricemap);
		String leveldis   = "5000"+level(level)+distance(distance);
		String timedis    = "6000"+time(time)+distance(distance);
		String res = " "+levelhpop+":1 "+levelopop+":1 "+leveltime+":1 "+levelprice+":1 "+leveldis+":1 "+timedis+":1";		
		return res;
	}
	
	public static String time(String time){
		Double timerange = Double.parseDouble(time);
		if(timerange<6){return "1";}
		else if ((timerange>=6)&&(timerange<10)){return "2";}
		else if ((timerange>=10)&&(timerange<16)){return "3";}
		else if ((timerange>=16)&&(timerange<20)){return "4";}
		else{return "5";}
	}
	
	public static String level(String level){
		if(level.equals("1")){return "1";}
		else if(level.equals("2")){return "2";}
		else if(level.equals("3")){return "3";}
		else if(level.equals("4")){return "4";}
		else{return "5";}
	}
	
	public static String distance(String distance){
		Double dis = Double.parseDouble(distance);
		if(dis<0.01){return "1";}
		else if ((dis>=0.01)&&(dis<0.05)){return "2";}
		else if ((dis>=0.05)&&(dis<0.25)){return "3";}
		else if ((dis>=0.25)&&(dis<0.40)){return "4";}
		else{return "5";}
	}
	
	public static String landprice(LonLat point, HashMap<LonLat, String> pricemap){
		String price = GetLandPrice.getnearestprice(pricemap,point);
		Double priced = Double.parseDouble(price);
		if(priced<0.0015){return "1";}
		else if ((priced>=0.0015)&&(priced<0.003)){return "2";}
		else if ((priced>=0.003)&&(priced<0.01)){return "3";}
		else if ((priced>=0.01)&&(priced<0.05)){return "4";}
		else{return "5";}
	}
	
	public static String citypop(LonLat point, HashMap<String, String> popmap){
		String nowm  = new Mesh(3, point.getLon(),point.getLat()).getCode();
		String pop = popmap.get(nowm);
		Double poprange = Double.parseDouble(pop);
		if(poprange<0.00025){return "1";}
		else if ((poprange>=0.00025)&&(poprange<0.001)){return "2";}
		else if ((poprange>=0.001)&&(poprange<0.005)){return "3";}
		else if ((poprange>=0.005)&&(poprange<0.01)){return "4";}
		else{return "5";}
	}
}

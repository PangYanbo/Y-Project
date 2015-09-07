package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetRoadData {

	public static String getroaddata
	(HashMap<String, String> smallroad, HashMap<String, String> bigroad, HashMap<String, String> allroad, 
			LonLat now, LonLat home, LonLat office){
		String nowm  = new Mesh(3, now.getLon(),now.getLat()).getCode();
		String homem = new Mesh(3, home.getLon(), home.getLat()).getCode();
		String offm  = new Mesh(3, office.getLon(), office.getLat()).getCode();
		
		String nows = smallroad.get(nowm);
		String homes = smallroad.get(homem);
		String offs = smallroad.get(offm);

		String nowb = bigroad.get(nowm);
		String homeb = bigroad.get(homem);
		String offb = bigroad.get(offm);
		
		String nowa = allroad.get(nowm);
		String homea = allroad.get(homem);
		String offa = allroad.get(offm);
		
		String res = getlines(nows) +","+getlines(homes)+","+getlines(offs) +
				     ","+getlineb(nowb) +","+getlineb(homeb)+","+ getlineb(offb)+
				     ","+getlinea(nowa) +","+getlinea(homea)+","+ getlinea(offa);
		return res;
	}
	
	public static HashMap<String, String> getsmallroad(File in) throws IOException{
		HashMap<String, Integer> temp = new HashMap<String, Integer>(); 
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Integer pop  = Integer.valueOf(toks[2]);
			temp.put(mesh, pop);
			if(pop>max){
				max = pop;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			BigDecimal x = new BigDecimal((double)temp.get(s)/(double)max);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			res.put(s, String.valueOf(x));
		}
		return res;
	}
	
	public static HashMap<String, String> getfatroad(File in) throws IOException{
		HashMap<String, Integer> temp = new HashMap<String, Integer>(); 
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Integer pop  = Integer.valueOf(toks[1]);
			temp.put(mesh, pop);
			if(pop>max){
				max = pop;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			BigDecimal x = new BigDecimal((double)temp.get(s)/(double)max);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			res.put(s, String.valueOf(x));
		}
		return res;
	}
	
	public static HashMap<String, String> getallroad(File in) throws IOException{
		HashMap<String, Integer> temp = new HashMap<String, Integer>(); 
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Integer pop  = Integer.valueOf(toks[3]);
			temp.put(mesh, pop);
			if(pop>max){
				max = pop;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			BigDecimal x = new BigDecimal((double)temp.get(s)/(double)max);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			res.put(s, String.valueOf(x));
		}
		return res;
	}
	
	public static String getlines(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.2){return "1,0,0,0,0";}
			else if ((poprange>=0.2)&&(poprange<0.4)){return "0,1,0,0,0";}
			else if ((poprange>=0.4)&&(poprange<0.6)){return "0,0,1,0,0";}
			else if ((poprange>=0.6)&&(poprange<0.8)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	public static String getlineb(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.1){return "1,0,0,0,0";}
			else if ((poprange>=0.1)&&(poprange<0.2)){return "0,1,0,0,0";}
			else if ((poprange>=0.2)&&(poprange<0.3)){return "0,0,1,0,0";}
			else if ((poprange>=0.3)&&(poprange<0.45)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	public static String getlinea(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.15){return "1,0,0,0,0";}
			else if ((poprange>=0.15)&&(poprange<0.3)){return "0,1,0,0,0";}
			else if ((poprange>=0.3)&&(poprange<0.5)){return "0,0,1,0,0";}
			else if ((poprange>=0.5)&&(poprange<0.65)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	
}

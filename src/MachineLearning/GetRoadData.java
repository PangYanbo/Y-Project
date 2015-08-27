package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetRoadData {

	public static String getroaddata
	(HashMap<String, String> smallroad, HashMap<String, String> bigroad, HashMap<String, String> allroad, 
			LonLat now, LonLat home, LonLat office){
		Mesh nowm  = new Mesh(3, now.getLon(),now.getLat());
		Mesh homem = new Mesh(3, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(3, office.getLon(), office.getLat());
		
		String nows = smallroad.get(nowm);
		String homes = smallroad.get(homem);
		String offs = smallroad.get(offm);

		String nowb = bigroad.get(nowm);
		String homeb = bigroad.get(homem);
		String offb = bigroad.get(offm);
		
		String nowa = allroad.get(nowm);
		String homea = allroad.get(homem);
		String offa = allroad.get(offm);
		
		String res = " 13:"+nows +" 14:"+homes+" 15:"+offs +
				     " 16:"+nowb +" 17:"+homeb+" 18:"+ offb+
				     " 19:"+nowa +" 20:"+homea+" 21:"+ offa;
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
			res.put(s, String.valueOf((double)temp.get(s)/(double)max));
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
			res.put(s, String.valueOf((double)temp.get(s)/(double)max));
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
			res.put(s, String.valueOf((double)temp.get(s)/(double)max));
		}
		return res;
	}
	
}

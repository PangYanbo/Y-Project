package MachineLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GetLanduse {

	public static String getlanduse
	(HashMap<String, String> building, HashMap<String, String> farm, LonLat now, LonLat home, LonLat office){
		Mesh nowm  = new Mesh(3, now.getLon(),now.getLat());
		Mesh homem = new Mesh(3, home.getLon(), home.getLat());
		Mesh offm  = new Mesh(3, office.getLon(), office.getLat());
		
		String nowb = building.get(nowm);
		String homeb = building.get(homem);
		String offb = building.get(offm);

		String nowf = farm.get(nowm);
		String homef = farm.get(homem);
		String offf = farm.get(offm);
		
		String res = " 7:"+nowb+" 8:"+homeb+" 9:"+offb+
				     " 10:"+nowf+" 11:"+homef+" 12:"+offf;
		return res;
	}
	
	public static HashMap<String, String> getmeshbuilding(File pops) throws IOException{
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Integer area  = Integer.valueOf(toks[5]);
			temp.put(mesh, area);
			if(area>max){
				max = area;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			res.put(s, String.valueOf((double)temp.get(s)/(double)max));
		}
		return res;
	}
	
	public static HashMap<String, String> getmeshfarm(File pops) throws IOException{
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		HashMap<String, String> res = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(pops));
		String line = null;
		Integer max = Integer.MIN_VALUE;
		while((line=br.readLine())!=null){
			String[] toks = line.split(",");
			String mesh = toks[0];
			Integer area  = Integer.valueOf(toks[1]);
			temp.put(mesh, area);
			if(area>max){
				max = area;
			}
		}
		br.close();
		
		for(String s : temp.keySet()){
			res.put(s, String.valueOf((double)temp.get(s)/(double)max));
		}
		return res;
	}
	
	
}

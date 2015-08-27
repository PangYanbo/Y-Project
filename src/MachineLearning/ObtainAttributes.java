package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class ObtainAttributes {

	
	
	public static void getAttributes(File in, File out, String level, String time) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");

			if(tokens.length==8){ // output version 1 
				String id = tokens[0];
				String diff = tokens[1];
				String nowzone = tokens[2];
				String homezone = tokens[3];
				String dis = tokens[4];
				LonLat nowp = StringtoLonLat(tokens[5]);
				LonLat homep = StringtoLonLat(tokens[6]);
				LonLat officep = StringtoLonLat(tokens[7]);
				
				bw.write(diff + ", rain," + level + "," + time + "," );
				
			}
			else if(tokens.length==11){ // output version 2
				String id = tokens[0];
				String diff = tokens[1];
				String nowzone = tokens[2];
				String homezone = tokens[3];
				String dis = tokens[4];
				LonLat nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
				LonLat homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
				LonLat officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
			}
			else{
				System.out.println("#number of tokens in line is invalid");
			}
			

			
			
		}
		br.close();
		bw.close();
	}

	public static String locationinfo
	(LonLat p,HashMap<String,String> landusefile,HashMap<String,String> roadnwfile,
			HashMap<String,String> landprice,HashMap<String,String> stations){
		String res = getLanduse(p,landusefile)+","+getRoadnetwork(p,roadnwfile)+","+
			getlandprice(p,landprice)+","+getStationID(p,stations);
		return res;
	}
	
	//3次メッシュから取得
	public static String getLanduse(LonLat point, HashMap<String,String> landusefile){
		String landuse = null;

		return landuse;
	}

	//3次メッシュから取得
	public static String getRoadnetwork(LonLat point, HashMap<String,String> roadnwfile){
		String roadnw = null;

		return roadnw;
	}

	//ポイントから検索か？
	public static String getlandprice(LonLat point, HashMap<String,String> landprice){
		String landp = null;

		return landp;		
	}

	//
	public static String getStationID(LonLat point, HashMap<String,String> stations){
		String stationID = null;
		return stationID;
	}

	public static LonLat StringtoLonLat(String x){
		String[] tokens = x.split(",");
		String slon = tokens[0].replace("(", "");
		String slat = tokens[1].replace(")", "");
		Double lon = Double.parseDouble(slon);
		Double lat = Double.parseDouble(slat);
		LonLat p = new LonLat(lon,lat);
		return p;
	}
}

package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class ObtainAttributes {

	public static void getAttributes(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			String diff = tokens[1];
			String nowzone = tokens[2];
			String homezone = tokens[3];
			String dis = tokens[4];

			//暫定
			LonLat nowp = StringtoLonLat(tokens[5]);
			LonLat homep = StringtoLonLat(tokens[6]);
			LonLat officep = StringtoLonLat(tokens[7]);

			//new version
//			LonLat nowp = new LonLat(Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
//			LonLat homep = new LonLat(Double.parseDouble(tokens[7]),Double.parseDouble(tokens[8]));
//			LonLat officep = new LonLat(Double.parseDouble(tokens[9]),Double.parseDouble(tokens[10]));
			
			
			
		}
		br.close();
		bw.close();
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
	
//	public static void main(String args[]){
//		String x = "(133.5,42.4)";
//		LonLat y = StringtoLonLat(x);
//		System.out.println(y.getLon());
//		System.out.println(y);
//	}
}

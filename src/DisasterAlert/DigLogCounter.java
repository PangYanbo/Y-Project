//package DisasterAlert;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class DigLogCounter {
//
//	protected static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");//change time format
//	
//	public static void dislogcount(File in, File out) throws IOException, ParseException{
//		BufferedReader br = new BufferedReader(new FileReader(in));
//		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
//		String line = br.readLine();
//		while((line = br.readLine())!= null){
//			String[] tokens = line.split(",");
//			String[] ymd = tokens[0].split("/");
//			String daytime = ymd[2];
//			String[] d_t = daytime.split(" ");
//			String dt = ymd[0]+"-"+ymd[1]+"-"+d_t[0];
//			Date disdate = YMD.parse(dt);
//			String type = tokens[1];
//			
//		}
//		br.close();
//		bw.close();
//	}
//
//}

package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class DisLogDecider {

	protected static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");//change time format

	public static void main(String args[]) throws IOException, ParseException{
		File in = new File("c:/users/c-tyabe/desktop/DisasterDataLogs/DisasterAlertData.csv");
		File out = new File("c:/users/c-tyabe/desktop/DisasterDataLogs/DisasterAlertData_shutoken_eq.csv");
		File jiscodes = new File("c:/users/c-tyabe/desktop/ShutokenSHP/JIScodes_pref.csv");
		choosebyAreaDateType(in,out,jiscodes,"emg1","2014-10-21","2015-08-17");		
	}

	public static File choosebyAreaDateType(File in, File out, File codes, String t, String date, String enddate) throws IOException, ParseException{
		BufferedReader br2 = new BufferedReader(new FileReader(codes));
		HashSet<String> JISset = new HashSet<String>();
		String line2 = null;
		while((line2=br2.readLine())!=null){
			String[] tokens = line2.split(",");
			String code = tokens[0];
			JISset.add(code);
		}
		br2.close();

		Date startdate = YMD.parse(date);
		Date enddat    = YMD.parse(enddate);

		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = br.readLine();
		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");

			String[] ymd = tokens[0].split("/");
			String daytime = ymd[2];
			String[] d_t = daytime.split(" ");
			String dt = ymd[0]+"-"+ymd[1]+"-"+d_t[0];
			Date disdate = YMD.parse(dt);

			String type = tokens[1];
			if((disdate.after(startdate))&&(disdate.before(enddat))){
				if(type.equals(t)){
					String[] jiscodes = tokens[3].split(" ");
					if(jiscodes[0].equals("ALL")){
						bw.write(ymd +","+ type +","+tokens[2] +","+"8 11 12 13 14");
						bw.newLine();
					}
					else{
						HashSet<String> temp = new HashSet<String>();
						for(String jiscode : jiscodes){
							if(JISset.contains(jiscode)){
								temp.add(jiscode);
							}
						}
						String c = arraytostr(temp);
						bw.write(ymd +","+ type +","+tokens[2] +","+ c);
						bw.newLine();
					}
				}
			}
		}
		br.close();
		bw.close();
		System.out.println("number of disasters in shutoken : " + count);
		return out;
	}
	
	public static String arraytostr(HashSet<String> temp){
		String tmp = temp.toString();
		String tmp2 = tmp.replace("[", "");
		String tmp3 = tmp2.replace("]", "");
		String tmp4 = tmp3.replace(",", "");
		return tmp4;
	}
	
//	public static void main(String args[]){
//		HashSet<String> temp = new HashSet<String>();
//		temp.add("11");
//		temp.add("13");
//		temp.add("12");
//		temp.add("2");
//		
//		String res = arraytostr(temp);
//		System.out.println(res);
//	}

}

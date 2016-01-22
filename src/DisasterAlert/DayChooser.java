package DisasterAlert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class DayChooser {

	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd");//change time format

	public static HashSet<Date> getDisDays(File dislogs) throws IOException, ParseException{
		HashSet<Date> res = new HashSet<Date>();
		BufferedReader br = new BufferedReader(new FileReader(dislogs));
		String line = null;
		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");
			String[] ymd = tokens[0].split("/");
			String year = ymd[0];
			String month = ymd[1];
			String daytime = ymd[2];
			String[] d_t = daytime.split(" ");
			String day = d_t[0];

			Date d = SDF_TS.parse("2014-10-20");
			Date d2 = SDF_TS.parse("2015-06-19");
			Date date = SDF_TS.parse(year+"-"+month+"-"+day);
			if((date.after(d))&&(date.before(d2))){
				res.add(date);
			}
		}
		br.close();
		return res;
	}

	public static HashSet<String> getTargetDates(String disDate, String dislog) 
			throws ParseException, IOException{
		File dislogs = new File(dislog);
		HashSet<String> res = new HashSet<String>();
		HashSet<Date> DisDays = getDisDays(dislogs);
		String year = disDate.substring(0,4);
		String month = disDate.substring(4,6);
		String nextmonth = String.valueOf(Integer.valueOf(month)-1);
//		String nextnmonth = String.valueOf(Integer.valueOf(month)+2);
		for(int i=1; i<=28; i++){
			String day = String.valueOf(i);
			Date d = SDF_TS.parse(year+"-"+month+"-"+day);
			String youbi = (new SimpleDateFormat("u")).format(d);
			if(!((youbi.equals("6"))||(youbi.equals("7")))){
				if(!(DisDays.contains(d))){
					String date = SDF_TS.format(d);
					res.add(date);
				}
			}
			Date d2 = SDF_TS.parse(year+"-"+nextmonth+"-"+day);
			String youbi2 = (new SimpleDateFormat("u")).format(d2);
			if(!((youbi2.equals("6"))||(youbi2.equals("7")))){
				if(!(DisDays.contains(d2))){
					String date2 = SDF_TS.format(d2);
					res.add(date2);
				}
			}
//			Date d3 = SDF_TS.parse(year+"-"+nextnmonth+"-"+day);
//			String youbi3 = (new SimpleDateFormat("u")).format(d2);
//			if(!((youbi3.equals("6"))||(youbi3.equals("7")))){
//				if(!(DisDays.contains(d3))){
//					String date3 = SDF_TS.format(d3);
//					res.add(date3);
//				}
//			}
		}
		return res;
	}

	public static HashSet<String> getTargetDates2(String disDate) throws ParseException, IOException{
		HashSet<String> res = new HashSet<String>();
		String year = disDate.substring(0,4);
		for(int m=1; m<=7; m++){
			String month = String.valueOf(m);
			for(int i=1; i<=28; i++){
				String day = String.valueOf(i);
				Date d = SDF_TS.parse(year+"-"+month+"-"+day);
				String youbi = (new SimpleDateFormat("u")).format(d);
				if(!((youbi.equals("6"))||(youbi.equals("7")))){
					String date = SDF_TS.format(d);
					res.add(date);
				}
			}
		}
		return res;
	}
}

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
			Date date = SDF_TS.parse(year+"-"+month+"-"+day);
			if(date.after(d)){
				res.add(date);
			}
		}
		br.close();
		return res;
	}

	public static HashSet<Date> getTargetDates(String disDate, File dislogs) throws ParseException, IOException{
		HashSet<Date> res = new HashSet<Date>();
		HashSet<Date> DisDays = getDisDays(dislogs);
		String year = disDate.substring(0,4);
		String month = disDate.substring(4,6);
		for(int i=1; i<=28; i++){
			String day = String.valueOf(i);
			Date d = SDF_TS.parse(year+"-"+month+"-"+day);
			String youbi = (new SimpleDateFormat("u")).format(d);
			if(!((youbi.equals("6"))||(youbi.equals("7")))){
				if(!(DisDays.contains(d))){
					res.add(d);
					if(res.size()==15){
						break;
					}
				}
			}
		}
		return res;		
	}

}

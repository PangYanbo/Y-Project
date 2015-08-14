package MainRuns;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataModify.ExtractFile;
import DisasterAlert.DayChooser;
import DisasterAlert.DisasterLogs;
import DisasterAlert.ExtractIDbyDate;
import MobilityAnalyser.MovementAnalyzer;
import Motif.MotifFinder2;
import StayPointDetection.HomeDetector;
import StayPointDetection.OfficeSchoolDetection;

public class YDisasterProject {

	/**
	 * Class for running every step 
	 * @param 
	 * args[0] : disaster type (dosha,earthquake,eew,flood,heats,rain,tsunami,volc)
	 * args[1] : number of towns affected
	 * 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * 
	 */

	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd");//change time format
		
	private static final String type = "rain";
	private static final String homepath = "/home/c-tyabe/Data/"+type+"Tokyo/";
	private static final String GPSpath  = "/tmp/bousai_data/gps_";

	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{
		File dir = new File(homepath);
		dir.mkdir();
		
		String disasterlogfile = "/home/c-yabe/Data/DisasterLogs/DisasterAlertData_shutoken_"+type+".csv";
		runforallevents(disasterlogfile);
	}

	
	public static void runforallevents(String dislog) throws IOException, NumberFormatException, ParseException{
		HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> dislogs = DisasterLogs.sortLogs(dislog);
		
		int count = 0;
		for(String ymd : dislogs.keySet()){
			for(String time : dislogs.get(ymd).keySet()){
				for(String level : dislogs.get(ymd).get(time).keySet()){
					ArrayList<String> codes = dislogs.get(ymd).get(time).get(level);
					run(codes, ymd, time, level, dislog);
					count++;
					if(count==1){
						break;
					}
				}
			}
		}
	}

	public static void run(ArrayList<String> zones, String ymd, String time, String level, String dislog) throws IOException, NumberFormatException, ParseException{
		String workpath = homepath+"/"+type+"_"+level+"/"+ymd+"_"+time+"/";
		File dir = new File(workpath);
		dir.mkdir();

		String disGPS = GPSpath+ymd+".tar.gz"; //ymd=yyyymmdd‚ÌŒ`‚É‚È‚Á‚Ä‚¢‚é
		ExtractFile.extract(disGPS);
		
		String unzippedfile = FilePaths.deephomepath(ymd);
		HashSet<String> targetIDs = ExtractIDbyDate.extractID(unzippedfile,time,zones,10); //10: minimum logs
		System.out.println("#the number of IDs for " + ymd+time+ " is " + targetIDs.size());
		File i = new File(unzippedfile);
		i.delete();

		String dataforexp = workpath+"dataforexp.csv";
		HashSet<String> targetdays = DayChooser.getTargetDates(ymd, dislog); System.out.println("#the nuumber of days are " + targetdays.size());
		Makedata4exp.makedata(dataforexp, targetdays, targetIDs);

		HomeDetector.getHome(dataforexp, ymd, type);
		OfficeSchoolDetection.getOfficeSchool(dataforexp, ymd, type);

		MovementAnalyzer.executeAnalyser
		(dataforexp, FilePaths.dirfile(dir.toString(),"id_home.csv"), 
				FilePaths.dirfile(dir.toString(),"id_office.csv"), dir.toString());

		MotifFinder2.executeMotif(dataforexp, ymd, type);

	}
}

package MainRuns;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataModify.AreaChecker;
import DisasterAlert.DisasterLogs;
import DisasterAlert.ExtractIDbyDate;
import IDExtract.ID_Extractor;
import MobilityAnalyser.MovementAnalyzer;
import Motif.MotifFinder2;
import StayPointDetection.HomeDetector;
import StayPointDetection.OfficeSchoolDetection;
import ToolsforFileManagement.SlashDelete;

public class YDisasterProject {

	/**
	 * Class for running every step 
	 * @param 
	 * args[0] : disaster logs filepath
	 * args[1] : number of towns affected
	 * 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * 
	 */

	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{
		runforallevents(args[0],5);
	}

	public static void runforallevents(String dislog, Integer codenum) throws IOException, NumberFormatException, ParseException{
		HashMap<String, ArrayList<String>> date_code = DisasterLogs.date_codemap(dislog);
		for(String day : date_code.keySet()){
			if(date_code.get(day).size()>=codenum){ //TODO change number of Žs’¬‘º
				String yyyymmdd = SlashDelete.excludeslash(day);
				String filepath = "/tmp/bousai_data/gps_" + yyyymmdd + ".tar.gz";
				run(date_code.get(day), filepath, yyyymmdd);
			}
		}
	}

	public static void run(ArrayList<String> zones, String zipfilepath, String ymd) throws IOException, NumberFormatException, ParseException{
		//unzip filepath
		String unzip = "/home/c-tyabe/Data/gps_" + ymd + ".csv";
		HashSet<String> targetIDs = ExtractIDbyDate.extractID(unzip,zones);
		ID_Extractor.ID_Extracter(unzip,targetIDs);
		String dataforexp = "/home/c-tyabe/Data/new_gps_" + ymd + ".csv";
		AreaChecker.WriteonlyGcheckedLogs(unzip, dataforexp);
		System.out.println("#got the data for exp for " + ymd);
		
		HomeDetector.getHome(dataforexp, ymd);
		OfficeSchoolDetection.getOfficeSchool(dataforexp, ymd);
		File dir = new File("/home/c-tyabe/Data/"+ymd+"/");
		dir.mkdir();
		
		MovementAnalyzer.executeAnalyser
		(dataforexp, "/home/c-tyabe/Data/id_home_"+ymd+".csv", "/home/c-tyabe/Data/id_office_"+ ymd +".csv", "/home/c-tyabe/Data/"+ymd+"/");
		
		MotifFinder2.executeMotif(dataforexp, ymd);
	}

}

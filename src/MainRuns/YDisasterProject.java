package MainRuns;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import DataModify.AreaChecker;
import DataModify.ExtractFile;
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
	 * args[0] : disaster type (dosha,earthquake,eew,flood,heats,rain,tsunami,volc)
	 * args[1] : number of towns affected
	 * 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * 
	 */

	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd");//change time format
	
	public static void runforallevents(String dislog, Integer codenum, String type) throws IOException, NumberFormatException, ParseException{
		HashMap<String, ArrayList<String>> date_code = DisasterLogs.date_codemap(dislog);
		for(String day : date_code.keySet()){
			if(date_code.get(day).size()>=codenum){ //TODO change number of Žs’¬‘º
				String yyyymmdd = SlashDelete.excludeslash(day);
				String filepath = FilePaths.tmppath(yyyymmdd);
				run(date_code.get(day), filepath, yyyymmdd, type);
				//run for normal days here
			}
		}
	}

	public static void run(ArrayList<String> zones, String zipfilepath, String ymd, String type) throws IOException, NumberFormatException, ParseException{
		File dir = new File(FilePaths.homedir(type+ymd));
		dir.mkdir();
		
		ExtractFile.extract(zipfilepath);
		String unzip = FilePaths.deephomepath(ymd);
		HashSet<String> targetIDs = ExtractIDbyDate.extractID(unzip,zones);
		ID_Extractor.ID_Extracter(unzip,targetIDs,ymd);
		String extracted = FilePaths.deephomepath(ymd+"extr");
		
		String dataforexp = FilePaths.dirfile(dir.toString(), "newgps"+ymd+".csv");
		AreaChecker.WriteonlyGcheckedLogs(extracted, dataforexp);
		System.out.println("#got the dis data for exp for " + ymd);
		
		Date disday = SDF_TS.parse(SlashDelete.slashtodash(ymd));
		
		
		HomeDetector.getHome(dataforexp, ymd, type);
		OfficeSchoolDetection.getOfficeSchool(dataforexp, ymd, type);
		
		MovementAnalyzer.executeAnalyser
		(dataforexp, FilePaths.dirfile(FilePaths.homedir(type+ymd),"id_home_"+ymd+".csv"), 
				FilePaths.dirfile(FilePaths.homedir(type+ymd),"id_office_"+ymd+".csv"), dir.toString());
		
		MotifFinder2.executeMotif(dataforexp, ymd, type);
		
		File i = new File(unzip);
		i.delete();
		File j = new File(extracted);
		j.delete();
	}
	
	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{
		String type = args[0];
		String disasterlogfile = "/home/c-yabe/Data/DisasterLogs/"+type+".csv";
		runforallevents(disasterlogfile,5,type);
	}

}

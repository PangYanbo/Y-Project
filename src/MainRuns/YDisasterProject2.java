//package MainRuns;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//
//import jp.ac.ut.csis.pflow.geom.LonLat;
//import DataModify.ExtractFile;
//import DisasterAlert.DayChooser;
//import DisasterAlert.DisasterLogs;
//import DisasterAlert.ExtractIDbyDate;
//import MobilityAnalyser.MovementAnalyzer;
//import Motif.MotifFinder2;
//import StayPointDetection.HomeDetector;
//import StayPointDetection.OfficeSchoolDetection;
//
//public class YDisasterProject2 {
//
//	/**
//	 * Class for running every step 
//	 * @param 
//	 * args[0] : disaster type (emg1)
//	 * args[1] : number of towns affected
//	 * 
//	 * @throws IOException 
//	 * @throws ParseException 
//	 * @throws NumberFormatException 
//	 * 
//	 */
//
//	// for disasters with 県コード（2桁）！(prefecture codes!)
//
//	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("yyyy-MM-dd");//change time format
//
//	private static final String type = "eq";
//	private static final String city = "Tokyo";
//	private static final String homepath = "/home/c-tyabe/Data/"+type+city+"6/";
//	private static final String GPSpath  = "/tmp/bousai_data/gps_";
//
//	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{
//		File dir = new File(homepath);
//		dir.mkdir();
//
//		String disasterlogfile = "/home/c-tyabe/Data/DisasterLogs/DisasterAlertData_shutoken_"+type+".csv";
//		runforallevents(disasterlogfile);
//	}
//
//
//	public static void runforallevents(String dislog) throws IOException, NumberFormatException, ParseException{
//		HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> dislogs = DisasterLogs.sortLogs(dislog);
//		int c = 0;
//		for(String day : dislogs.keySet()){
//			for(String time : dislogs.get(day).keySet()){
//				c = c + dislogs.get(day).get(time).size();
//			}
//		}
//		System.out.println("#successfully sorted out disaster info logs... there are " + c);
//
//		int count = 0;
//		for(String ymd : dislogs.keySet()){
//			for(String time : dislogs.get(ymd).keySet()){
//				for(String level : dislogs.get(ymd).get(time).keySet()){
//					if(filedoublechecker(ymd,time,type,level,city)==true){
//						//						if(ymd.equals("20150521")){
//						System.out.println("#starting run for " + ymd +", time: "+ time + ", level:" +level);
//						ArrayList<String> codes = dislogs.get(ymd).get(time).get(level);
//						run(codes, ymd, time, level, dislog);
//						System.out.println("------------------done " + count + " disasters------------------");
//						System.out.println("                ");
//						//						}
//					}
//					count++;
//				}
//			}
//		}
//	}
//
//	public static boolean filedoublechecker(String ymd, String time, String type, String level, String city){
//		File file = new File(homepath+type+"_"+level+"/"+ymd+"_"+time);
//		if(file.exists()){
//			return false;
//		}
//		else{
//			return true;
//		}
//	}
//
//	public static void run(ArrayList<String> zones, String ymd, String time, String level, String dislog) throws IOException, NumberFormatException, ParseException{
//		System.out.println("start run for " + zones.size() +" zones"); System.out.println("zones are " + zones);
//		String disasterday = ymd.substring(4,6);
//
//		String wpath = homepath+"/"+type+"_"+level+"/"; File dir2 = new File(wpath); dir2.mkdir();
//		String workpath = homepath+"/"+type+"_"+level+"/"+ymd+"_"+time+"/"; File dir = new File(workpath); dir.mkdir();
//
//		String disGPS = GPSpath+ymd+".tar.gz"; //ymd=yyyymmddの形になっている
//
//		if(new File(disGPS).exists()){ //もしログのファイルがあれば！
//			ExtractFile.extractfromcommand(ymd); System.out.println("#done uncompressing " + disGPS);
//
//			String unzippedfile = FilePaths.deephomepath(ymd);
//			HashMap<String,LonLat> targetIDs_code = ExtractIDbyDate.extractIDPref(unzippedfile,time,zones,0); //0: minimum logs
//			System.out.println("#the number of IDs for " + ymd+time+ " is " + targetIDs_code.size());
//			File i = new File(unzippedfile); i.delete();
//
//			if(targetIDs_code.size()>10){ //対象人数で絞る
//				String dataforexp = workpath+"dataforexp.csv";
//				HashSet<String> targetdays = DayChooser.getTargetDates(ymd, dislog); System.out.println("#the number of days are " + targetdays.size());
//				Makedata4exp.makedata(dataforexp, targetdays, targetIDs_code); System.out.println("#successfully made data for exp");
//
//				HomeDetector.getHome(dataforexp, workpath);
//				HashMap<String,String> id_homecode = HomeDetector.gethomecode(workpath+"id_home.csv");
//				OfficeSchoolDetection.getOfficeSchool(dataforexp, workpath);
//
//				MovementAnalyzer.executeAnalyser
//				(dataforexp, FilePaths.dirfile(workpath,"id_home.csv"), FilePaths.dirfile(workpath,"id_office.csv"), 
//						workpath, disasterday, targetIDs_code, id_homecode);
//				MotifFinder2.executeMotif(dataforexp, workpath, disasterday, targetIDs_code, id_homecode,500,300);
//
//				File data = new File(dataforexp); data.delete();
//				//				File home = new File(workpath+"id_home.csv"); home.delete();
//				//				File office = new File(workpath+"id_office.csv"); office.delete();
//			}
//
//			//			if(!(new File(workpath+"home_exit.csv").exists())){
//			//				File emptydir = new File(workpath); emptydir.delete();
//			//			}
//
//		}
//	}
//
//}

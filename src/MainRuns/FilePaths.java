package MainRuns;

public class FilePaths {

	public static String tmppath(String yyyymmdd){
		String res = "/tmp/bousai_data/gps_"+yyyymmdd+".tar.gz";
		return res;
	}
	
	public static String deephomepath(String x){
		String res = "/home/t-tyabe/Data/grid/0/tmp/ktsubouc/gps_" + x +".csv";
		return res;
	}

	public static String homepath(String x){
		String res = "/home/t-tyabe/Data/" + x + ".csv";
		return res;
	}
	
	public static String homedir(String x){
		String res = "/home/t-tyabe/Data/" + x;
		return res;
	}
	
	public static String dirfile(String dir, String file){
		String res = dir +"/"+ file;
		return res;
	}
	
}

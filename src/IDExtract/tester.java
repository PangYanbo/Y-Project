package IDExtract;

public class tester {

	public static void main(String args[]){
		String id = "111";
		String lon = "100.0";
		String lat = "999.0";
		
		String res = String.join("\t",id,lon,lat);
		String res2 = String.join("\t", id, lon, lat);
		
		System.out.println(res);
		System.out.println(res2);
		}
}

package ToolsforFileManagement;

public class SlashDelete {

	public static void main(String arg[]){
		excludeslash("2011/1/12");
	}
	
	public static String excludeslash(String in){
		String[] tokens = in.split("/");
		String yyyymmdd = tokens[0] + String.format("%02d",Integer.valueOf(tokens[1])) + String.format("%02d",Integer.valueOf(tokens[2]));
		System.out.println(yyyymmdd);
		return yyyymmdd;
	}
	
}

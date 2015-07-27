package IDExtract;

public class ID_Extract_Tools {

	public static boolean SameLogCheck(String line, String prevline){
		if(line.equals(prevline)){
			return false;
		}
		else{
			return true;
		}
	}
	
}

package MachineLearning;

public class GetLevel {
	
	public static String getLevel(String level){
		if(level.equals("1")){
			return "1,0,0,0";
		}
		else if(level.equals("2")){
			return "0,1,0,0";
		}
		else if(level.equals("3")){
			return "0,0,1,0";
		}
		else if(level.equals("3")){
			return "0,0,0,1";
		}
		else{
			return "0,0,0,0";
		}
	}
}

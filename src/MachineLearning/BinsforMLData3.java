package MachineLearning;

public class BinsforMLData3 {

	public static String bilinearline2(String level, String time, String subject, String normaltime){

		String leveltime  = "3000"+level(level)+timerange2(time); //level and time of disaster
		String levelnormaltime   = "7000"+level(level)+getline4Diffs2(subject,normaltime);
		String res = " "+leveltime+":1 "+levelnormaltime+":1 ";		
		return res;
	}
	
	public static String level(String level){
		if(level.equals("1")){return "1";}
		else if(level.equals("2")){return "2";}
		else if(level.equals("3")){return "3";}
		else if(level.equals("4")){return "4";}
		else{return "5";}
	}
	
	public static String timerange(String time){
		if(time==null){
			return "0,0,0,0,0,0,0,0,0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<6){return "1,0,0,0,0,0,0,0,0";}
			else if ((timerange>=6)&&(timerange<8)){return "0,1,0,0,0,0,0,0,0";}
			else if ((timerange>=8)&&(timerange<10)){return "0,0,1,0,0,0,0,0,0";}
			else if ((timerange>=10)&&(timerange<14)){return "0,0,0,1,0,0,0,0,0";}
			else if ((timerange>=14)&&(timerange<16)){return "0,0,0,0,1,0,0,0,0";}
			else if ((timerange>=16)&&(timerange<18)){return "0,0,0,0,0,1,0,0,0";}
			else if ((timerange>=18)&&(timerange<20)){return "0,0,0,0,0,0,1,0,0";}
			else if ((timerange>=20)&&(timerange<22)){return "0,0,0,0,0,0,0,1,0";}
			else{return "0,0,0,0,0,0,0,0,1";}
		}
	}
	
	public static String timerange2(String time){
		if(time==null){
			return "0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<6){return "1";}
			else if ((timerange>=6)&&(timerange<8)){return "2";}
			else if ((timerange>=8)&&(timerange<10)){return "3";}
			else if ((timerange>=10)&&(timerange<14)){return "4";}
			else if ((timerange>=14)&&(timerange<16)){return "5";}
			else if ((timerange>=16)&&(timerange<18)){return "6";}
			else if ((timerange>=18)&&(timerange<20)){return "7";}
			else if ((timerange>=20)&&(timerange<22)){return "8";}
			else{return "9";}
		}
	}
	
	public static String getline4Diffs(String subject, String normaltime){
		if((subject.equals("tsukin_time_diff"))||(subject.equals("kitaku_time_diff"))){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<0.1){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=0.1)&&(timerange<0.25)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=0.25)&&(timerange<0.5)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=0.5)&&(timerange<0.75)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=0.75)&&(timerange<1)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=1.25)&&(timerange<1.5)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=1.75)&&(timerange<2)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=2)&&(timerange<2.5)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		if(subject.equals("office_time_diff")){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<1){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=1)&&(timerange<3)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=3)&&(timerange<5)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=5)&&(timerange<6)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=6)&&(timerange<7)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=7)&&(timerange<8)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=8)&&(timerange<9)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=9)&&(timerange<10)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		if(subject.equals("home_exit_diff")){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<5){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=5)&&(timerange<6)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=6)&&(timerange<7)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=7)&&(timerange<7.5)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=7.5)&&(timerange<8)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=8)&&(timerange<8.5)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=8.5)&&(timerange<9)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=9)&&(timerange<10)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		if(subject.equals("office_enter_diff")){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<6){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=6)&&(timerange<6.5)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=6.5)&&(timerange<7)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=7)&&(timerange<7.5)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=7.5)&&(timerange<8)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=8)&&(timerange<8.5)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=8.5)&&(timerange<9)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=9)&&(timerange<10)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		if(subject.equals("office_exit_diff")){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<15){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=15)&&(timerange<16)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=16)&&(timerange<17)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=17)&&(timerange<18)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=18)&&(timerange<19)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=19)&&(timerange<20)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=21)&&(timerange<22)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=22)&&(timerange<23)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		if(subject.equals("home_return_diff")){
			if(normaltime==null){
				return "0,0,0,0,0,0,0,0,0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<15){return "1,0,0,0,0,0,0,0,0";}
				else if ((timerange>=15)&&(timerange<16)){return "0,1,0,0,0,0,0,0,0";}
				else if ((timerange>=16)&&(timerange<17)){return "0,0,1,0,0,0,0,0,0";}
				else if ((timerange>=17)&&(timerange<18)){return "0,0,0,1,0,0,0,0,0";}
				else if ((timerange>=18)&&(timerange<19)){return "0,0,0,0,1,0,0,0,0";}
				else if ((timerange>=19)&&(timerange<20)){return "0,0,0,0,0,1,0,0,0";}
				else if ((timerange>=21)&&(timerange<22)){return "0,0,0,0,0,0,1,0,0";}
				else if ((timerange>=22)&&(timerange<23)){return "0,0,0,0,0,0,0,1,0";}
				else{return "0,0,0,0,0,0,0,0,1";}
			}
		}
		else{
			return "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		}
	}
	
	public static String getline4Diffs2(String subject, String normaltime){
		if((subject.equals("tsukin_time_diff"))||(subject.equals("kitaku_time_diff"))){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<0.1){return "1";}
				else if ((timerange>=0.1)&&(timerange<0.25)){return "2";}
				else if ((timerange>=0.25)&&(timerange<0.5)){return "3";}
				else if ((timerange>=0.5)&&(timerange<0.75)){return "4";}
				else if ((timerange>=0.75)&&(timerange<1)){return "5";}
				else if ((timerange>=1.25)&&(timerange<1.5)){return "6";}
				else if ((timerange>=1.75)&&(timerange<2)){return "7";}
				else if ((timerange>=2)&&(timerange<2.5)){return "8";}
				else{return "9";}
			}
		}
		if(subject.equals("office_time_diff")){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<1){return "1";}
				else if ((timerange>=1)&&(timerange<3)){return "2";}
				else if ((timerange>=3)&&(timerange<5)){return "3";}
				else if ((timerange>=5)&&(timerange<6)){return "4";}
				else if ((timerange>=6)&&(timerange<7)){return "5";}
				else if ((timerange>=7)&&(timerange<8)){return "6";}
				else if ((timerange>=8)&&(timerange<9)){return "7";}
				else if ((timerange>=9)&&(timerange<10)){return "8";}
				else{return "9";}
			}
		}
		if(subject.equals("home_exit_diff")){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<5){return "1";}
				else if ((timerange>=5)&&(timerange<6)){return "2";}
				else if ((timerange>=6)&&(timerange<7)){return "3";}
				else if ((timerange>=7)&&(timerange<7.5)){return "4";}
				else if ((timerange>=7.5)&&(timerange<8)){return "5";}
				else if ((timerange>=8)&&(timerange<8.5)){return "6";}
				else if ((timerange>=8.5)&&(timerange<9)){return "7";}
				else if ((timerange>=9)&&(timerange<10)){return "8";}
				else{return "9";}
			}
		}
		if(subject.equals("office_enter_diff")){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<6){return "1";}
				else if ((timerange>=6)&&(timerange<6.5)){return "2";}
				else if ((timerange>=6.5)&&(timerange<7)){return "3";}
				else if ((timerange>=7)&&(timerange<7.5)){return "4";}
				else if ((timerange>=7.5)&&(timerange<8)){return "5";}
				else if ((timerange>=8)&&(timerange<8.5)){return "6";}
				else if ((timerange>=8.5)&&(timerange<9)){return "7";}
				else if ((timerange>=9)&&(timerange<10)){return "8";}
				else{return "9";}
			}
		}
		if(subject.equals("office_exit_diff")){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<15){return "1";}
				else if ((timerange>=15)&&(timerange<16)){return "2";}
				else if ((timerange>=16)&&(timerange<17)){return "3";}
				else if ((timerange>=17)&&(timerange<18)){return "4";}
				else if ((timerange>=18)&&(timerange<19)){return "5";}
				else if ((timerange>=19)&&(timerange<20)){return "6";}
				else if ((timerange>=21)&&(timerange<22)){return "7";}
				else if ((timerange>=22)&&(timerange<23)){return "8";}
				else{return "9";}
			}
		}
		if(subject.equals("home_return_diff")){
			if(normaltime==null){
				return "0";
			}
			else{
				Double timerange = Double.parseDouble(normaltime);
				if(timerange<15){return "1";}
				else if ((timerange>=15)&&(timerange<16)){return "2";}
				else if ((timerange>=16)&&(timerange<17)){return "3";}
				else if ((timerange>=17)&&(timerange<18)){return "4";}
				else if ((timerange>=18)&&(timerange<19)){return "5";}
				else if ((timerange>=19)&&(timerange<20)){return "6";}
				else if ((timerange>=21)&&(timerange<22)){return "7";}
				else if ((timerange>=22)&&(timerange<23)){return "8";}
				else{return "9";}
			}
		}
		else{
			return "0";
		}
	}
	
	public static String sigmaline(Double sigma){
		if(sigma==null){
			return "0,0,0,0,0,0,0,0,0";
		}
		else{
			if(sigma<0.5){return "1,0,0,0,0,0,0,0,0";}
			else if ((sigma>=0.5)&&(sigma<1)){return "0,1,0,0,0,0,0,0,0";}
			else if ((sigma>=1)&&(sigma<1.5)){return "0,0,1,0,0,0,0,0,0";}
			else if ((sigma>=1.5)&&(sigma<2)){return "0,0,0,1,0,0,0,0,0";}
			else if ((sigma>=2)&&(sigma<2.5)){return "0,0,0,0,1,0,0,0,0";}
			else if ((sigma>=2.5)&&(sigma<3)){return "0,0,0,0,0,1,0,0,0";}
			else if ((sigma>=3)&&(sigma<3.5)){return "0,0,0,0,0,0,1,0,0";}
			else if ((sigma>=3.5)&&(sigma<4)){return "0,0,0,0,0,0,0,1,0";}
			else{return "0,0,0,0,0,0,0,0,1";}
		}
	}
	
	
}

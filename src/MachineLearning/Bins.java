package MachineLearning;

public class Bins {
	public static String timerange(String time){
		if(time==null){
			return "0,0,0,0,0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<6){return "1,0,0,0,0";}
			else if ((timerange>=6)&&(timerange<10)){return "0,1,0,0,0";}
			else if ((timerange>=10)&&(timerange<16)){return "0,0,1,0,0";}
			else if ((timerange>=16)&&(timerange<20)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}

	public static String timerangeshort(String time){
		if(time==null){
			return "0,0,0,0,0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<1){return "1,0,0,0,0";}
			else if ((timerange>=1)&&(timerange<2)){return "0,1,0,0,0";}
			else if ((timerange>=2)&&(timerange<3)){return "0,0,1,0,0";}
			else if ((timerange>=4)&&(timerange<5)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}

	public static String timerangeoffice(String time){
		if(time==null){
			return "0,0,0,0,0";
		}
		else{
			Double timerange = Double.parseDouble(time);
			if(timerange<1){return "1,0,0,0,0";}
			else if ((timerange>=1)&&(timerange<3)){return "0,1,0,0,0";}
			else if ((timerange>=3)&&(timerange<6)){return "0,0,1,0,0";}
			else if ((timerange>=6)&&(timerange<10)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}

	public static String getlineDistance(String poprate){
		if(poprate==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(poprate);
			if(poprange<0.01){return "1,0,0,0,0";}
			else if ((poprange>=0.01)&&(poprange<0.05)){return "0,1,0,0,0";}
			else if ((poprange>=0.05)&&(poprange<0.25)){return "0,0,1,0,0";}
			else if ((poprange>=0.25)&&(poprange<0.40)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}

	public static String h_e_line(String diff){
		if(diff==null){
			return "0,0,0,0,0";
		}
		else{
			Double poprange = Double.parseDouble(diff);
			if(poprange<-3){return "1,0,0,0,0";}
			else if ((poprange>=-3)&&(poprange<-1)){return "0,1,0,0,0";}
			else if ((poprange>=-1)&&(poprange<1)){return "0,0,1,0,0";}
			else if ((poprange>=1)&&(poprange<3)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	public static String sigmaline(Double sigma){
		if(sigma==null){
			return "0,0,0,0,0";
		}
		else{
			if(sigma==0d){return "1,0,0,0,0";}
			else if ((sigma>=0d)&&(sigma<1)){return "0,1,0,0,0";}
			else if ((sigma>=1)&&(sigma<2)){return "0,0,1,0,0";}
			else if ((sigma>=2)&&(sigma<3)){return "0,0,0,1,0";}
			else{return "0,0,0,0,1";}
		}
	}
	
	public static String getline4Diffs(String subject, String normaltime){
		if((subject.equals("tsukin_time_diff"))||(subject.equals("kitaku_time_diff"))){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<0.5){return "1,0,0,0,0";}
				else if ((poprange>=0.5)&&(poprange<1)){return "0,1,0,0,0";}
				else if ((poprange>=1)&&(poprange<2)){return "0,0,1,0,0";}
				else if ((poprange>=2)&&(poprange<4)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		if(subject.equals("office_time_diff")){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<1){return "1,0,0,0,0";}
				else if ((poprange>=1)&&(poprange<4)){return "0,1,0,0,0";}
				else if ((poprange>=4)&&(poprange<9)){return "0,0,1,0,0";}
				else if ((poprange>=9)&&(poprange<14)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		if(subject.equals("home_exit_diff")){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<6){return "1,0,0,0,0";}
				else if ((poprange>=6)&&(poprange<8)){return "0,1,0,0,0";}
				else if ((poprange>=8)&&(poprange<10)){return "0,0,1,0,0";}
				else if ((poprange>=10)&&(poprange<12)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		if(subject.equals("office_enter_diff")){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<7){return "1,0,0,0,0";}
				else if ((poprange>=7)&&(poprange<9)){return "0,1,0,0,0";}
				else if ((poprange>=9)&&(poprange<12)){return "0,0,1,0,0";}
				else if ((poprange>=12)&&(poprange<18)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		if(subject.equals("office_exit_diff")){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<16){return "1,0,0,0,0";}
				else if ((poprange>=16)&&(poprange<18)){return "0,1,0,0,0";}
				else if ((poprange>=18)&&(poprange<20)){return "0,0,1,0,0";}
				else if ((poprange>=20)&&(poprange<23)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		if(subject.equals("home_return_diff")){
			if(normaltime==null){
				return "0,0,0,0,0";
			}
			else{
				Double poprange = Double.parseDouble(normaltime);
				if(poprange<17){return "1,0,0,0,0";}
				else if ((poprange>=17)&&(poprange<19)){return "0,1,0,0,0";}
				else if ((poprange>=19)&&(poprange<21)){return "0,0,1,0,0";}
				else if ((poprange>=21)&&(poprange<23)){return "0,0,0,1,0";}
				else{return "0,0,0,0,1";}
			}
		}
		else{
			return "0,0,0,0,0";
		}
	}
}

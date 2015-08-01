package MobilityAnalyser;

import java.util.ArrayList;

public class MotifNumber {

	public static Integer motifs(ArrayList<Integer> locchain){
		if(locchain.size()==1){
			return 1;
		}
		else if(locchain.size()==2){
			System.out.println("2 nodes... something wrong");
			return 99;
		}
		else if(locchain.size()==3){
			return 2; 
		}
		else if(locchain.size()==4){
			return 4;
		}
		else if(locchain.size()==5){
			if(locchain.get(3)==4){
				return 7;
			}
			else if(locchain.get(2)==3){
				return 5;
			}
			else{
				return 3;
			}
		}
		else if(locchain.size()==6){
			if(locchain.get(4)==5){
				return 11;
			}
			else{
				return 6;
			}
		}
		else if(locchain.size()==9){
			return 16;
		}
		else if(locchain.size()==8){
			if(locchain.contains(6)){
				if((locchain.get(3)==1)||(locchain.get(4)==1)){
					return 17;
				}
				else{
					return 14;
				}
			}
			else{
				return 12;
			}
		}
		else if(locchain.size()==7){
			if(locchain.contains(6)){
				return 15;
			}
			else if(locchain.get(3)==1){
				return 13;
			}
			else if(locchain.contains(5)){
				return 10;
			}
			else if(locchain.get(2)==1&&locchain.get(4)==1){
				return 8;
			}
			else{
				return 9;
			}
		}
		else{
			return 0;
		}
	}
	
}

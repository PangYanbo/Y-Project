package testfiles;

public class round {

	public static int numofline(Double num){
		int res = 0;
		if(num>=1){
			res = (int)Math.round(num);
		}
		else{
			res = 1;
		}
		return res;
	}
	
	public static void main(String args[]){
		int a = (int)(numofline(1.12));
		System.out.println(a);
	}
	
}

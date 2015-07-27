package testfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SameLogtest {

	public static void main(String args[]){
		File in = new File("c:/users/yabetaka/Desktop/test.txt");
		test(in);
	}
	
	/*
	 * TESTED 7/27/2015
	 * POSITIVE
	 * 
	 */
	
	public static boolean SameLogCheck(String line, String prevline){
		if(line.equals(prevline)){
			return false;
		}
		else{
			return true;
		}
	}

	public static void test(File in){
		try{
			BufferedReader br = new BufferedReader(new FileReader(in));
			String line = br.readLine();
			String prevline = null;
			while ((line=br.readLine())!=null){
				if(SameLogCheck(line,prevline)==true){
					System.out.println("diff");
				}
				else{
					System.out.println("same");
				}
				prevline = line;
			}
			br.close();
		}
		catch(FileNotFoundException xx) {
			System.out.println("File not found 5");
		}
		catch(IOException xxx) {
			System.out.println(xxx);
		}
	}
}

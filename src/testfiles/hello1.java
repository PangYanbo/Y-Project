package testfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class hello1 {
	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
		IdCounter(args[0]);
	}

	public static void IdCounter(String in){
		int counter = 0;
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			String line = null;
			while ((line=br.readLine())!=null){
				String[] tokens = line.split("\t");
				if(tokens.length>1){
					String id = tokens[0];
					if(!id.equals("null")){
						counter++;
					}
				}
			}
			System.out.println("null:" + counter);
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

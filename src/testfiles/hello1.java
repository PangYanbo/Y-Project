package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

public class hello1 {
	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
		IdCounter(args[0],args[1]);
	}

	public static void IdCounter(String in, String out){
		int counter = 0;
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
			String line = null;
			while ((line=br.readLine())!=null){
				bw.write(line);
				bw.newLine();
				counter++;
				if(counter == 10){
					break;
				}
			}
			System.out.println("done sorting out, will start putting them in order...");

			br.close();
			bw.close();
		}
		catch(FileNotFoundException xx) {
			System.out.println("File not found 5");
		}
		catch(IOException xxx) {
			System.out.println(xxx);
		}
	}
}

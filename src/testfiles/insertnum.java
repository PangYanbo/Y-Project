package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class insertnum {

	public static void main(String args[]) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File("c:/users/yabetaka/desktop/data_example/data_example.dat")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("c:/users/yabetaka/desktop/data_example_2.dat")));
		String line = null;
		int count = 1;
		while((line=br.readLine())!=null){
			bw.write(count + " " + line);
			bw.newLine();
			count++;
		}
		bw.close();
		br.close();
	}
	
}

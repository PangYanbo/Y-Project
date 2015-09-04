package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class makepopfile {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/yabetaka/desktop/pflowdata/08tky2_1200_mesh.csv");
		File out = new File("c:/users/yabetaka/desktop/pflowdata/08tky2_1200_mesh_clean.csv");
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			bw.write(tokens[0]+","+tokens[1]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}
	
}

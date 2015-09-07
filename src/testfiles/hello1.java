package testfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class hello1 {
	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
		IdCounter(args[0],args[1]);
	}

	public static void IdCounter(String subject, String mode) throws IOException{
		for(int v = 1; v<=30; v++){
			String infile = "/home/c-tyabe/Data/MLResults_rain2/forML/"+subject+"_diff_ML_plusminus_"+mode+".csv";
			String outfile = "/home/c-tyabe/Data/MLResults_rain2/"+subject+"_diff_ML_plusminus_"+mode+"_forcounting"+String.valueOf(v)+".csv";
			BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));
			String line = null;
			ArrayList<String> list = new ArrayList<String>();
			while ((line=br.readLine())!=null){
				String[] tokens = line.split(" ");
				for(String token : tokens){
					if(token.split(":").length==2){
						if(token.split(":")[0].equals(String.valueOf(v))){
							String val = token.split(":")[1];
							list.add(val);
						}
					}
				}
			}
			for(String atai : list){
				bw.write(atai);
				bw.newLine();
			}
			br.close();
			bw.close();
		}
	}
}

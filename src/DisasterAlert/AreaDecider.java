package DisasterAlert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class AreaDecider {

	public static void main(String args[]) throws IOException{
		File in = new File("c:/users/c-tyabe/desktop/DisasterDataLogs/DisasterAlertData.csv");
		File out = new File("c:/users/c-tyabe/desktop/DisasterDataLogs/DisasterAlertData_toshiken.csv");
		File jiscodes = new File("c:/users/c-tyabe/desktop/ToshikenSHP/JIScodes.xlsx");
		choosebyArea(in,out,jiscodes);		
	}
	
	public static File choosebyArea(File in, File out, File codes) throws IOException{
		BufferedReader br2 = new BufferedReader(new FileReader(codes));
		HashSet<String> JISset = new HashSet<String>();
		String line2 = null;
		while((line2=br2.readLine())!=null){
			String[] tokens = line2.split(",");
			String code = tokens[0];
			JISset.add(code);
		}
		br2.close();
		
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = br.readLine();
		while((line = br.readLine())!= null){
			String[] tokens = line.split(",");
			String jiscode = tokens[3];		
			if(JISset.contains(jiscode)){
				bw.write(line);
				count++;
			}
		}
		br.close();
		bw.close();
		System.out.println("number of disasters in toshiken : " + count);
		return out;
	}
	
}

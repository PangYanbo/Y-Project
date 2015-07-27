package IDExtract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class ID_Chooser {

	/*
	 * param 
	 * args[0] : All logs of 1 day
	 * args[1] : Output file (Selected IDs)
	 * args[2] : Matome File (not necessarily needed...)
	 * args[3] : Number of Wanted Samples
	 * 
	 */
	public static void main(String args[]) throws NumberFormatException, ParseException, IOException{
		Id_CountGetter(args[0],args[1],args[2], args[3]);
		System.out.println("finished "+ args[0]);
	}

	public static void Id_CountGetter(String in, String out, String matomeres, String SampleNumber){
		int counter = 0;
		int errorLine = 0;
		int samelog = 0;
		HashMap<String, Integer> id_count = new HashMap<String,Integer>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(matomeres),true));
			String line = br.readLine();
			String prevline = null;
			while ((line=br.readLine())!=null){
				if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
					String[] tokens = line.split("\t");
					if(tokens.length>1){
						String id = tokens[0];
						if(!id.equals("null")){
							if(id_count.containsKey(id)){
								int count = id_count.get(id) + 1;
								id_count.put(id, count);
							}
							else{
								id_count.put(id, 1);
							}
							counter++;
						}
					}
					else{
						errorLine++;
					}
				}
				else{
					samelog++;
				}
				prevline = line;
			}
			System.out.println("done " + counter + " lines, and " + errorLine +" were error lines, and "+samelog+" were samelogs.");
			System.out.println("done sorting out, will start writing them out...");

			int bigusers = 0;
			for(String str : id_count.keySet()){
				if((id_count.get(str)>20)&&(id_count.get(str)<60)){
					bw.write(str + "," + id_count.get(str));
					bw.newLine();
					bigusers++;
					if(bigusers==Integer.parseInt(SampleNumber)){
						break;
					}
				}
			}
			bw2.write(in + ":" + counter + "," + errorLine + "," + samelog + "," + bigusers);
			bw2.newLine();
			bw2.close();
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

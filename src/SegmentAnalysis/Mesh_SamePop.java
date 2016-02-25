package SegmentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Mesh_SamePop {

	public static void main(String args[]) throws IOException{
		String base = "C:/Users/yabetaka/Desktop/MasterPresentation/EqRainPart/forMeshDiagram/";

		File in1 = new File(base+"20150512_raw_onlyshutoken_mesh.csv");
		File in1out = new File(base+"20150512_raw_onlyshutoken_mesh_cv.csv");
		File in2 = new File(base+"20150521_raw_onlyshutoken_mesh.csv");
		File in2out = new File(base+"20150521_raw_onlyshutoken_mesh_cv.csv");
		File in3 = new File(base+"20150522_raw_onlyshutoken_mesh.csv");
		File in3out = new File(base+"20150522_raw_onlyshutoken_mesh_cv.csv");

		File in12out = new File(base+"20150521_raw_onlyshutoken_mesh_diff12.csv");
		File in32out = new File(base+"20150521_raw_onlyshutoken_mesh_diff32.csv");
		
		converttotalpop(in1,in1out);
		converttotalpop(in2,in2out);
		converttotalpop(in3,in3out);

//		compare2(in1,in2,in12out);
//		compare2(in3,in2,in32out);
		
		
	}

	public static void converttotalpop(File in, File out) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = br.readLine();
		Integer temptotal = 0;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			Integer pop = Integer.valueOf(tokens[1]);
			temptotal = temptotal+pop;
		}
		br.close();

		Double convertrate = 30000000d/(double)temptotal;
		System.out.println(temptotal);
		
		BufferedReader br2 = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line2 = br2.readLine();
		while((line2=br2.readLine())!=null){
			String[] tokens = line2.split("\t");
			Integer pop = Integer.valueOf(tokens[1]);
			Integer newpop = (int)(pop*convertrate);
			bw.write(tokens[0]+"\t"+newpop+"\t"+tokens[2]);
			bw.newLine();
		}

		bw.close();
		br2.close();
	}

	public static void compare2(File one, File two, File out) throws NumberFormatException, IOException{

		HashMap<String, Integer> onemap = new HashMap<String, Integer>();
		HashMap<String, String> geommap = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(one));
		String line = br.readLine();
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			Integer pop = Integer.valueOf(tokens[1]);
			onemap.put(tokens[0], pop);
			geommap.put(tokens[0], tokens[2]);
		}
		br.close();

		HashMap<String, Integer> twomap = new HashMap<String, Integer>();
		BufferedReader br2 = new BufferedReader(new FileReader(two));
		String line2 = br2.readLine();
		while((line2=br2.readLine())!=null){
			String[] tokens = line2.split("\t");
			Integer pop = Integer.valueOf(tokens[1]);
			twomap.put(tokens[0], pop);		
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(out));

		for(String mesh : onemap.keySet()){
			if(twomap.containsKey(mesh)){
				String diff = String.valueOf(onemap.get(mesh)-twomap.get(mesh));
				bw.write(mesh+"\t"+diff+"\t"+geommap.get(mesh));
				bw.newLine();
			}
		}
		bw.close();
		br2.close();

	}

}

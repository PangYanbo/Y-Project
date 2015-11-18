package MachineLearning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//ML Data for normal-irregular decision

public class MLclean1020 {



	public static void main(String args[]) throws IOException{

		String type      = args[0];
		String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"12/";
		String outdir2   = outdir+"forML/";
		String outdir3   = outdir+"forML/calc/";

		File outputdir  = new File(outdir);  outputdir.mkdir();
		File outputdir2 = new File(outdir2); outputdir2.mkdir();
		File outputdir3 = new File(outdir3); outputdir3.mkdir();

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit_diff");
		subjects.add("tsukin_time_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_time_diff");
		subjects.add("office_exit_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_return_diff");
		run(subjects, outdir3);

	}

	public static void run(ArrayList<String> subjects, String outdir3) throws IOException{


		for(String subject : subjects){
			String infile   = outdir3+subject+"_ML_plusminus_lineforeach.csv"; 
			String outfile   = outdir3+subject+"_ML2_plusminus_lineforeach.csv"; 

			naosu(infile,outfile);
		}
	}


	public static void naosu(String infile, String outfile) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile),true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			bw.write(tokens[0]);
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=1; i<tokens.length-1; i++){
				if(!tokens[i].split(":")[0].isEmpty()){
					Integer num = Integer.valueOf(tokens[i].split(":")[0]);
					list.add(num);
				}
			}
			Collections.sort(list);
			for(Integer n : list){
				bw.write(" "+String.valueOf(n)+":1");
			}
			bw.write(" "+tokens[tokens.length-1]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

	public static void deletecomment(String infile, String outfile) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile),true));
		String line = null;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			bw.write(tokens[0]);
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=1; i<tokens.length-1; i++){
				if(!tokens[i].split(":")[0].isEmpty()){
					Integer num = Integer.valueOf(tokens[i].split(":")[0]);
					list.add(num);
				}
			}
			Collections.sort(list);
			for(Integer n : list){
				bw.write(" "+String.valueOf(n)+":1");
			}
			bw.write(" "+tokens[tokens.length-1]);
			bw.newLine();
		}
		br.close();
		bw.close();
	}

	public static void count(String infile, String subject) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(new File(infile)));
		String line = null;

		int count1 = 0;
		int count0 = 0;
		int counti = 0;

		while((line=br.readLine())!=null){
			String[] tokens = line.split(" ");
			if(tokens[0].equals("1")){
				count1++;
			}
			else if(tokens[0].equals("0")){
				count0++;
			}
			else{
				counti++;
			}
		}
		br.close();

		double perc1 = (double)count1/(double)(count1+count0+counti);
		double perc0 = (double)count0/(double)(count1+count0+counti);
		double perci = (double)counti/(double)(count1+count0+counti);


		System.out.println(subject+": 1:"+perc1+", 0:"+perc0+", -1:"+perci);

	}


}


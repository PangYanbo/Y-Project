package MachineLearning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SameNumberofLines {

	public static void main(String args[]) throws IOException{
		String type      = args[0];	
		String outdir    = "/home/c-tyabe/Data/MLResults_"+type+"13/";
		String outdir3   = outdir+"forML/calc/";
		String outdir4   = outdir+"forML/calc/sameexp/";

		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("home_exit_diff");
		subjects.add("tsukin_time_diff");
		subjects.add("office_enter_diff");
		subjects.add("office_time_diff");
		subjects.add("office_exit_diff");
		subjects.add("kitaku_time_diff");
		subjects.add("home_return_diff");

		for(String subject:subjects){
			String plusminus_multiplelinesclean = outdir3+subject+"_ML2_plusminus_lineforeach.csv";
			String plusminus_multiplelinesclean_samenumlines = outdir4+subject+"_ML2_plusminus_lineforeach_same.csv";
			MLData2.samenumberoflines(new File(plusminus_multiplelinesclean), new File(plusminus_multiplelinesclean_samenumlines));
		}
	}

}

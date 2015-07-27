package StayPointDetection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.STPoint;
import jp.ac.ut.csis.pflow.geom.clustering.MeanShift;
import jp.ac.ut.csis.pflow.geom.clustering.MeanShift.IKernel;

public class StayPointGetter {
	
	/*
	 * param 
	 * in : File of all data
	 * start : start time of cropping
	 * end : end time of cropping
	 * min :  minimum points for a cluster
	 * sigma & threshold : params used in clustering
	 * 
	 */
	
	public static HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> getSPs
	(File in, String start, String end, int min, double sigma, double threshold) 
			throws NumberFormatException, ParseException, IOException{

		HashMap<String, ArrayList<STPoint>> alldatamap = sortintoMap(in);
		HashMap<String, ArrayList<STPoint>> targetmap = getTargetMap(alldatamap,start,end);

		HashMap<String,HashMap<LonLat,ArrayList<STPoint>>> res = new HashMap<String,HashMap<LonLat,ArrayList<STPoint>>>();
		for(String id:targetmap.keySet()){
			HashMap<LonLat, ArrayList<STPoint>> SPmap = getStayPoints(targetmap.get(id),sigma,threshold,min);
			if(SPmap.size()>0){
				res.put(id, SPmap);
			}
		}
		return res;
	}
	
	protected static final SimpleDateFormat SDF_TS = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat SDF_TS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	
	public static HashMap<String, ArrayList<STPoint>> sortintoMap(File in) throws ParseException, NumberFormatException, IOException{
		HashMap<String, ArrayList<STPoint>> id_count = new HashMap<String, ArrayList<STPoint>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while ((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			STPoint point = new STPoint(SDF_TS2.parse(tokens[1]),Double.parseDouble(tokens[2]),Double.parseDouble(tokens[3]));
			if(id_count.containsKey(id)){
				id_count.get(id).add(point);
			}
			else{
				ArrayList<STPoint> list = new ArrayList<STPoint>();
				list.add(point);
				id_count.put(id, list);
			}
		}
		br.close();	
		return id_count;
	}

	public static HashMap<String, ArrayList<STPoint>> getTargetMap(HashMap<String, ArrayList<STPoint>> alldata, String start, String end) throws ParseException{
		HashMap<String, ArrayList<STPoint>> targetmap = new HashMap<String, ArrayList<STPoint>>();
		Date startdate = SDF_TS.parse(start);
		Date finishdate = SDF_TS.parse(end);
		for(String id: alldata.keySet()){
			for(STPoint p: alldata.get(id)){
				String date = (new SimpleDateFormat("HH:mm:ss")).format(p.getTimeStamp());
				Date date1 = SDF_TS.parse(date);
				if( (date1.after(startdate))&&(date1.before(finishdate)) ){
					if(targetmap.containsKey(id)){
						targetmap.get(id).add(p);
					}
					else{
						ArrayList<STPoint> list = new ArrayList<STPoint>();
						list.add(p);
						targetmap.put(id, list);
					}
				}
			}
		}
		return targetmap;
	}

	public static HashMap<LonLat, ArrayList<STPoint>> getStayPoints(ArrayList<STPoint> list, double h, double e, int min){
		HashMap<LonLat, ArrayList<STPoint>> map = new HashMap<LonLat, ArrayList<STPoint>>();
		map = clustering2dKNSG(MeanShift.GAUSSIAN,list,h,e);
		HashMap<LonLat, ArrayList<STPoint>> Cutmap = cutbyPoints(map,min);		
		return Cutmap;
	}

	public static HashMap<LonLat, ArrayList<STPoint>> cutbyPoints(HashMap<LonLat, ArrayList<STPoint>> in, int min){
		HashMap<LonLat, ArrayList<STPoint>> res = new HashMap<LonLat, ArrayList<STPoint>>();
		for(LonLat p : in.keySet()){
			if(in.get(p).size()>=min){
				res.put(p, in.get(p));
			}
		}
		return res;
	}

	//h:determines how far points can influence eachother, e:determines closeness of same cluster
	public static HashMap<LonLat,ArrayList<STPoint>> clustering2dKNSG(IKernel kernel,ArrayList<STPoint> data,double h,double e) {
		HashMap<LonLat,ArrayList<STPoint>> result = new HashMap<LonLat,ArrayList<STPoint>>();
		int N = data.size();
		//		System.out.println("#number of points : "+N);

		for(STPoint point:data) {
			// seek mean value //////////////////
			LonLat mean = new LonLat(point.getLon(),point.getLat());

			//loop from here for meanshift
			while(true) {
				double numx = 0d;
				double numy = 0d;
				double din = 0d;
				for(int j=0;j<N;j++) {
					LonLat p = new LonLat(data.get(j).getLon(),data.get(j).getLat());
					double k = kernel.getDensity(mean,p,h);
					numx += k * p.getLon();
					numy += k * p.getLat();
					din  += k;
				}
				LonLat m = new LonLat(numx/din,numy/din);
				if( mean.distance(m) < e ) { mean = m; break; }
				mean = m;
			}
			//			System.out.println("#mean is : " + mean);
			// make cluster /////////////////////
			ArrayList<STPoint> cluster = null;
			for(LonLat p:result.keySet()) {
				if( mean.distance(p) < e ) { cluster = result.get(p); break; }
			}
			if( cluster == null ) {
				cluster = new ArrayList<STPoint>();
				result.put(mean,cluster);
			}
			cluster.add(point);
		}
		return result;
	}

}

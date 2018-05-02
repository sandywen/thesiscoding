package multidimensionaccuracyexp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.utilities.pairs.Pair;
import multidimension.DTWm;
import multidimension.EDRm;
import multidimension.LCSSm;
import multidimension.MultiDimensionDistanceFunction;
import multidimension.NewGotohm;
import utils.DataHandler;

public class MethodAccuracyNew {
	
	private final double edr_delta = 0.4;
	private final double lcss_delta = 1.0;
	private final double lcss_epsilon = 0.4;
	private double co = 1.0;
	private double cr = 1.0;
		
	/**
	 * get the accuracy of compared methods 
	 * @param filepath
	 * @return
	 */
	public String getComparedResult(String dir_path, String dataset, String method_str){
		
		//readData
		dir_path += "/"+dataset;
		ArrayList<String> file_list = getFileList(dir_path);
		
		if(file_list == null || file_list.size() == 0) {
			return "";
		}
		
		HashMap<NumberVector[], String> train_data = null;
		HashMap<NumberVector[], String> test_data  = null;
		
		for(String file_name : file_list){
			
			String file_path = dir_path + "/" + file_name;
			if(file_name.contains("TEST")){
				test_data = readFile(file_path);
			}else{
				train_data = readFile(file_path);
			}
		}
		
		HashSet<String> set = new HashSet<>();
		set.addAll(train_data.values());
		for(String class_name: set) {
			if(class_name.equals("0") || class_name.equals("-1")) {
				return "";
			}
		}
		int class_num = set.size();
		
		System.out.println("trainsize:"+train_data.size()+" testsize:"+test_data.size()+
				" class:" + class_num);
	
		return getAccuracy(test_data, train_data, method_str, class_num, dataset);
		
//		System.out.println( "dataset: "+dataset+" method: " + method_str +" error rate:" + (1-accuracy));
		
	}
	
	/**
	 * 
	 * @param test_data
	 * @param train_data
	 * @param method_str
	 * @return
	 */
	MultiDimensionDistanceFunction function;
	public String getAccuracy(HashMap<NumberVector[],String> test_data,
			HashMap<NumberVector[],String> train_data, String method_str,
			int class_num, String dataset) {
		
		int test_size = test_data.size();
		double[] min_distance = new double[test_size];
		String[] class_result = new String[test_size];
		
		Arrays.fill(min_distance, Double.MAX_VALUE);
		
		
		int index = 0;
		int right_labels = 0;
		
		for(Map.Entry<NumberVector[], String> entry_test: test_data.entrySet()){
			NumberVector[] v1 = entry_test.getKey();
			for(Map.Entry<NumberVector[], String> entry_train: train_data.entrySet()){
				NumberVector[] v2 = entry_train.getKey();
				double bandsize = Math.max(v1[0].getDimensionality(), v2[0].getDimensionality());
				
				switch(method_str) {
					case "DTW":
						function = new DTWm(bandsize);
						break;
					case "EDR":
						function = new EDRm(edr_delta,bandsize);
						break;
					case "LCS":
						function = new LCSSm(lcss_delta,lcss_epsilon);
						break;
					case "NewGotoh":
						function = new NewGotohm(co, cr);
						break;
					default:
						break;
				}
				
				double distance = function.distance(v1, v2);
				if((distance < min_distance[index])) {
					min_distance[index] = distance;
					class_result[index] = entry_train.getValue();
				}
			}

			
			//得到混淆矩阵
			int truth_label = Integer.parseInt(entry_test.getValue());
			int predict_label = Integer.parseInt(class_result[index]);
			
			if(truth_label == predict_label) {
				right_labels++;
			}

			
			index++;
	      }
		

		double accuracy = (double)right_labels/(double)test_size;
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		
		String file_line =  dataset + "\t" + decimalFormat.format(accuracy);
		return file_line;
	}
	
	public HashMap<NumberVector[], String> readFile(String filepath){
		HashMap<NumberVector[], String> timeseries_map = new LinkedHashMap<>();
		DataHandler handler = new DataHandler();
		
		ArrayList<String> map_A = null;
		ArrayList<String> map_B = null;
		ArrayList<String> map_C = null;
		
		ArrayList<String> file_list = handler.getFileList(filepath);
		
		for(String file : file_list) {
			if(file.contains("A") || file.contains("X")) {
				map_A = handler.readFile(filepath + "/" + file, " ");
			}else if(file.contains("B") || file.contains("Y")) {
				map_B = handler.readFile(filepath + "/" + file, " ");
			}else if(file.contains("C") || file.contains("Z")) {
				map_C= handler.readFile(filepath + "/" + file, " ");
			}
		}
		
		int size = map_A.size();
		String symbol = " ";
		for(int i = 0; i < size; i++) {
			
			NumberVector[] vecs = new NumberVector[3];
			
			String line_A = map_A.get(i);
			Pair<NumberVector,String> vec_A = handler.stringTodouble(line_A, symbol);
			vecs[0] = vec_A.getFirst();
			
			String line_B = map_B.get(i);
			Pair<NumberVector,String> vec_B = handler.stringTodouble(line_B, symbol);
			vecs[1] = vec_B.getFirst();
			
			String line_C = map_C.get(i);
			Pair<NumberVector,String> vec_C = handler.stringTodouble(line_C, symbol);
			vecs[2] = vec_C.getFirst();
			
			if(i == 0) {
				System.out.println("length："+vecs[0].getDimensionality());
			}
			
			timeseries_map.put(vecs, vec_A.getSecond());
			
		}
		return timeseries_map;
		
	}
	

	public void writeFile(String file, String line) {
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(line+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void normalization(double[] in) {
		double mean = 0;
		int length = in.length;
		
		for(int i = 0; i < length; i++) {
			mean += in[i];
		}
		mean /= length;
		
		double std = 0;
		for(int i = 0; i < length ; i++) {
			std += Math.pow(in[i]-mean, 2) / length;
		}
		std = Math.sqrt(std);
		
		for(int i = 0; i < length; i++) {
			in[i] = (in[i] - mean) / std;
		}
		
	}
	
	public ArrayList<String> getFileList(String dir_path){
		
		File dirs = new File(dir_path);
		File[] files = dirs.listFiles();
		
		if(files == null || files.length == 0) {
			return null;
		}
		
		ArrayList<String> dir_name = new ArrayList<String>();
		for(File file : files) {
			if(file.getName().startsWith(".")) {
				continue;
			}
			dir_name.add(file.getName());
		}
		
		return dir_name;
	}
	
		

	public double getCo() {
		return co;
	}

	public double getCr() {
		return cr;
	}

	public void setCo(double co) {
		this.co = co;
	}

	public void setCr(double cr) {
		this.cr = cr;
	}
	

}

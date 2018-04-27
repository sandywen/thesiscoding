package accuracyexp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import de.lmu.ifi.dbs.elki.data.DoubleVector;
import onedimension.DTW;
import onedimension.ED;
import onedimension.EDR;
import onedimension.ERP;
import onedimension.LCSS;
import onedimension.NewGotoh;
import onedimension.UserDistanceFunction;

public class MethodAccuracy {
	
	private final double edr_delta = 0.4;
	private final double erp_g = 0;
	private final double lcss_delta = 1.0;
	private final double lcss_epsilon = 0.4;
	private double co = 1.0;
	private double cr = 0.2;
		
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
		
		HashMap<double[], String> train_data = null;
		HashMap<double[], String> test_data  = null;
		
		for(String file_name : file_list){
			
			String file_path = dir_path + "/" + file_name;
			if(file_name.contains("TEST")){
				test_data = readFile(file_path);
//				train_data = readFile(file_path);
			}else{
//				test_data = readFile(file_path);
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
	UserDistanceFunction function;
	public String getAccuracy(HashMap<double[],String> test_data,
			HashMap<double[],String> train_data, String method_str,
			int class_num, String dataset) {
		
		int test_size = test_data.size();
		double[] min_distance = new double[test_size];
		String[] class_result = new String[test_size];
//		int[][] confusion_matrix = new int[class_num][class_num];
		
		Arrays.fill(min_distance, Double.MAX_VALUE);
		
		
		int index = 0;
		int right_labels = 0;
		
		for(Map.Entry<double[], String> entry_test: test_data.entrySet()){
			DoubleVector v1 = new DoubleVector(entry_test.getKey());
			for(Map.Entry<double[], String> entry_train: train_data.entrySet()){
				DoubleVector v2 = new DoubleVector(entry_train.getKey());
				double bandsize = Math.max(v1.getDimensionality(), v2.getDimensionality());
				
				switch(method_str) {
					case "DTW":
						function = new DTW(bandsize);
						break;
					case "EDR":
						function = new EDR(bandsize,edr_delta);
						break;
					case "ERP":
						function = new ERP(bandsize,erp_g);
						break;
					case "LCS":
						function = new LCSS(lcss_delta,lcss_epsilon);
						break;
					case "ED":
						function = new ED();
						break;
					case "NewGotoh":
						function = new NewGotoh(co, cr);
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

//			if(truth_label-1 < 0 || truth_label-1 >= class_num 
//					|| predict_label-1 < 0 || predict_label-1 >= class_num) {
//				return "";
//			}
//			confusion_matrix[truth_label-1][predict_label-1]++;
			
			index++;
	      }
		
		//根据混淆矩阵得到precision recall fmeasure
//		double precision_all = 0.0;
//		double recall_all = 0.0;
//		double fmeasure_all = 0.0;
//		
//		double[] row_sum = new double[class_num];
//		double[] column_sum = new double[class_num];
//		for(int i = 0; i < class_num; i++) {
//			for(int j = 0; j < class_num; j++) {
//				row_sum[i] += confusion_matrix[i][j];
//			}
//		}
//		
//		for(int i = 0; i < class_num; i++) {
//			for(int j = 0; j < class_num; j++) {				
//				column_sum[i] += confusion_matrix[j][i];
//			}
//		}
//		
//		double precision_temp = 0.0;
//		double recall_temp    = 0.0;
//		double fmeasure_temp  = 0.0;
//		
//		for(int i = 0; i < class_num; i++) {
//			
//			if(column_sum[i] != 0.0) {
//				precision_temp = confusion_matrix[i][i]/column_sum[i];
//			}
//			if(row_sum[i] != 0.0) {
//				recall_temp = confusion_matrix[i][i]/row_sum[i];
//			}
//			if(recall_temp != 0.0 && precision_temp != 0.0) {
//				fmeasure_temp  = 2 * precision_temp * recall_temp 
//						/ (precision_temp + recall_temp); 
//			} 
//			
//			precision_all += precision_temp;
//			recall_all    += recall_temp;
//			fmeasure_all  += fmeasure_temp;
//		}
//		
//		precision_all /= class_num;
//		recall_all    /= class_num;
//		fmeasure_all  /= class_num;
//		
//		String line = "dataset: "+dataset+" method: " + method_str + " precision: "
//				+ precision_all + " recall: " + recall_all + " fmeasure: " + fmeasure_all;
		double accuracy = (double)right_labels/(double)test_size;
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		
		String file_line =  dataset + "\t" + decimalFormat.format(accuracy);
//		String file_line = dataset + "\t" + method_str + "\t" 
//				+ decimalFormat.format(precision_all)+ "\t" + decimalFormat.format(recall_all)
//				+ "\t"+ decimalFormat.format(fmeasure_all);
		
//		String file_line = co + "\t" 
//				+ decimalFormat.format(precision_all)+ "\t" + decimalFormat.format(recall_all)
//				+ "\t"+ decimalFormat.format(fmeasure_all);
		
//		writeFile(output_file, file_line);
//		System.out.println(line);
		return file_line;
	}
	
	public HashMap<double[], String> readFile(String filepath){
		HashMap<double[], String> timeseries_map = new LinkedHashMap<>();
		try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));//构造一个BufferedReader类来读取文件
            String line = null;
            while((line = br.readLine())!=null){//使用readLine方法，一次读一行
                
            	String[] array = line.split(",");
            	int length = array.length;
            	
            	String class_str = array[0];
            	double[] timeseries = new double[length-1];
            	for(int i = 0; i < length-1; i++){
            		timeseries[i] = Double.parseDouble(array[i+1]);
            	}
            	
            	//normalization
            	normalization(timeseries);
            	
            	timeseries_map.put(timeseries, class_str);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
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

package multidimensionaccuracyexp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import multidimension.DTWm;
import multidimension.EDRm;
import multidimension.LCSSm;
import multidimension.MultiDimensionDistanceFunction;
import multidimension.NewGotohm;
import onedimension.DTW;
import onedimension.ED;
import onedimension.EDR;
import onedimension.ERP;
import onedimension.LCSS;
import onedimension.NewGotoh;
import onedimension.UserDistanceFunction;
import utils.DataHandler;

public class MethodAccuracym {
	
	private final double edr_delta = 0.05;
	private final double lcss_delta = 1.0;
	private final double lcss_epsilon = 0.2;
	private double co = 1.0;
	private double cr = 1.0;
	private DataHandler handler = new DataHandler(); 
		
	/**
	 * get the accuracy of compared methods 
	 * @param filepath
	 * @return
	 */
	public String getComparedResult(String dir_path, String dataset, String method_str){
		
		//readData
		dir_path += "/"+dataset;
		ArrayList<String> file_list = handler.getFileList(dir_path);
		
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
		int[][] confusion_matrix = new int[class_num][class_num];
		
		Arrays.fill(min_distance, Double.MAX_VALUE);
		
		
		int index = 0;
		
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

			if(truth_label-1 < 0 || truth_label-1 >= class_num 
					|| predict_label-1 < 0 || predict_label-1 >= class_num) {
				return "";
			}
			confusion_matrix[truth_label-1][predict_label-1]++;
			
			index++;
	      }
		
		//根据混淆矩阵得到precision recall fmeasure
		double precision_all = 0.0;
		double recall_all = 0.0;
		double fmeasure_all = 0.0;
		
		double[] row_sum = new double[class_num];
		double[] column_sum = new double[class_num];
		for(int i = 0; i < class_num; i++) {
			for(int j = 0; j < class_num; j++) {
				row_sum[i] += confusion_matrix[i][j];
			}
		}
		
		for(int i = 0; i < class_num; i++) {
			for(int j = 0; j < class_num; j++) {				
				column_sum[i] += confusion_matrix[j][i];
			}
		}
		
		double precision_temp = 0.0;
		double recall_temp    = 0.0;
		double fmeasure_temp  = 0.0;
		
		for(int i = 0; i < class_num; i++) {
			
			if(column_sum[i] != 0.0) {
				precision_temp = confusion_matrix[i][i]/column_sum[i];
			}
			if(row_sum[i] != 0.0) {
				recall_temp = confusion_matrix[i][i]/row_sum[i];
			}
			if(recall_temp != 0.0 && precision_temp != 0.0) {
				fmeasure_temp  = 2 * precision_temp * recall_temp 
						/ (precision_temp + recall_temp); 
			} 
			
			precision_all += precision_temp;
			recall_all    += recall_temp;
			fmeasure_all  += fmeasure_temp;
		}
		
		precision_all /= class_num;
		recall_all    /= class_num;
		fmeasure_all  /= class_num;
		
		String line = "dataset: "+dataset+" method: " + method_str + " precision: "
				+ precision_all + " recall: " + recall_all + " fmeasure: " + fmeasure_all;
		
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		
		String file_line = dataset + "\t" + method_str + "\t" 
				+ decimalFormat.format(precision_all)+ "\t" + decimalFormat.format(recall_all)
				+ "\t"+ decimalFormat.format(fmeasure_all);
		
//		String file_line = co + "\t" 
//				+ decimalFormat.format(precision_all)+ "\t" + decimalFormat.format(recall_all)
//				+ "\t"+ decimalFormat.format(fmeasure_all);
		
//		writeFile(output_file, file_line);
//		System.out.println(line);
		return file_line;
	}
	
	public HashMap<NumberVector[], String> readFile(String filepath){
		HashMap<NumberVector[], String> timeseries_map = new LinkedHashMap<>();
		try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));//构造一个BufferedReader类来读取文件
            String line = null;
            while((line = br.readLine())!=null){//使用readLine方法，一次读一行
                
            	String[] array = line.split(";");
            	int length = array.length;          	
            	String class_str = array[0];

//            	ArrayList<NumberVector> vec = new ArrayList<>();
//            	for(int i = 1; i < length; i++){
//            		String[] temp = array[i].split(",");
//            		double[] tuple = new double[temp.length];
//                	for(int j = 0; j < temp.length; j++) {
//                		tuple[j] = Double.parseDouble(temp[j]);
//                	}
//                	vec.add(new DoubleVector(tuple));
//            	}
            	
            	NumberVector[] vecs = new NumberVector[length-1];
            	for(int i = 1; i < length; i++) {
            		String[] temp = array[i].split(",");
            		double[] tuple = new double[temp.length];
                	for(int j = 0; j < temp.length; j++) {
                		tuple[j] = Double.parseDouble(temp[j]);
                	}
                	handler.normalization(tuple);
                	vecs[i-1] = new DoubleVector(tuple);
            	}
            	
            	//normalization

            	timeseries_map.put(vecs, class_str);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		
		return timeseries_map;
		
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

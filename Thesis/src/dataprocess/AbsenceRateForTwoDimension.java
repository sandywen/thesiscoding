package dataprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;

public class AbsenceRateForTwoDimension {
	
	public String delete_timeseries_random(double[] input, double delete_rate) {
		String result = "";
		String timestamp = "";
		ArrayList<Double> list = new ArrayList<Double>();
		
		for(int i = 0; i < input.length; i++) {
			list.add(input[i]);
		}
		
		int length = (int) (list.size() * delete_rate);
		Random rand = new Random(5);
		for(int i = 0; i < length; i++) {
			int index = rand.nextInt(list.size());
			list.set(index, Double.MAX_VALUE);
		}
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) == Double.MAX_VALUE) {
				continue;
			}
			result += String.valueOf(list.get(i)) + ",";
			timestamp += i + ",";
		}

		return timestamp+";"+result;
	}
	
	public void delete(String dest_path, String source_path, double delete_rate) {
		
		//mk dir
		File dest_dir = new File(dest_path);
		if(dest_dir.mkdir()) {
			System.out.println("目标文件夹创建成功");
		}
		
		//copy test file
		MethodAccuracy acc = new MethodAccuracy();
		ArrayList<String> file_list = acc.getFileList(source_path);
		
		for(String file_name : file_list) {
			
			HashMap<double[], String> map = acc.readFile(source_path+"/"+file_name);
			String output_file = dest_path + "/" + file_name; 
			for(Map.Entry<double[], String> entry_test: map.entrySet()) {
				double[] vec = entry_test.getKey();
				String class_label = entry_test.getValue();
				String line = class_label + ";";
				
				if(file_name.contains("TEST")) {
					
					int low = vec.length;
					int high = low;
					
					AbsenceLengthForTwoDimension absence = new AbsenceLengthForTwoDimension();
					line += absence.delete_ts_continue(vec, low, high);
					
				}else if(file_name.contains("TRAIN")) {
					
					line += delete_timeseries_random(vec, delete_rate);
				}
				acc.writeFile(output_file, line);
			 }
			
		}
		
		
	}
	public static void main(String[] args) {
		String source_path = "/Users/wencheng/test";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/testfile_absence_rate2";
		
		MethodAccuracy exp = new MethodAccuracy();
		ArrayList<String> dir_list = exp.getFileList(source_path);
		
		AbsenceRateForTwoDimension absence = new AbsenceRateForTwoDimension();
		
		for(String dir : dir_list) {
			for(double i = 0.1; i < 0.6; i += 0.1) {
				absence.delete(dest_path+"/"+dir+"-"+i, source_path+"/"+dir, i);
			}
		}
		
		
	}
	

}

package dataprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;

public class AbsenceLengthForTwoDimension {
	
	public String delete_ts_continue(double[] input, int low, int high) {
		String ts = "";
		String timestamp = "";
		int length = input.length;
		for(int i = 0; i < length; i++) {
			if(i >= low && i <= high) {
				continue;
			}
			ts += String.valueOf(input[i]) + ",";
			timestamp += i + ",";
		}
		
		return timestamp+";"+ts;
	}
	
	public void delete(String dest_path, String source_path, int delete_length) {
		
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
					
					line += delete_ts_continue(vec, low, high);
					
				}else if(file_name.contains("TRAIN")) {
						
					Random rand = new Random(5);
					int low = rand.nextInt(vec.length-delete_length);
					int high = low + delete_length - 1;
					
					line += delete_ts_continue(vec, low, high);
					
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
		
		AbsenceLengthForTwoDimension absence = new AbsenceLengthForTwoDimension();
		
		for(String dir : dir_list) {
			for(int i = 20; i <= 100; i += 20) {
				absence.delete(dest_path+"/"+dir+"-"+i, source_path+"/"+dir, i);
			}
		}
		
		
	}
	

}

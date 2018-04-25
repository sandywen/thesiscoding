package dataprocesstrain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accuracyexp.MethodAccuracy;
import dataprocess.AbsenceLengthForTwoDimension;
import dataprocess.AbsenceRateForTwoDimension;
import utils.DataHandler;

public class AbsenceRateForMultiDimension {
	
	
	public void delete(String dest_path, String source_path, double delete_rate) {
		
		//mk dir
		File dest_dir = new File(dest_path);
		if(dest_dir.mkdir()) {
			System.out.println("目标文件夹创建成功");
		}
		
		//copy test file
		MethodAccuracy acc = new MethodAccuracy();
		ArrayList<String> file_list = acc.getFileList(source_path);
		DataHandler handler = new DataHandler();
		String test_output = dest_path + "/";
		String train_output = dest_path + "/";
		HashMap<double[], String> map = null;
		
		for(String file_name : file_list) {
			if(file_name.contains("TRAIN")) {
				map = acc.readFile(source_path+"/"+file_name);
				test_output  += file_name+"-TEST"; 
				train_output += file_name;
			}
		}
		
		if(map == null) {
			return;
		}

		for(Map.Entry<double[], String> entry_test: map.entrySet()) {
			double[] vec = entry_test.getKey();
			String class_label = entry_test.getValue();
			String line_test  = class_label + ";";
			String line_train = class_label + ";";
			AbsenceRateForTwoDimension absence = new AbsenceRateForTwoDimension();
			
			line_test  += handler.delete_ts_continue(vec, vec.length, vec.length);
			line_train += absence.delete_timeseries_random(vec, delete_rate);
			
			acc.writeFile(test_output, line_test);
			acc.writeFile(train_output, line_train);
		}
		
		
	}
	
	public static void main(String[] args) {
		
		String source_path = "/Users/wencheng/test";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/testfile_train_rate2";
		
		MethodAccuracy exp = new MethodAccuracy();

		AbsenceRateForMultiDimension absence = new AbsenceRateForMultiDimension();
		ArrayList<String> dir_list = exp.getFileList(source_path);
		
		for(String dir : dir_list) {
			for(double i = 0.1; i < 0.9; i += 0.1) {
				absence.delete(dest_path+"/"+dir+"-"+i, source_path+"/"+dir, i);
			}
		}

	}

}

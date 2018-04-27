package dataprocesstrain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;
import dataprocess.AbsenceLengthForOneDimension;
import dataprocess.AbsenceRateForOneDimension;

public class AbsenceRateForOneDimensionTrain {
	
	
	public void delete(String dest_path, String source_path, double delete_rate) {
		
		//mk dir
		File dest_dir = new File(dest_path);
		if(dest_dir.mkdir()) {
			System.out.println("目标文件夹创建成功");
		}
		
		//copy test file
		MethodAccuracy acc = new MethodAccuracy();
		ArrayList<String> file_list = acc.getFileList(source_path);
		String output_file = dest_path+"/";
		HashMap<double[], String> train_map = null;
		AbsenceLengthForOneDimension temp = new AbsenceLengthForOneDimension();
		
		for(String file_name : file_list) {
			if(file_name.contains("TRAIN")) {
				temp.copyFile(source_path+"/"+file_name, dest_path+"/"+file_name+"-TEST");
				train_map = acc.readFile(source_path+"/"+file_name);
				output_file += file_name; 
			}
		}
		
		//delete train file
	
		if(train_map == null) {
			return;
		}
		
		AbsenceRateForOneDimension rate = new AbsenceRateForOneDimension();
		for(Map.Entry<double[], String> entry_test: train_map.entrySet()) {
			double[] vec = entry_test.getKey();
			String class_label = entry_test.getValue();
			
//			String line = class_label + ","+ rate.delete_timeseries_random(vec, delete_rate);
			
//			acc.writeFile(output_file, line);
		}
		
		
	}
	
	public static void main(String[] args) {
		
		String source_path = "/Users/wencheng/test";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/testfile_train_rate";
		
		MethodAccuracy exp = new MethodAccuracy();

		AbsenceRateForOneDimensionTrain absence = new AbsenceRateForOneDimensionTrain();
		ArrayList<String> dir_list = exp.getFileList(source_path);
		
		for(String dir : dir_list) {
			for(double i = 0.1; i < 0.9; i += 0.1) {
				absence.delete(dest_path+"/"+dir+"-"+i, source_path+"/"+dir, i);
			}
		}

	}

}

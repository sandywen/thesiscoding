package dataprocesstrain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;
import dataprocess.AbsenceLengthForOneDimension;

public class AbsenceLengthForOneDimensionTrain {

	public void delete(String dest_path, String source_path, int delete_length) {
		
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
		for(Map.Entry<double[], String> entry_test: train_map.entrySet()) {
			double[] vec = entry_test.getKey();
			String class_label = entry_test.getValue();
			
			Random rand = new Random(5);
			
			int low = rand.nextInt(vec.length-delete_length);
			int high = low + delete_length - 1;
			
			System.out.println(low);
			String line = class_label + ","+ temp.delete_timeseries(vec, low, high);
			
			acc.writeFile(output_file, line);
		}
		
		
	}
	
	public static void main(String[] args) {
		
		String source_path = "/Users/wencheng/test";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/testfile_train_length";
		
		MethodAccuracy exp = new MethodAccuracy();
		AbsenceLengthForOneDimensionTrain absence = 
				new AbsenceLengthForOneDimensionTrain();
		
		ArrayList<String> dir_list = exp.getFileList(source_path);
		
		for(String dir : dir_list) {
			for(int i = 20; i <= 160; i += 20) {
				absence.delete(dest_path+"/"+dir+"-"+i, source_path+"/"+dir, i);
			}
		}

	}

}

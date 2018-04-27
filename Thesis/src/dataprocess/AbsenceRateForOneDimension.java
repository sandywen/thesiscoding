package dataprocess;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;
import utils.DataHandler;

public class AbsenceRateForOneDimension {
	
	
	public String delete_timeseries_random(double[] input, double delete_rate, int seed) {
		String result = "";
		ArrayList<Double> list = new ArrayList<Double>();
		
		for(int i = 0; i < input.length; i++) {
			list.add(input[i]);
		}
		
		int length = (int) (list.size() * delete_rate);
		Random rand = new Random(seed);
		for(int i = 0; i < length; i++) {
			int index = rand.nextInt(list.size());
			System.out.println(index);
			list.remove(index);
		}
		for(int i = 0; i < list.size(); i++) {
			result += String.valueOf(list.get(i)) + ",";
		}

		return result;
	}
	
	public void delete(String dest_path, String source_path,
			double delete_rate, int seed) {
		
		//mk dir
		File dest_dir = new File(dest_path);
		if(dest_dir.mkdirs()) {
			System.out.println("目标文件夹创建成功");
		}
		
		//copy test file
		MethodAccuracy acc = new MethodAccuracy();
		ArrayList<String> file_list = acc.getFileList(source_path);
		String output_file = dest_path+"/";
		HashMap<double[], String> test_map = null;
		
		for(String file_name : file_list) {
			if(file_name.contains("TRAIN")) {
				DataHandler handler = new DataHandler();
				handler.copyFile(source_path+"/"+file_name, dest_path+"/"+file_name);
			}else if(file_name.contains("TEST")) {
				test_map = acc.readFile(source_path+"/"+file_name);
				output_file += file_name; 
			}
		}
		
		//delete train file
	
		if(test_map == null) {
			return;
		}
		for(Map.Entry<double[], String> entry_test: test_map.entrySet()) {
			double[] vec = entry_test.getKey();
			String class_label = entry_test.getValue();
				
			String line = class_label + ","+ delete_timeseries_random(vec, delete_rate,seed);
			
			acc.writeFile(output_file, line);
		}
	
	}
	
public static void main(String[] args) {
		
		String source_path = "/Users/wencheng/thesiscoding/Thesis/testfile";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/testfile_absence_rate";
		
		MethodAccuracy exp = new MethodAccuracy();
		AbsenceRateForOneDimension absence = new AbsenceRateForOneDimension();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		ArrayList<String> dir_list = exp.getFileList(source_path);
		
		for(String dir : dir_list) {
			for(double i = 0.1; i <= 0.6; i += 0.1) {
				String up_dir = dir + "/" + dir + "-" + decimalFormat.format(i);
				for(int j = 0; j < 10; j++) {
					String down_dir = dir + decimalFormat.format(i) +"-con-" + j;
					absence.delete(dest_path + "/" + up_dir + "/" + down_dir,
							source_path + "/" + dir, i, j);
				}
				
			}
		}

	}
	
	
	

}

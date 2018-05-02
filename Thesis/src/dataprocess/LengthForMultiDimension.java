package dataprocess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accuracyexp.MethodAccuracy;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import multidimensionaccuracyexp.MethodAccuracyNew;
import utils.DataHandler;

public class LengthForMultiDimension {
	
	public void copyFile(String source, String dest) {
		try {
			Files.copy((new File(source)).toPath(), 
					(new File(dest)).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] delete_timeseries(NumberVector[] input, int low, int high) {
		int length = input.length;
		String[] result = new String[length];
		
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < input[i].getDimensionality(); j++) {
				if(j >= low && j <= high) {
					continue;
				}
				result[i] += String.valueOf(input[i].doubleValue(j)) + " ";
			}
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
		DataHandler handler = new DataHandler();
		
		//copy test file
		MethodAccuracyNew acc = new MethodAccuracyNew();
		ArrayList<String> dir_list = acc.getFileList(source_path);
		String output_file = dest_path+"/";
		HashMap<NumberVector[], String> test_map = null;
		
		for(String dir_name : dir_list) {
			
			String spath = source_path + "/" + dir_name;
			File dpath = new File(dest_path+"/"+dir_name);
			if(dpath.mkdirs()) {
				System.out.println("目标文件夹创建成功");
			}
			
			ArrayList<String> file_list = handler.getFileList(spath);
			
			if(dir_name.contains("TRAIN")) {
				
				for(String file_name : file_list) {
					copyFile(spath+"/"+file_name, dpath+"/"+file_name);
				}
				
			}else if(dir_name.contains("TEST")) {
				
				
				test_map = acc.readFile(spath);
				
				for(Map.Entry<NumberVector[], String> entry_test: test_map.entrySet()) {
					NumberVector[] vec = entry_test.getKey();
					String class_label = entry_test.getValue();
					
					Random rand = new Random();
					
					int delete_length = (int) (delete_rate * vec.length);
					System.out.println(delete_length);
					int low = rand.nextInt(vec.length - delete_length);
//						System.out.println(low);
					int high = low + delete_length - 1;
					
					String[] line_array = delete_timeseries(vec, low, high);
					
					acc.writeFile(dpath + "/" + "Atest.txt", class_label + " " + line_array[0]);
					acc.writeFile(dpath + "/" + "Btest.txt", class_label + " " + line_array[1]);
					acc.writeFile(dpath + "/" + "Ctest.txt", class_label + " " + line_array[2]);
					
				}
				
				 
			}
		}
		
		//delete train file
	
		
		
		
	}
	
	public static void main(String[] args) {
		
		String source_path = "/Users/wencheng/thesiscoding/Thesis/dataset";
		String dest_path = "/Users/wencheng/thesiscoding/Thesis/dataset_absence";
		
		MethodAccuracyNew exp = new MethodAccuracyNew();
		LengthForMultiDimension absence = new LengthForMultiDimension();
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

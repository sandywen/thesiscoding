package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

public class DataHandler {
	
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
	
	public void copyFile(String source, String dest) {
		try {
			Files.copy((new File(source)).toPath(), 
					(new File(dest)).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public String delete_timeseries_random(double[] input, double delete_rate) {
		String result = "";
		ArrayList<Double> list = new ArrayList<Double>();
		
		for(int i = 0; i < input.length; i++) {
			list.add(input[i]);
		}
		
		int length = (int) (list.size() * delete_rate);
		Random rand = new Random(5);
		for(int i = 0; i < length; i++) {
			int index = rand.nextInt(list.size());
//			System.out.println(index);
			list.remove(index);
		}
		for(int i = 0; i < list.size(); i++) {
			result += String.valueOf(list.get(i)) + ",";
		}

		return result;
	}

}

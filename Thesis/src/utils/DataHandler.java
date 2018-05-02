package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.utilities.pairs.Pair;

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
	
	public ArrayList<String> readFile(String filepath, String symbol){
		ArrayList<String> timeseries_list = new ArrayList<>();
		try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));//构造一个BufferedReader类来读取文件
            String line = null;
            while((line = br.readLine())!=null){//使用readLine方法，一次读一行
            	
            	timeseries_list.add(line);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		
		return timeseries_list;
		
	}
	
	public Pair<NumberVector,String> stringTodouble(String line, String symbol){
		
		String[] array = line.split(symbol);
    	int length = array.length;
    	
    	String class_str = array[0];
    	double[] timeseries = new double[length-1];
    	for(int i = 0; i < length-1; i++){
    		timeseries[i] = Double.parseDouble(array[i+1]);
    	}
    	
    	//normalization
    	normalization(timeseries);
    	NumberVector vec = new DoubleVector(timeseries);
		return new Pair<NumberVector, String>(vec,class_str);
		
	}

}

package accuracyexp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import utils.DataHandler;

public class ParameterTuningExp {
	
	public static void main(String[] args) {
			
		String path   = "/Users/wencheng/thesiscoding/Thesis/testfile";
		String output = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-para-";
		
		DataHandler handler = new DataHandler();
		ArrayList<String> dir_list = handler.getFileList(path);
	
		ParameterTuningExp exp = new ParameterTuningExp();
		exp.run(dir_list,"go",path,output);
		exp.run(dir_list,"gr",path,output);
		
	}
	
	public void run(ArrayList<String> dir_list, String para, String path, String output ) {
		
		MethodAccuracy exp = new MethodAccuracy();
		String header = para+"\tprecision";
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		for(String dir : dir_list) {
			String output_path = output + dir + ".txt";
			exp.writeFile(output_path, header);
			for(double i = 0.0; i <= 1.0; i += 0.2) {
				if(para.equals("go")) {
					exp.setCo(i);
				}else {
					exp.setCr(i);
				}
				
				String result = exp.getComparedResult(path, dir, "NewGotoh");
				String[] line = result.split("\t");
				exp.writeFile(output_path, decimalFormat.format(i)+"\t"+line[1]);
			}
			
			for(double i = 1.0; i < 30; i += 5.0) {
				if(para.equals("go")) {
					exp.setCo(i);
				}else {
					exp.setCr(i);
				}
				String result = exp.getComparedResult(path, dir, "NewGotoh");
				String[] line = result.split("\t");
				exp.writeFile(output_path, decimalFormat.format(i)+"\t"+line[1]);
			}
		}
		
		
	}

}

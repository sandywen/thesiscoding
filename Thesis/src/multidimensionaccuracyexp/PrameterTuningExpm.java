package multidimensionaccuracyexp;

import java.util.ArrayList;

import utils.DataHandler;

public class PrameterTuningExpm {
	
	public static void main(String[] args) {
		
		MethodAccuracym exp = new MethodAccuracym();
		DataHandler handler = new DataHandler();
		
		String path   = "/Users/wencheng/thesiscoding/Thesis/testfile_train_rate2/50words";
//		String path   = "/Users/wencheng/Downloads/testfile";
		String output = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-go-rate2-";
		
		ArrayList<String> dir_list = handler.getFileList(path);
		
		String header = "go\tprecision\trecall\tfmeasure";

		for(String dir : dir_list) {
			String output_path = output + dir + ".txt";
			handler.writeFile(output_path, header);
			for(double i = 0.0; i <= 0.4; i += 0.02) {
				exp.setCo(i);
				String result = exp.getComparedResult(path, dir, "NewGotoh");
				String[] line = result.split("\t");
				handler.writeFile(output_path, i+"\t"+line[2]+"\t"+line[3]+"\t"+line[4]);
			}
			
//			for(double i = 1.0; i < 30; i += 5.0) {
//				exp.setCo(i);
//				String result = exp.getComparedResult(path, dir, "NewGotoh");
//				String[] line = result.split("\t");
//				handler.writeFile(output_path, i+"\t"+line[2]+"\t"+line[3]+"\t"+line[4]);
//			}
		}
		
		
	}

}

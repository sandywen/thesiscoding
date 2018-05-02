package multidimensionaccuracyexp;

import java.util.ArrayList;

public class AccuracyNew {
	
	public static void main(String[] arg) {
		
		MethodAccuracyNew exp = new MethodAccuracyNew();
		
		String path = "/Users/wencheng/thesiscoding/Thesis/dataset";
		
		ArrayList<String> dir_list = exp.getFileList(path);
		
		String header = "dataset\tDTW\tEDR\tLCS\tNewGotoh";
		
		String output_precision = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-multi-accuracy.txt";
		
		exp.writeFile(output_precision, header);
		
		String[] methods = {"DTW","EDR","LCS","NewGotoh"};

		for(String dir : dir_list) {
			
			String precision_line = dir + "\t";
			
			for(String str : methods) {
				String result = exp.getComparedResult( path, dir, str);
				if(result.equals("")) {
					break;
				}
				String[] line = result.split("\t");
				
				precision_line += line[1] + "\t";
			}
			
			System.out.println("accuracy: " + precision_line);
			
			exp.writeFile(output_precision, precision_line);
		}
		
		
	}

}

package multidimensionaccuracyexp;

import java.util.ArrayList;

import utils.DataHandler;

public class AccracyLengthExpm {
	
	public static void main(String[] arg) {
		
		MethodAccuracym exp = new MethodAccuracym();
		DataHandler handler = new DataHandler();
		
//		String path = "/Users/wencheng/Downloads/testfile2";
		String path   = "/Users/wencheng/thesiscoding/Thesis/testfile_train_length2/50words";
		
		ArrayList<String> dir_list = handler.getFileList(path);
		
		String header = "dataset\tDTW\tEDR\tLCS\tNewGotoh";
		
		String output_precision = "/Users/wencheng/thesiscoding/Thesis/experiments/multidimension/exp-train-length2-precision-50words.txt";
		String output_recall = "/Users/wencheng/thesiscoding/Thesis/experiments/multidimension/exp-train-length2-recall-50words.txt";
		String output_fscore = "/Users/wencheng/thesiscoding/Thesis/experiments/multidimension/exp-train-length2-fscore-50words.txt";
		
		handler.writeFile(output_precision, header);
		handler.writeFile(output_recall, header);
		handler.writeFile(output_fscore, header);
		
		String[] methods = {"DTW","EDR","LCS","NewGotoh"};
		for(String dir : dir_list) {
			
			String precision_line = dir + "\t";
			String recall_line    = dir + "\t";
			String fscore_line    = dir + "\t";
			
			for(String str : methods) {
				String result = exp.getComparedResult( path, dir, str);
				if(result.equals("")) {
					break;
				}
				String[] line = result.split("\t");
				
				precision_line += line[2] + "\t";
				recall_line    += line[3] + "\t";
				fscore_line    += line[4] + "\t";
			}
			
			System.out.println("precision: " + precision_line);
			System.out.println("recall: " + recall_line);
			System.out.println("fscore: " + fscore_line);
			
			handler.writeFile(output_precision, precision_line);
			handler.writeFile(output_recall, recall_line);
			handler.writeFile(output_fscore, fscore_line);
		}
		
		
	}

}

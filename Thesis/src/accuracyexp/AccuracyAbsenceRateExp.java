package accuracyexp;

import java.util.ArrayList;

public class AccuracyAbsenceRateExp {
	
public static void main(String[] arg) {
		
		MethodAccuracy exp = new MethodAccuracy();
		
		String path   = "/Users/wencheng/thesiscoding/Thesis/testfile_train_rate/BirdChicken";
//		String path   = "/Users/wencheng/test/";
		
		ArrayList<String> dir_list = exp.getFileList(path);
		
		String header = "数据集\tDTW\tEDR\tLCS\tERP\tNewGotoh";
		
		String output_precision = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-precision-train-rate-BirdChicken.txt";
		String output_recall = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-recall-train-rate-BirdChicken.txt";
		String output_fscore = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-fscore-train-rate-BirdChicken.txt";
		
		exp.writeFile(output_precision, header);
		exp.writeFile(output_recall, header);
		exp.writeFile(output_fscore, header);
		
		String[] methods = {"DTW","EDR","LCS","ERP","NewGotoh"};
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
			
			exp.writeFile(output_precision, precision_line);
			exp.writeFile(output_recall, recall_line);
			exp.writeFile(output_fscore, fscore_line);
		}
		
	}

}

package accuracyexp;

import java.util.ArrayList;

public class AccuracyExp {
	
	public static void main(String[] arg) {
		
		MethodAccuracy exp = new MethodAccuracy();
		
//		String path = "/Users/wencheng/test";
		String path = "/Users/wencheng/thesiscoding/Thesis/testfile";
		
		ArrayList<String> dir_list = exp.getFileList(path);
		
		String header = "dataset\tED\tDTW\tEDR\tLCS\tERP\tNewGotoh";
		
		String output_precision = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-accuracy.txt";
//		String output_recall = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-recall.txt";
//		String output_fscore = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-fscore.txt";
		
		exp.writeFile(output_precision, header);
//		exp.writeFile(output_recall, header);
//		exp.writeFile(output_fscore, header);
		
		String[] methods = {"ED","DTW","EDR","LCS","ERP","NewGotoh"};
//		String[] methods = {"DTW","EDR","LCS","ERP","NewGotoh"};
//		String[] methods = {"LCS"};
		for(String dir : dir_list) {
			
			String precision_line = dir + "\t";
//			String recall_line    = dir + "\t";
//			String fscore_line    = dir + "\t";
			
			for(String str : methods) {
				String result = exp.getComparedResult( path, dir, str);
				if(result.equals("")) {
					break;
				}
				String[] line = result.split("\t");
				
				precision_line += line[1] + "\t";
//				recall_line    += line[3] + "\t";
//				fscore_line    += line[4] + "\t";
			}
			
			System.out.println("accuracy: " + precision_line);
//			System.out.println("recall: " + recall_line);
//			System.out.println("fscore: " + fscore_line);
			
			exp.writeFile(output_precision, precision_line);
//			exp.writeFile(output_recall, recall_line);
//			exp.writeFile(output_fscore, fscore_line);
		}
		
		
	}

}

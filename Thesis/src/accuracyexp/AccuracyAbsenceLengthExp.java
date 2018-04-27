package accuracyexp;

import java.text.DecimalFormat;
import java.util.ArrayList;

import utils.DataHandler;

public class AccuracyAbsenceLengthExp {
	
	public static void main(String[] arg) {
		
		MethodAccuracy exp = new MethodAccuracy();
		DataHandler handler = new DataHandler();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		String path   = "/Users/wencheng/thesiscoding/Thesis/testfile_absence";
		
		ArrayList<String> dir_list = handler.getFileList(path);
		
		String header = "dataset\tDTW\tEDR\tLCS\tERP\tNewGotoh";
		
		String output = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-accuracy-length-";
		String log = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-accuracy-length-log-";
		
		
		
		String[] methods = {"DTW","EDR","LCS","ERP","NewGotoh"};
		for(String dir : dir_list) {
			
			String output_precision = output + dir + ".txt";
			String output_log = log + dir + ".txt";
			exp.writeFile(output_precision, header);
						
			for(double i = 0.1; i <= 0.6; i += 0.1) {
				String up_dir = dir+"-"+decimalFormat.format(i);
				String precision_line = up_dir + "\t";
		
				for(String str : methods) {
					ArrayList<String> dir_down_list = handler.getFileList(path+"/"+dir+"/"+up_dir);
					double accuracy = 0.0;
					exp.writeFile(output_log, str);
					for(String down_dir : dir_down_list) {
						String result = exp.getComparedResult( path+"/"+dir+"/"+up_dir, down_dir, str);
						if(result.equals("")) {
							break;
						}
						String[] line = result.split("\t");
						accuracy += Double.parseDouble(line[1]);
						exp.writeFile(output_log, down_dir+"\t"+line[1]);
//						System.out.println(down_dir+"\t"+line[1]);
						
					}
					precision_line += decimalFormat.format(accuracy/10) + "\t";
					
				}
				System.out.println("accuracy: " + precision_line);
				
				exp.writeFile(output_precision, precision_line);
			}
			
			
			

		}
		
	}


}

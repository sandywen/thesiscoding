package accuracyexp;

import java.util.ArrayList;

public class RunningtimeExp {
	
public static void main(String[] arg) {
		
		MethodAccuracy exp = new MethodAccuracy();
		
		String path = "/Users/wencheng/test";
//		String path = "/Users/wencheng/thesiscoding/Thesis/testfile"; 
		
		ArrayList<String> dir_list = exp.getFileList(path);
		
//		ArrayList<String> dir_list = new ArrayList<String>();
//		dir_list.add("BeetleFly");
		
		String header = "数据集\tED\tDTW\tEDR\tLCS\tERP\tNewGotoh";
		String output_file = "/Users/wencheng/thesiscoding/Thesis/experiments/exp-runningtime.dat";
		exp.writeFile(output_file, header);
		
		String[] methods = {"ED","DTW","EDR","LCS","ERP","NewGotoh"};
//		String[] methods = {"NewGotoh"};
		for(String dir : dir_list) {
			String line = "";
			line += dir + "\t";
			System.out.println(dir);
			for(String str : methods) {
				System.out.println(str);
				long start_time = System.currentTimeMillis(); 
				for(int i = 0; i < 5; i++) {
					exp.getComparedResult( path, dir, str);
				}
				long end_time = System.currentTimeMillis();
				long runningtime = (end_time - start_time)/5;
				line += runningtime + "\t";
			}
			System.out.println(line);
			exp.writeFile(output_file, line);
			
		}
		
	}
	

}

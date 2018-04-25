package onedimension;

import java.util.ArrayList;
import java.util.Arrays;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import utils.DataHandler;

public class NewGotoh implements UserDistanceFunction{
	
	private final double co;
	private final double cr;
	
	public NewGotoh(double co, double cr){
		this.co = co;
		this.cr = cr;
	}
	
	
	/*
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		int dim1 = v1.getDimensionality()+1;
		int dim2 = v2.getDimensionality()+1;
		
		double[][] A_matrix = new double[dim1][dim2];
		double[][] B_matrix = new double[dim1][dim2];
		double[][] D_matrix = new double[dim1][dim2];
		
		firstRow(A_matrix, D_matrix, dim1, v1, v2);

		
		for(int j = 1; j < dim2; j++){
			for(int i = 0; i < dim1; i++){
				if(i == 0){
					double val2 = v2.doubleValue(j-1);
					double B_value = B_matrix[i][j-1] + val2 * val2 * cr;
					double D_value = D_matrix[i][j-1] + val2 * val2 * co;
					B_matrix[i][j] = B_value<D_value?B_value:D_value;
					continue;
				}
				
				double val1 = v1.doubleValue(i-1);
				double val2 = v2.doubleValue(j-1);
				
				A_matrix[i][j] = Math.min(A_matrix[i-1][j] + val1* val1 * cr,
						D_matrix[i-1][j] + val1 * val1 * co);

				B_matrix[i][j] = Math.min(B_matrix[i][j-1] + val2 * val2 * cr, 
						D_matrix[i][j-1] + val2 * val2 * co);
				
				D_matrix[i][j] = Math.min(D_matrix[i-1][j-1] + delta(val1, val2),
						Math.min(A_matrix[i][j], B_matrix[i][j]));
				
			}
		}
		*/
	

	public void firstRow(double[] A_matrix, double[] D_matrix, int dim1,
			NumberVector v1, NumberVector v2){
		
		for(int i = 1; i < dim1; i++){
			double val1 = v1.doubleValue(i-1);
			A_matrix[i] = Math.min(A_matrix[i-1] + val1 * val1 * cr,
					D_matrix[i-1] + val1 * val1 * co);
			D_matrix[i] = A_matrix[i];
		}
	}
	
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		int dim1 = v1.getDimensionality();
		int dim2 = v2.getDimensionality();
		
		if(dim1 > dim2) {
			return distance(v2,v1);
		}
		
		dim1 += 1;
		dim2 += 1;
		
		double[] A_matrix = new double[dim1];
		double[] B_matrix = new double[2*dim1];
		double[] D_matrix = new double[2*dim1];
//		int[][] states = new int[dim1][dim2];
		
		Arrays.fill(A_matrix, Double.MAX_VALUE);
		Arrays.fill(B_matrix, Double.MAX_VALUE);
		Arrays.fill(D_matrix, Double.MAX_VALUE);
		
//		A_matrix[0] = 0;
//		B_matrix[0] = 0;
		D_matrix[0] = 0;
		
		firstRow(A_matrix, D_matrix, dim1, v1, v2);
		
		int cur = 0;
		int next = dim1;

		for(int j = 1; j < dim2; j++){
			for(int i = 0; i < dim1; i++){
				if(i == 0){
					double val2 = v2.doubleValue(j-1);
					double B_value = B_matrix[cur+i] + val2 * val2 * cr;
					double D_value = D_matrix[cur+i] + val2 * val2 * co;
					B_matrix[next+i] = B_value<D_value?B_value:D_value;
					D_matrix[next+i] = B_matrix[next+i];
					A_matrix[i] = Double.MAX_VALUE;
					continue;
				}
				
				double val1 = v1.doubleValue(i-1);
				double val2 = v2.doubleValue(j-1);
				
//				double state1 = A_matrix[i-1] + val1* val1 * cr;
//				double state2 = D_matrix[next+i-1] + val1 * val1 * co;
//				double state3 = B_matrix[cur+i] + val2 * val2 * cr;
//				double state4 = D_matrix[cur+i] + val2 * val2 * co;
//				double state5 = D_matrix[cur+i-1] + delta(val1, val2);
				
				A_matrix[i] = Math.min(A_matrix[i-1] + val1* val1 * cr,
						D_matrix[next+i-1] + val1 * val1 * co);

				B_matrix[next+i] = Math.min(B_matrix[cur+i] + val2 * val2 * cr, 
						D_matrix[cur+i] + val2 * val2 * co);
				
				D_matrix[next+i] = Math.min(D_matrix[cur+i-1] + delta(val1, val2),
						Math.min(A_matrix[i], B_matrix[next+i]));  
				
//				if(D_matrix[next+i] == state1) {
//					states[i][j] = 1;
//				}else if(D_matrix[next+i] == state2) {
//					states[i][j] = 2;
//				}else if(D_matrix[next+i] == state3) {
//					states[i][j] = 3;
//				}else if(D_matrix[next+i] == state4) {
//					states[i][j] = 4;
//				}else if(D_matrix[next+i] == state5) {
//					states[i][j] = 5;
//				}
				
			}
			cur  = dim1 - cur;
			next = dim1 - next;
		}
		
//		reversePath(states, dim1, dim2);
		return Math.sqrt(D_matrix[cur+dim1-1]);
	}
	
	
	/**
	 * get the distance of val1 and val2
	 * @param val1
	 * @param val2
	 * @return
	 */
	public double delta(double val1, double val2){
		return (val1-val2)*(val1-val2);
	}
	
	protected void reversePath(int[][] states,int dim1, int dim2) {
		int len = dim1 + dim2;
		ArrayList<int[]> path = new ArrayList<>();
//		path[0][0] = dim1-1;
//		path[0][1] = dim2-1;
		path.add(new int[]{dim1-1, dim2-1});
		int m = dim1 - 1, n = dim2 - 1;
		DataHandler handler = new DataHandler();
//		handler.writeFile("/Users/wencheng/align-0.2.txt", m + "\t" + n);
		for (int i = 1; i < len; i++) {
			if(m < 0 || n < 0){
				break;
			}
			if(states[m][n] == 1){
//				path[i][0] = m-2;
//				path[i][1] = n;
//				path.put(--m, n);
//				path.put(--m, n);
				path.add(new int[]{--m, n});
				path.add(new int[]{--m, n});
//				m -= 2;
			}else if(states[m][n] == 2){
//				path[i][0] = m-2;
//				path[i][1] = n-1;
//				path.put(--m, n);
//				path.put(--m, --n);
				path.add(new int[]{--m, n});
				path.add(new int[]{--m, --n});
//				m -= 2;
//				n -= 1;
			}else if(states[m][n] == 3){  
//				path[i][0] = m;
//				path[i][1] = n-2;
//				path.put(m, --n);
//				path.put(m, --n);
				path.add(new int[]{m, --n});
				path.add(new int[]{m, --n});
//				n -= 2;
			}else if(states[m][n] == 4){  
//				path[i][0] = m-1;
//				path[i][1] = n-2;
//				path.put(m, --n);
//				path.put(--m,--n);
				path.add(new int[]{m, --n});
				path.add(new int[]{--m, --n});
//				m -= 1;
//				n -= 2;
			}else if(states[m][n] == 5){  
//				path[i][0] = m-2;
//				path[i][1] = n-2;
//				path.put(--m, --n);
//				path.put(--m, --n);
				path.add(new int[]{--m, --n});
				path.add(new int[]{--m, --n});
				
			}
//			System.out.println(m+"\t"+n);
//			handler.writeFile("/Users/wencheng/align-0.2.txt", m + "\t" + n);
		}	
		String result = "";
		for(int i = 0; i < path.size(); i++) {
			int[] temp = path.get(i);
//			System.out.println(temp[0]+"\t"+temp[1]);
			result += temp[0] + "\t" + temp[1] + "\n";
			
		}
		handler.writeFile("/Users/wencheng/align-rate-6.0-cr.txt", result);
	} 
	
	
}

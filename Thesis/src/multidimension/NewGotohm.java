package multidimension;

import java.util.Arrays;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public class NewGotohm implements MultiDimensionDistanceFunction{
	
	private final double co;
	private final double cr;
	
	public NewGotohm(double co, double cr){
		this.co = co;
		this.cr = cr;
	}
	
	public double distance(NumberVector[] v1, NumberVector[] v2) {
		// TODO Auto-generated method stub
		
		int dim1 = v1[0].getDimensionality();
		int dim2 = v2[0].getDimensionality();
		
		if(dim1 > dim2) {
			return distance(v2,v1);
		}
		
		dim1 += 1;
		dim2 += 1;
		
		double[] A_matrix = new double[dim1];
		double[] B_matrix = new double[2*dim1];
		double[] D_matrix = new double[2*dim1];
		
		Arrays.fill(A_matrix, Double.MAX_VALUE);
		Arrays.fill(B_matrix, Double.MAX_VALUE);
		Arrays.fill(D_matrix, Double.MAX_VALUE);
		
//		A_matrix[0] = 0;
//		B_matrix[0] = 0;
		D_matrix[0] = 0;
		
		firstRow(A_matrix, D_matrix, dim1, v1);
		
		int cur = 0;
		int next = dim1;

		for(int j = 1; j < dim2; j++){
			for(int i = 0; i < dim1; i++){
				if(i == 0){
//					NumberVector val2 = v2.get(j-1);
					double B_value = B_matrix[cur+i] + delta(v2,j-1) * cr;
					double D_value = D_matrix[cur+i] + delta(v2,j-1)* co;
					B_matrix[next+i] = B_value<D_value?B_value:D_value;
					D_matrix[next+i] = B_matrix[next+i];
					A_matrix[0] = Double.MAX_VALUE;
					continue;
				}
				
//				NumberVector val1 = v1.get(i-1);
//				NumberVector val2 = v2.get(j-1);
				
				A_matrix[i] = Math.min(A_matrix[i-1] + delta(v1,i-1) * cr,
						D_matrix[next+i-1] + delta(v1,i-1) * co);

				B_matrix[next+i] = Math.min(B_matrix[cur+i] + delta(v2,j-1) * cr, 
						D_matrix[cur+i] + delta(v2,j-1) * co);
				
				D_matrix[next+i] = Math.min(D_matrix[cur+i-1] + delta(v1, v2, i-1, j-1),
						Math.min(A_matrix[i], B_matrix[next+i]));
				
			}
			cur  = dim1 - cur;
			next = dim1 - next;
		}
		
		return Math.sqrt(D_matrix[cur+dim1-1]);
	}
	
	public void firstRow(double[] A_matrix, double[] D_matrix, int dim1,
			NumberVector[] v1){
		
		for(int i = 1; i < dim1; i++){
//			NumberVector val1 = v1.get(i-1);
			A_matrix[i] = Math.min(A_matrix[i-1] + delta(v1,i-1) * cr,
					D_matrix[i-1] + delta(v1,i-1) * co);
			D_matrix[i] = A_matrix[i];
		}
	}
	
	public static double delta(NumberVector[] val1, NumberVector[] val2, 
			int index1, int index2){
		double result = 0.0;
		int length = val1.length;
		for(int i = 0; i < length; i++) {
			result += (val1[i].doubleValue(index1) - val2[i].doubleValue(index2)) *
					(val1[i].doubleValue(index1) - val2[i].doubleValue(index2));
		}
		return result;
	}
	
	public static double delta(NumberVector[] val, int index) {
		double result = 0;
		int length = val.length;
		
		for(int i = 0; i < length; i++) {
			result += val[i].doubleValue(index) * val[i].doubleValue(index);
		}
		return result;
	}



}

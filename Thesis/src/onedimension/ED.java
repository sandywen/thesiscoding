package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public class ED implements UserDistanceFunction{

	@Override
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		int dim1 = v1.getDimensionality();
		int dim2 = v2.getDimensionality();
		
		int length = Math.min(dim1, dim2);
		double distance = 0.0;
		
		for(int i = 0; i < length; i++) {
			distance += delta(v1.doubleValue(i),v2.doubleValue(i));
		}
		return Math.sqrt(distance);
		
	}
	
	public double delta(double val1, double val2) {
		return Math.pow(val1-val2, 2);
	}
	
	

}

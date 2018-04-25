package multidimension;

import java.util.Arrays;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public class EDRm implements MultiDimensionDistanceFunction{
	
	 private final double delta;
	 private final double bandSize;

	  /**
	   * Constructor.
	   * 
	   * @param bandSize Band size
	   * @param delta Allowed delta
	   */
	  public EDRm(double delta, double bandSize) {
	    this.bandSize = bandSize;
	    this.delta = delta;
	  }


	@Override
	public double distance(NumberVector[] v1, NumberVector[] v2) {
		// TODO Auto-generated method stub
		 // Dimensionality, and last valid value in second vector:
	    final int dim1 = v1[0].getDimensionality(), dim2 = v2[0].getDimensionality();
	    
	   
	    // Current and previous columns of the matrix
	    double[] buf = new double[dim2 << 1];
	    Arrays.fill(buf, Double.POSITIVE_INFINITY);

	    // Fill first row:
	    firstRow(buf, bandSize, v1, v2, dim2);

	    final int m2 = dim2 - 1;
	    // Active buffer offsets (cur = read, nxt = write)
	    int cur = 0, nxt = dim2;
	    // Fill remaining rows:
	    int i = 1, l = 0, r = (int) Math.min(m2, i + bandSize);
	    while(i < dim1) {
//	      final double val1 = v1.doubleValue(i);
	      for(int j = l; j <= r; j++) {
	        // Value in previous row (must exist, may be infinite):
	        double min = buf[cur + j];
	        // Diagonal:
	        if(j > 0) {
	          final double pij = buf[cur + j - 1];
	          min = (pij < min) ? pij : min;
	          // Previous in same row:
	          if(j > l) {
	            final double pj = buf[nxt + j - 1];
	            min = (pj < min) ? pj : min;
	          }
	        }
	        // Write:
	        buf[nxt + j] = min + delta(v1, v2, i, j);
	      }
	      // Swap buffer positions:
	      cur = dim2 - cur;
	      nxt = dim2 - nxt;
	      // Update positions:
	      ++i;
	      if(i > bandSize) {
	        ++l;
	      }
	      if(r < m2) {
	        ++r;
	      }
	    }

	    return buf[cur + dim2 - 1];
	}
	
	protected void firstRow(double[] buf, double band, 
			NumberVector[] v1, NumberVector[] v2, int dim2) {
	    // First cell:
//	    final double val1 = v1[].doubleValue(0);
	    buf[0] = delta(v1, v2, 0, 0);

	    // Width of valid area:
	    final double w = (band >= dim2) ? dim2 - 1 : band;
	    // Fill remaining part of buffer:
	    for(int j = 1; j <= w; j++) {
	      buf[j] = buf[j - 1] + delta(v1, v2, 0, j);
	    }
	}
	
	public double delta(NumberVector[] val1, NumberVector[] val2, 
			int index1, int index2){
		int length = val1.length;
		for(int i = 0; i < length; i++) {
			if(Math.abs(val1[i].doubleValue(index1)-val2[i].doubleValue(index2)) >= delta) {
				return 1.0;
			}
		}
		return 0.0;
	}
	
	public double delta(NumberVector[] val, int index) {
		double result = 0;
		int length = val.length;
		
		for(int i = 0; i < length; i++) {
			result += val[i].doubleValue(index) * val[i].doubleValue(index);
		}
		return result;
	}
	

}

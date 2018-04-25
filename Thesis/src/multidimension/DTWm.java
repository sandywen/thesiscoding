package multidimension;

import java.util.Arrays;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public class DTWm implements MultiDimensionDistanceFunction{
	 private double bandSize;
	
	  /**
	   * Constructor.
	   * 
	   * @param bandSize Band size
	   */
	  public DTWm(double bandSize) {
		  this.bandSize = bandSize;
	  }

		
	  /**
	   * Fill the first row.
	   * 
	   * @param buf Buffer
	   * @param band Bandwidth
	   * @param v1 First vector
	   * @param v2 Second vector
	   * @param dim2 Dimensionality of second
	   */
	  protected void firstRow(double[] buf, int band, NumberVector[] v1,
			  NumberVector[] v2, int dim2) {
	    // First cell:
	    buf[0] = NewGotohm.delta(v1, v2, 0, 0);

	    // Width of valid area:
	    final int w = (band >= dim2) ? dim2 - 1 : band;
	    // Fill remaining part of buffer:
	    for(int j = 1; j <= w; j++) {
	      buf[j] = buf[j - 1] + NewGotohm.delta(v1, v2,0,j);
	    }
	  }


	@Override
	public double distance(NumberVector[] v1, NumberVector[] v2) {
		// TODO Auto-generated method stub
		// Dimensionality, and last valid value in second vector:
	    final int dim1 = v1[0].getDimensionality(), dim2 = v2[0].getDimensionality();
	    final int m2 = dim2 - 1;
	    
	    // Current and previous columns of the matrix
	    double[] buf = new double[dim2 << 1];
	    Arrays.fill(buf, Double.POSITIVE_INFINITY);

	    // Fill first row:
	    firstRow(buf, (int)bandSize, v1, v2, dim2);

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
	        buf[nxt + j] = min + NewGotohm.delta(v1, v2, i, j);
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

	    // TODO: support Euclidean, Manhattan here:
	    return Math.sqrt(buf[cur + dim2 - 1]);
	}

}

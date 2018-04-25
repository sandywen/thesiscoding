package multidimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public class LCSSm implements MultiDimensionDistanceFunction{

	/**
	   * Keeps the currently set pDelta.
	   */
	  private double pDelta;

	  /**
	   * Keeps the currently set pEpsilon.
	   */
	  private double pEpsilon;

	  /**
	   * Constructor.
	   * 
	   * @param pDelta pDelta
	   * @param pEpsilon pEpsilon
	   */
	  public LCSSm(double pDelta, double pEpsilon) {
	    this.pDelta = pDelta;
	    this.pEpsilon = pEpsilon;
	  }

	@Override
	public double distance(NumberVector[] v1, NumberVector[] v2) {
		// TODO Auto-generated method stub
		final int dim1 = v1[0].getDimensionality(), dim2 = v2[0].getDimensionality();
	    if(dim1 > dim2) {
	      return distance(v2, v1);
	    }
	    final int delta = (int) Math.ceil(dim2 * pDelta);

	    // Compute value range, for scaling epsilon:
	    final double epsilon = getRange(v1, v2) * pEpsilon;

	    double[] curr = new double[dim2 + 1];
	    double[] next = new double[dim2 + 1];

	    for(int i = 0; i < dim1; i++) {
//	      final double ai = v1.doubleValue(i);
	      for(int j = Math.max(0, i - delta); j <= Math.min(dim2 - 1, i + delta); j++) {
//	        final double bj = v2.doubleValue(j);
	        if(delta(v1, v2, i, j, epsilon)) { // match
	          next[j + 1] = curr[j] + 1;
	        }
	        else if(curr[j + 1] > next[j]) { // ins
	          next[j + 1] = curr[j + 1];
	        }
	        else { // del
	          next[j + 1] = next[j];
	        }
	      }
	      // Swap
	      double[] tmp = curr;
	      curr = next;
	      next = tmp;
	    }

	    // search for maximum in the last line
	    double maxEntry = curr[1];
	    for(int i = 2; i < dim2 + 1; i++) {
	      maxEntry = (curr[i] > maxEntry) ? curr[i] : maxEntry;
	    }
	    final double sim = maxEntry / Math.min(dim1, dim2);
	    return 1. - sim;
	}
	
	public boolean delta(NumberVector[] v1, NumberVector[] v2,
			int index1, int index2, double epsilon) {
		
		for(int i = 0; i < v1.length; i++) {
			if(Math.abs(v1[i].doubleValue(index1) - v2[i].doubleValue(index2)) > epsilon) {
				return false;
			}
		}
		return true;
		
	}
	
	public double getRange(NumberVector[] v1, NumberVector[] v2) {
	    double min = v1[0].doubleValue(0), max = min;
	    int length1 = v1.length;
	    for(int i = 0; i < length1; i++) {
	    	int dim1 = v1[i].getDimensionality();
	    	for(int j = 0; j < dim1; j++) {
	    		final double v = v1[i].doubleValue(j);
	  	      	min = (v < min) ? v : min;
	  	      	max = (v > max) ? v : max;
	    	}
	      
	    }
	    
	    int length2 = v2.length;
	    for(int i = 0; i < length2; i++) {
	    	int dim2 = v2[i].getDimensionality();
	    	for(int j = 0; j < dim2; j++) {
	    		final double v = v2[i].doubleValue(j);
	   	      	min = (v < min) ? v : min;
	   	      	max = (v > max) ? v : max;
	    	}
	     
	    }
	    final double range = max - min;
	    return range;
	  }

}

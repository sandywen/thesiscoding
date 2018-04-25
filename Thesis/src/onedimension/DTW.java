package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.timeseries.DTWDistanceFunction;

public class DTW implements UserDistanceFunction{
	
	private DTWDistanceFunction function;
	
	public DTW(double bandsize){
		function = new DTWDistanceFunction(bandsize);
	}

	@Override
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		return function.distance(v1, v2);
	}
	
	
	
	

}

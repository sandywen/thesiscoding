package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.timeseries.EDRDistanceFunction;

public class EDR implements UserDistanceFunction{
	
	private EDRDistanceFunction function;
	
	public EDR(double bandsize, double delta) {
		function = new EDRDistanceFunction(bandsize, delta);
	}

	@Override
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		return function.distance(v1, v2);
	}

}

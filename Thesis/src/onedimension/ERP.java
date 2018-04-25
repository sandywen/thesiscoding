package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.timeseries.ERPDistanceFunction;

public class ERP implements UserDistanceFunction{
	
	private ERPDistanceFunction function;
	
	public ERP(double bandsize, double g) {
		function = new ERPDistanceFunction(bandsize, g);
	}

	@Override
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		return function.distance(v1, v2);
	}
	

}
